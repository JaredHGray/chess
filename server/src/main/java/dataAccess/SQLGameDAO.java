package dataAccess;

import model.GameData;

import java.util.Set;

public class SQLGameDAO implements GameDAO{

    public void createGame(GameData newGame, int gameID) throws DataAccessException {

    }

    public Set<GameData> listGames() throws DataAccessException {
        return null;
    }

    public GameData findGame(int gameID) throws DataAccessException {
        return null;
    }

    public void joinGame(int gameID, String user, String playerColor) throws DataAccessException {

    }

    public void clearGames() throws DataAccessException {

    }
}
