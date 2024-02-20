package dataAccess;
import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthData implements AuthDAO {
    Set<AuthData> auth = new HashSet<>();
    public void createAuth(String username, String authToken) throws DataAccessException {
        AuthData createToken = new AuthData(username, authToken);
        auth.add(createToken);
    }
}
