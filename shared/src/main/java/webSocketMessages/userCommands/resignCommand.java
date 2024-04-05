package webSocketMessages.userCommands;

public class resignCommand extends UserGameCommand{
    private int gameID;
    public resignCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.RESIGN;
    }

    public String getAuth(){
        return getAuthString();
    }

    public int getGameID(){
        return gameID;
    }
}
