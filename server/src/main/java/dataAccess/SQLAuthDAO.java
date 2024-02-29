package dataAccess;

public class SQLAuthDAO implements AuthDAO{

    public void createAuth(String username, String authToken) throws DataAccessException {

    }

    public String getAuth(String authID) throws DataAccessException {
        return null;
    }

    public boolean deleteAuth(String authToken) throws DataAccessException {
        return false;
    }

    public void clearAuth() throws DataAccessException {

    }

    public String getUser(String username) throws DataAccessException {
        return null;
    }
}
