package dataAccess;

public interface AuthDAO {

    boolean createAuth(String username, String authToken) throws DataAccessException;

    String getAuth(String authID) throws DataAccessException;

    boolean deleteAuth(String authToken) throws DataAccessException;

    void clearAuth() throws DataAccessException;

    String getUser(String username) throws DataAccessException;
}
