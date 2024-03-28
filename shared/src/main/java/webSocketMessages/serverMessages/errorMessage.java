package webSocketMessages.serverMessages;

public class errorMessage extends ServerMessage{
    private String errorMessage;
    public errorMessage(ServerMessageType type) {
        super(type);
    }
}
