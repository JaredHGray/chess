package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.*;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    public void addUser(UserData registerUser) throws DataAccessException {
        String hashedPassword = encryptPassword(registerUser.password());
        var insertStatement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {

            // Set values for parameters
            preparedStatement.setString(1, registerUser.username());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, registerUser.email());

            // Execute the query
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to insert data into users table: %s", ex.getMessage()));
    }
        }

    public UserData getUser(UserData registerUser) throws DataAccessException {
        var insertStatement = "SELECT username FROM users WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
             preparedStatement.setString(1, registerUser.username());
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return registerUser;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return null;
    }

    public UserData verifyUser(UserData verifyUser) throws DataAccessException {
        var insertStatement = "SELECT username, password FROM users WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, verifyUser.username());
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    if(verifyUsePassword(rs.getString("password"), verifyUser.password())){
                        return verifyUser;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return null;
    }

    public void clearUsers() throws DataAccessException {
        var insertStatement = "DROP table users";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(insertStatement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private boolean verifyUsePassword(String storedPassword, String clearTextPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(storedPassword, clearTextPassword);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` VARCHAR(255) NOT NULL,
              `password` VARCHAR(255) NOT NULL,
              `email` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`username`)
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

