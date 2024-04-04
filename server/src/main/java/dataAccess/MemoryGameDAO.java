package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.Set;
public class MemoryGameDAO implements GameDAO {

    Set<GameData> game = new HashSet<>();
    public boolean createGame(GameData newGame, int gameID) throws DataAccessException {
        GameData createGame = new GameData(gameID, null, null, newGame.gameName(), null);
        game.add(createGame);
        return false;
    }

    public Set<GameData> listGames() throws DataAccessException {
        return game;
    }


    public GameData findGame(int gameID) throws DataAccessException {
        if (!game.isEmpty()) {
            for (GameData findGame : game) {
                if (findGame.gameID() == gameID) {
                    return findGame;
                }
            }
        }
        return null;
    }

    public boolean joinGame(int gameID, String user, String playerColor) throws DataAccessException {
        if (!game.isEmpty()) {
            for (GameData findGame : game) {
                if (findGame.gameID() == gameID) {
                    if(playerColor.equals("WHITE")){
                        GameData createGame = new GameData(findGame.gameID(), user, findGame.blackUsername(), findGame.gameName(), findGame.game());
                        game.remove(findGame);
                        game.add(createGame);
                        return true;
                    } else if(playerColor.equals("BLACK")) {
                        GameData createGame = new GameData(findGame.gameID(), findGame.whiteUsername(), user, findGame.gameName(), findGame.game());
                        game.remove(findGame);
                        game.add(createGame);
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {}

    public void clearGames() throws DataAccessException {
        game.clear();
    }
}
