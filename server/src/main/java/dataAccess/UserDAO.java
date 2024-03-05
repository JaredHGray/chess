package dataAccess;

import model.UserData;

public interface UserDAO {
    boolean addUser(UserData registerUser) throws DataAccessException;
    UserData getUser(UserData registerUser) throws DataAccessException;

    UserData verifyUser(UserData verifyUser) throws DataAccessException;

    void clearUsers() throws DataAccessException;

}

///need to create actual DAOs