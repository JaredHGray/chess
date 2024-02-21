package dataAccess;
import model.GameData;

public interface GameDAO {
    void creatGame(GameData newGame, String username, int gameID) throws DataAccessException;
}
