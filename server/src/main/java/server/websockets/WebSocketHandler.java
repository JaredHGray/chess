package server.websockets;
import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.notificationMessage;
import webSocketMessages.userCommands.*;


import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@WebSocket
public class WebSocketHandler {
    //onMessage function is used for everything then reroute to everything else
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, List<String>> gameUsersMap = new ConcurrentHashMap<>();
    private Session session;

    private AuthDAO authDAO;
    private GameDAO gameDAO;


    public WebSocketHandler(){
        try{
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, InvalidMoveException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        this.session = session;
        switch (action.getCommandType()){
            case JOIN_PLAYER:
                joinPlayer(new Gson().fromJson(message, joinPlayerCommand.class));
                break;
            case JOIN_OBSERVER:
                observePlayer(new Gson().fromJson(message, joinObserverCommand.class));
                break;
            case LEAVE:
                leaveGame(new Gson().fromJson(message, leaveCommand.class));
                break;
            case MAKE_MOVE:
                makeMove(new Gson().fromJson(message, makeMoveCommand.class));
                break;
        }
    }

    private void joinPlayer(joinPlayerCommand action) throws DataAccessException {
        var playerColor = action.getPlayerColor();
        int gameID = action.getGameID();
        String authToken = action.getAuthToken();
        String user = authDAO.getAuth(authToken);
        if(user != null && !user.isEmpty()){
            GameData gameData = gameDAO.findGame(gameID);
            if(gameData != null){
                if((playerColor == ChessGame.TeamColor.WHITE && Objects.equals(gameData.whiteUsername(), user)) || (playerColor == ChessGame.TeamColor.BLACK && Objects.equals(gameData.blackUsername(), user))){
                    connections.put(authToken, session);
                    gameUsersMap.computeIfAbsent(gameID, k -> new CopyOnWriteArrayList<>()).add(authToken);
                    LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
                    sendMessage(new Gson().toJson(loadGameMessage), session);
                    var message = String.format("%s joined the game as the %s player", user, playerColor);
                    broadcast(message, gameID, authToken);
                }else{
                    sendErrorMessage("Username does not match username associated with game");
                }
            }else{
                sendErrorMessage("invalid gameID");
            }
        } else{
            sendErrorMessage("invalid authToken");
        }
    }

    private void observePlayer(joinObserverCommand action) throws DataAccessException {
        int gameID = action.getGameID();
        String authToken = action.getAuthToken();
        String user = authDAO.getAuth(authToken);
        if(user != null && !user.isEmpty()){
            GameData gameData = gameDAO.findGame(gameID);
            if(gameData != null){
                    connections.put(authToken, session);
                    gameUsersMap.computeIfAbsent(gameID, k -> new CopyOnWriteArrayList<>()).add(authToken);
                    LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
                    sendMessage(new Gson().toJson(loadGameMessage), session);
                    var message = String.format("%s joined the game as an observer", user);
                    broadcast(message, gameID, authToken);
            }else{
                sendErrorMessage("invalid gameID");
            }
        } else{
            sendErrorMessage("invalid authToken");
        }
    }

    private void leaveGame(leaveCommand action) throws DataAccessException {
        int gameID = action.getGameID();
        String authToken = action.getAuth();
        String user = authDAO.getAuth(authToken);
        if(user != null && !user.isEmpty()){
            GameData gameData = gameDAO.findGame(gameID);
            if(gameData != null){
                if(gameData.whiteUsername() != null && gameData.whiteUsername().equals(user)){
                    gameDAO.joinGame(gameID, null, "WHITE");
                    removeUserFromGame(gameID, authToken);
                }else if(gameData.blackUsername() != null && gameData.blackUsername().equals(user)){
                    gameDAO.joinGame(gameID, null, "BLACK");
                }
                removeUserFromGame(gameID,authToken);
                var message = String.format("%s left the game", user);
                broadcast(message, gameID, authToken);
            }else{
                sendErrorMessage("invalid gameID");
            }
        } else{
            sendErrorMessage("invalid authToken");
        }
    }

    public void makeMove(makeMoveCommand action) throws DataAccessException {
        int gameID = action.getGameID();
        String authToken = action.getAuth();
        String user = authDAO.getAuth(authToken);
        if(user != null && !user.isEmpty()){
            GameData gameData = gameDAO.findGame(gameID);
            if(gameData != null) {
                ChessPiece.PieceType pieceType = gameData.game().getBoard().getPiece(action.getMove().getStartPosition()).getPieceType();
                ChessGame.TeamColor turnColor = gameData.game().getTeamTurn();
                if ((turnColor == ChessGame.TeamColor.WHITE && gameData.whiteUsername().equals(user)) || (turnColor == ChessGame.TeamColor.BLACK && gameData.blackUsername().equals(user))) {
                    try {
                        gameData.game().makeMove(action.getMove());
                        if (turnColor == ChessGame.TeamColor.WHITE) {
                            gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                        } else {
                            gameData.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                        }
                    } catch (InvalidMoveException e) {
                        sendErrorMessage("Invalid move: The chess piece cannot move to the specified position.");
                        return;
                    }
                    gameDAO.updateGame(gameID, gameData.game());
                    loadBroadcast(gameID, gameData.game());
                    var message = String.format("%s moved the %s piece from %s to %s", user, pieceType, action.getMove().getStartPosition(), action.getMove().getEndPosition());
                    broadcast(message, gameID, authToken);
                } else {
                    sendErrorMessage("invalid gameID");
                }
            } else{
                sendErrorMessage("unauthorized to make move, not your turn");
            }
        } else{
            sendErrorMessage("invalid authToken");
        }
    }

    private void removeUserFromGame(int gameID, String authToken){
        if(gameUsersMap.get(gameID) != null){
            gameUsersMap.get(gameID).remove(authToken);
        }
    }

    private void loadBroadcast(int gameID, ChessGame game){
        List<String> tokens = gameUsersMap.get(gameID);
        for (String storedAuth : tokens) {
            if (connections.containsKey(storedAuth)) {
                Session session = connections.get(storedAuth);
                LoadGameMessage loadGameMessage = new LoadGameMessage(game);
                sendMessage(new Gson().toJson(loadGameMessage), session);
            }
        }
    }

    private void broadcast(String message, int gameID, String currentAuth) {
        List<String> tokens = gameUsersMap.get(gameID);
        for(String storedAuth : tokens) {
            if (!storedAuth.equals(currentAuth)) {
                for (String sessionAuth : connections.keySet()) {
                    if (storedAuth.equals(sessionAuth)) {
                        notificationMessage notificationMessage = new notificationMessage(message);
                        String notification = new Gson().toJson(notificationMessage);
                        sendMessage(notification, connections.get(sessionAuth));
                    }
                }
            }
        }
    }

    private void sendErrorMessage(String errorMessage) {
        ServerMessage error = new ErrorMessage(errorMessage);
        sendMessage(new Gson().toJson(error), session);
    }

    private void sendMessage(String message, Session sendSession) {
        try {
              sendSession.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
