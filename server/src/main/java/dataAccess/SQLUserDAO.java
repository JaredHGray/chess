package dataAccess;

import model.UserData;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO{
    public UserData addUser(UserData registerUser) throws DataAccessException {

        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        var json = new Gson().toJson(registerUser);
       // var id = executeUpdate(statement, registerUser.username(), registerUser.password(), registerUser.email()); //actually make database?

        return new UserData(registerUser.username(), registerUser.password(), registerUser.email());
    }
}
