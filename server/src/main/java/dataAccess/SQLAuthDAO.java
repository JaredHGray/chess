package dataAccess;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createAuth(String username, String authToken) throws DataAccessException {
        var insertStatement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            // Set values for parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);
            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
        }
    }

    public String getAuth(String authID) throws DataAccessException {
        var insertStatement = "SELECT authToken FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, authID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return authID;
                    }
                }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return null;
    }

    public boolean deleteAuth(String authToken) throws DataAccessException {
        var insertStatement = "DELETE FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, authToken);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
    }

    public void clearAuth() throws DataAccessException {
        var insertStatement = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
    }

    public String getUser(String username) throws DataAccessException {
        var insertStatement = "SELECT username FROM auth WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("authToken");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` VARCHAR(255) NOT NULL,
            `username` VARCHAR(255) NOT NULL,
            PRIMARY KEY (`authToken`)
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
