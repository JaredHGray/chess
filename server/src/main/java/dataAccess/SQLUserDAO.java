package dataAccess;

import model.UserData;
import com.google.gson.Gson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
import java.sql.DriverManager;

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
        return null;
    }

    public UserData verifyUser(UserData verifyUser) throws DataAccessException {
        return null;
    }

    public void clearUsers() throws DataAccessException {

    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return hashedPassword;
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

