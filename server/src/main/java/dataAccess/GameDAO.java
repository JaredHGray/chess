package dataAccess;
import model.GameData;

public interface GameDAO {
    void createGame(GameData newGame, String username, int gameID) throws DataAccessException;
}
