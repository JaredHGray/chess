package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.util.HashSet;
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
            //var registerUser = new Gson().toJson(game);
            preparedStatement.setString(5, null);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
        }
    }

    public Set<GameData> listGames() throws DataAccessException {
        Set<GameData> games = new HashSet<>();
        var insertStatement = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int retrievedGameID = rs.getInt("gameID");
                    String retrievedWhiteUsername = rs.getString("whiteUsername");
                    String retrievedBlackUsername = rs.getString("blackUsername");
                    String retrievedGameName = rs.getString("gameName");
                    String retrievedGame = rs.getString("game");

                    ChessGame newGame = new Gson().fromJson(retrievedGame, ChessGame.class);
                    GameData gameData = new GameData(retrievedGameID, retrievedWhiteUsername, retrievedBlackUsername, retrievedGameName, newGame);
                    games.add(gameData);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return games;
    }

    public GameData findGame(int gameID) throws DataAccessException {
        var insertStatement = "SELECT gameID FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int retrievedGameID = rs.getInt("gameID");
                    String retrievedWhiteUsername = rs.getString("whiteUsername");
                    String retrievedBlackUsername = rs.getString("blackUsername");
                    String retrievedGameName = rs.getString("gameName");
                    String retrievedGame = rs.getString("game");
                    ChessGame newGame = new Gson().fromJson(retrievedGame, ChessGame.class);
                    return new GameData(retrievedGameID, retrievedWhiteUsername, retrievedBlackUsername, retrievedGameName, newGame);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return null;
    }

    public void joinGame(int gameID, String user, String playerColor) throws DataAccessException {
        var insertStatement = "SELECT gameID FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String updateColumn = (playerColor.equals("WHITE")) ? "whiteUsername" : (playerColor.equals("BLACK") ? "blackUsername" : null);

                    if (updateColumn != null) {
                        var updateStatement = String.format("UPDATE game SET %s=? WHERE gameID=?", updateColumn);
                        try (var updatePreparedStatement = conn.prepareStatement(updateStatement)) {
                            updatePreparedStatement.setString(1, user);
                            updatePreparedStatement.setInt(2, gameID);
                            updatePreparedStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
    }

    public void clearGames() throws DataAccessException {
        var insertStatement = "DELETE FROM game";
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
