package dataAccess;
import model.AuthData;

public interface AuthDAO {

    void createAuth(String username) throws DataAccessException;
}
