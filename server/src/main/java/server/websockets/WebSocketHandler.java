package server.websockets;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;


import java.io.IOException;
import java.util.Timer;
@WebSocket
public class WebSocketHandler {
    //onMessage function is used for everything then reroute to everything else
    private final ConnectionManager connections = new ConnectionManager();

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
