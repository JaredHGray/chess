package dataAccess;

import model.AuthData;

import java.util.Set;

public interface AuthDAO {

    void createAuth(String username, String authToken) throws DataAccessException;

    String getAuth(String authID) throws DataAccessException;

    boolean deleteAuth(String authToken) throws DataAccessException;

    void clearAuth() throws DataAccessException;

    String getUser(String username) throws DataAccessException;
}
