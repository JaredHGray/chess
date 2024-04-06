package dataAccess;

import java.sql.SQLException;
import static dataAccess.ConfigureDatabase.configureDatabase;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS auth (
            `username` VARCHAR(255) NOT NULL,
            `authToken` VARCHAR(255) NOT NULL,
            PRIMARY KEY (`authToken`)
            );
            """
        };
        configureDatabase(createStatements);
    }

    public boolean createAuth(String username, String authToken) throws DataAccessException {
        var insertStatement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        if(!authToken.equals(getUser(username))){
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(insertStatement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);

                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException ex) {
                throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
            }
        } else {
            return false;
        }
    }

    public String getAuth(String authID) throws DataAccessException {
        var insertStatement = "SELECT username FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, authID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
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
        var insertStatement = "SELECT authToken FROM auth WHERE username=?";
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
}
