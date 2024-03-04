package dataAccess;

import model.GameData;

import java.util.Set;
import java.sql.*;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createGame(GameData newGame, int gameID) throws DataAccessException {
        var insertStatement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            // Set values for parameters
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, newGame.whiteUsername());
            preparedStatement.setString(3, newGame.blackUsername());
            preparedStatement.setString(4, newGame.gameName());
            preparedStatement.setString(5, null);
            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
        }
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
        var insertStatement = "DROP table game";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
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
