package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;

    public JoinObserverCommand(String authToken, int gameID) {
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
