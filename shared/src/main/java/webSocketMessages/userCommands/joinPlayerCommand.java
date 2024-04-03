package webSocketMessages.userCommands;

import chess.ChessGame;
import com.google.gson.Gson;

public class joinPlayerCommand extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor playerColor;

    public joinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
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
