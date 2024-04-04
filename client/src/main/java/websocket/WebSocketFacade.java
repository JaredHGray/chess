package websocket;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class WebSocketFacade extends Endpoint {
    Session session;
    private ServerMessageListener messageListener;
    public WebSocketFacade(String url) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI); //weird crap, this is a get request to be

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private void handleMessage(String message) {
        try {
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            switch (serverMessage.getServerMessageType()) {
                case LOAD_GAME:
                    LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                    messageListener.onLoadGame(loadGameMessage);
                    break;
                case NOTIFICATION:
                    notificationMessage notificationMessage = new Gson().fromJson(message, notificationMessage.class);
                    messageListener.onNotification(notificationMessage);
                    break;
                case ERROR:
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    messageListener.onError(errorMessage);
                    break;
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + message);
        }
    }

    public void joinPlayerSocket(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        joinPlayerCommand joinPlayerCommand = new joinPlayerCommand(authToken, gameID, playerColor);
        sendUserCommand(joinPlayerCommand);
    }

    public void observePlayerSocket(int gameID, String authToken){
        joinObserverCommand observerCommand = new joinObserverCommand(authToken, gameID);
        sendUserCommand(observerCommand);
    }

    public void leaveGameSocket(int gameID, String authToken){
        leaveCommand leaveCommand = new leaveCommand(authToken, gameID);
        sendUserCommand(leaveCommand);
    }

    private void sendUserCommand(UserGameCommand command) {
        try {
            String jsonCommand = new Gson().toJson(command);
            session.getBasicRemote().sendText(jsonCommand);
        } catch (IOException ex) {
            System.err.println("Error sending user command: " + ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void setMessageListener(ServerMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @ClientEndpoint
    public interface ServerMessageListener {
        void onLoadGame(LoadGameMessage message);
        void onNotification(notificationMessage message);
        void onError(ErrorMessage message);
    }
}

//would it be smarter to just draw chessboard after ws or keep it during http and also ws
//what should an observer see as  menu options?
//only works 1/2 the time??
//only sends to 1/2 thr players? steps thru correctly but doesnt actually connect?
