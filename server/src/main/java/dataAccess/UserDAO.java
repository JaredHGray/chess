package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {
    void addUser(UserData registerUser) throws DataAccessException;
    UserData getUser(UserData registerUser) throws DataAccessException;

    UserData verifyUser(UserData verifyUser) throws DataAccessException;
}

///need to create actual DAOs