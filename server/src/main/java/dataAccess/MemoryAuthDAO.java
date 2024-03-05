package dataAccess;
import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDAO implements AuthDAO {
    Set<AuthData> auth = new HashSet<>();
    public boolean createAuth(String username, String authToken) throws DataAccessException {
        if(!authToken.equals(getUser(username))){
            AuthData createToken = new AuthData(username, authToken);
            auth.add(createToken);
            return true;
        } else {
            return false;
        }
    }

    public String getAuth(String authID) throws DataAccessException {
        if (!auth.isEmpty()) {
            for (AuthData findAuth : auth) {
                if (findAuth.authToken().equals(authID)) {
                    return findAuth.username();
                }
            }
        }
        return null;
    }


    public boolean deleteAuth(String authToken) throws DataAccessException {
        if (!auth.isEmpty()) {
            for (AuthData findAuth : auth) {
                if (findAuth.authToken().equals(authToken)) {
                    auth.remove(findAuth);
                    return true;
                }
            }
        }
        return false;
    }

    public void clearAuth() throws DataAccessException {
        auth.clear();
    }

    public String getUser(String username) throws DataAccessException {
        if (!auth.isEmpty()) {
            for (AuthData findAuth : auth) {
                if (findAuth.username().equals(username)) {
                    return findAuth.authToken();
                }
            }
        }
        return null;
    }
}
