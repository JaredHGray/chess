package webSocketMessages.userCommands;

public class leaveCommand extends UserGameCommand{
    private int gameID;
    public leaveCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.LEAVE;
    }
}
