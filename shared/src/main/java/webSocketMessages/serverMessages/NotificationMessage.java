package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    private final String message;
    public NotificationMessage(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.message = notification;
    }

    public String getMessage(){
        return message;
    }
}
