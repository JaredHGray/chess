package dataAccess;
import model.GameData;

import java.util.Set;

public interface GameDAO {
    void createGame(GameData newGame, int gameID) throws DataAccessException;

    Set<GameData> listGames() throws DataAccessException;

    GameData findGame(int gameID) throws DataAccessException;

    void joinGame(int gameID, String user, String playerColor) throws DataAccessException;
}
