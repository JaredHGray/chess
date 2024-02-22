package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Set;

public interface UserDAO {
    void addUser(UserData registerUser) throws DataAccessException;
    UserData getUser(UserData registerUser) throws DataAccessException;

    UserData verifyUser(UserData verifyUser) throws DataAccessException;

    void clearUsers() throws DataAccessException;

    Set<UserData> getUsers() throws DataAccessException;
}

///need to create actual DAOs