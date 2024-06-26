package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;

import java.util.HashSet;
import java.util.Set;
import java.sql.*;
import static dataAccess.ConfigureDatabase.configureDatabase;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` INT NOT NULL,
            `whiteUsername` VARCHAR(255),
            `blackUsername` VARCHAR(255),
            `gameName` VARCHAR(255) NOT NULL,
            `game` JSON,
            PRIMARY KEY (`gameID`)
            );
            """
        };
        configureDatabase(createStatements);
    }

    public boolean createGame(GameData newGame, int gameID) throws DataAccessException {
        if(findGame(gameID) == null){
            var insertStatement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(insertStatement)) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, newGame.whiteUsername());
                preparedStatement.setString(3, newGame.blackUsername());
                preparedStatement.setString(4, newGame.gameName());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String gameJson = gson.toJson(newGame.game());
                preparedStatement.setString(5, gameJson);
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException ex) {
                throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
            }
        } else {
            return false;
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
        var insertStatement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
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

    public boolean joinGame(int gameID, String user, String playerColor) throws DataAccessException {
        if(findGame(gameID) != null){
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
                                return true;
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
            }
        }
        return false;
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        if(findGame(gameID) != null){
            var insertStatement = "SELECT gameID FROM game WHERE gameID=?";
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(insertStatement)) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        if (game != null) {
                            var updateStatement = "UPDATE game SET game=? WHERE gameID=?";
                            try (var updatePreparedStatement = conn.prepareStatement(updateStatement)) {
                                Gson gson = new GsonBuilder().serializeNulls().create();
                                String gameJson = gson.toJson(game);
                                updatePreparedStatement.setString(1, gameJson);
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
}
