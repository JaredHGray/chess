package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.Set;

public interface GameDAO {
    boolean createGame(GameData newGame, int gameID) throws DataAccessException;

    Set<GameData> listGames() throws DataAccessException;

    GameData findGame(int gameID) throws DataAccessException;

    boolean joinGame(int gameID, String user, String playerColor) throws DataAccessException;

    void clearGames() throws DataAccessException;

    public void updateGame(int gameID, ChessGame game) throws DataAccessException;
}
