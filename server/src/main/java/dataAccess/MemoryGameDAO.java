package dataAccess;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Set;
public class MemoryGameDAO implements GameDAO {

    Set<GameData> game = new HashSet<>();
    public void createGame(GameData newGame, String username, int gameID) throws DataAccessException {
        GameData createGame = new GameData(gameID, username, null, newGame.gameName(), null);
        game.add(createGame);
    }
}
