package dataAccess;

import model.UserData;

public interface UserDAO {
    void addUser(UserData registerUser) throws DataAccessException;
    UserData getUser(UserData registerUser) throws DataAccessException;
}

///need to create actual DAOs