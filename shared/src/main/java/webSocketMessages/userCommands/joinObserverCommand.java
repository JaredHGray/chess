package webSocketMessages.userCommands;

public class joinObserverCommand extends UserGameCommand {
    private int gameID;

    public joinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_OBSERVER;
    }
}
