package dataAccess;
import model.GameData;

import java.util.Set;

public interface GameDAO {
    void createGame(GameData newGame, int gameID) throws DataAccessException;

    Set<GameData> listGames() throws DataAccessException;
}
