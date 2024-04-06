package webSocketMessages.userCommands;

import chess.ChessGame;
import com.google.gson.Gson;

public class JoinPlayerCommand extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        commandType = CommandType.JOIN_PLAYER;
    }

    public String getAuthToken(){
        return getAuthString();
    }

    public ChessGame.TeamColor getPlayerColor(){
        return playerColor;
    }

    public int getGameID(){
        return gameID;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}