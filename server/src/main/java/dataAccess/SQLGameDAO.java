package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Set;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` INT NOT NULL,
            `whiteUsername` VARCHAR(255) NOT NULL,
            `blackUsername` VARCHAR(255) NOT NULL,
            `gameName` VARCHAR(255) NOT NULL,
            `game` JSON NOT NULL,
            PRIMARY KEY (`gameID`)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
