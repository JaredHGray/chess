package server.websockets;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;


import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    //onMessage function is used for everything then reroute to everything else
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();
    private Session session;

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    @OnWebSocketMessage
    public void onMessage(String message, Session session) throws DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        this.session = session;
        switch (action.getCommandType()){
            case JOIN_PLAYER:
                joinPlayer(new Gson().fromJson(message, joinPlayerCommand.class));
                break;
        }
    }

    private void joinPlayer(joinPlayerCommand action) throws DataAccessException {
        var playerColor = action.getPlayerColor();
        int gameID = action.getGameID();
        String authToken = action.getAuthToken();
        String user = authDAO.getAuth(authToken);
        if(!user.isEmpty()){
            GameData gameData = gameDAO.findGame(gameID);
            if(gameData != null){
                if((playerColor == ChessGame.TeamColor.WHITE && Objects.equals(gameData.whiteUsername(), user)) || (playerColor == ChessGame.TeamColor.BLACK && Objects.equals(gameData.blackUsername(), user))){
                    connections.put(user, session);
                    var message = String.format("%s joined the game as the %s player", user, playerColor);
                    broadcast(message);
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

    private void broadcast(String message) {
        for (Session sessionCheck : connections.values()) {
            sendMessage(sessionCheck, message);
        }
    }

    private void sendErrorMessage(String errorMessage) {
        ServerMessage error = new ErrorMessage(errorMessage);
        sendMessage(session, new Gson().toJson(error));
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
