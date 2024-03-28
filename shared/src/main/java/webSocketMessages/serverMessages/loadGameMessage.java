package webSocketMessages.serverMessages;

import chess.ChessGame;

public class loadGameMessage extends ServerMessage{
    private ChessGame game;
    public loadGameMessage(ServerMessageType type) {
        super(type);
    }
}
