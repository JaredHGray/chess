package webSocketMessages.serverMessages;

public class notificationMessage extends ServerMessage{
    private String message;
    public notificationMessage(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.message = notification;
    }

    public String getMessage(){
        return message;
    }
}
