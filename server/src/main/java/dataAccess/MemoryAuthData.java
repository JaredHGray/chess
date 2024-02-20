package dataAccess;
import model.AuthData;
import spark.Spark;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MemoryAuthData implements AuthDAO {
    Set<AuthData> auth = new HashSet<>();
    public void createAuth(String username) throws DataAccessException {
        String authToken = generateToken();
        AuthData createToken = new AuthData(authToken, username);
        auth.add(createToken);
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public int port() {
        return Spark.port();
    }
}
