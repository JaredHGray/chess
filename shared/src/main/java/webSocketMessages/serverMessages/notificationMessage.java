package webSocketMessages.serverMessages;

public class notificationMessage extends ServerMessage{
    private String message;
    public notificationMessage(ServerMessageType type) {
        super(type);
    }
}
