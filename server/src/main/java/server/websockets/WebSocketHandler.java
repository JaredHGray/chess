package server.websockets;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;



import java.io.IOException;
import java.util.Timer;
@WebSocket
public class WebSocketHandler {
    //onMessage function is used for everything then reroute to everything else
    private final ConnectionManager connections = new ConnectionManager();

//    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.webSocket("/connect", WebSocketHandler.class);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//    }
    @OnWebSocketMessage
    public void onMessage(String message, Session session) {
        Gson gson = new Gson();
        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            String commandType = json.get("commandType").getAsString();
            String authToken = json.get("authToken").getAsString();

            if (commandType.equals("JOIN_PLAYER")) {
                int gameID = json.get("gameID").getAsInt();
                ChessGame.TeamColor playerColor = teamColor(json.get("playerColor").getAsString()); // Assuming client provides color
                UserGameCommand command = new joinPlayerCommand(authToken, gameID, playerColor);
                handleUserCommand(session, command);
            }

        } catch (JsonSyntaxException | IllegalStateException e) {
            sendErrorMessage(session, "error: Invalid message");
        }
    }

    private void handleUserCommand(Session session, UserGameCommand command) {
        // Validate command and process accordingly
        // For simplicity, assume command is valid and send a dummy load game message
        LoadGameMessage loadGameMessage = new LoadGameMessage();

        // Set the ChessGame object in the LoadGameMessage instance
        // loadGameMessage.setGame(chessGame);

        sendServerMessage(session, loadGameMessage);
    }

    private void sendServerMessage(Session session, ServerMessage message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        sendMessage(session, json);
    }

    private void sendErrorMessage(Session session, String errorMessage) {
        ErrorMessage error = new ErrorMessage(errorMessage);
        sendServerMessage(session, error);
    }

    public void sendMessage(Session session, String message) {
        RemoteEndpoint remote = session.getRemote();
        try {
            remote.sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinGameMessage(String playerName, String playerColor, String auth, int gameID){
        ChessGame.TeamColor color = teamColor(playerColor);
       // try {
            var message = String.format("%s joined the game as the %s player", playerName, playerColor);
            var notification = new joinPlayerCommand(auth, gameID, color);
         //   connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new DataAccessException(ex.getMessage());
//        }
    }

    private ChessGame.TeamColor teamColor(String playerColor){
        if(playerColor.equalsIgnoreCase("white")){
            return ChessGame.TeamColor.WHITE;
        }else if(playerColor.equalsIgnoreCase("black")){
            return ChessGame.TeamColor.BLACK;
        }else{
            return null;
        }
    }
}
