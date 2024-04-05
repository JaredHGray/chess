package webSocketMessages.userCommands;

public class joinObserverCommand extends UserGameCommand {
    private int gameID;

    public joinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_OBSERVER;
    }

    public String getAuth(){
        return getAuthString();
    }

    public int getGameID(){
        return gameID;
    }
}
