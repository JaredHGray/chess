package webSocketMessages.userCommands;

import chess.ChessMove;

public class makeMoveCommand extends UserGameCommand{
    private int gameID;
    private ChessMove move;
    public makeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }

    public int getGameID(){
        return gameID;
    }

    public String getAuth(){
        return getAuthString();
    }

    public ChessMove getMove(){
        return move;
    }
}
