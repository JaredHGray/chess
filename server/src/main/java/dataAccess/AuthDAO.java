package dataAccess;

public interface AuthDAO {

    void createAuth(String username, String authToken) throws DataAccessException;

    boolean getAuth(String authID) throws DataAccessException;
}
