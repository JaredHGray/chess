package service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.GameData;
import model.AuthData;
import dataAccess.GameDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){

        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object createGame(GameData newGame, String authID) throws DataAccessException {
        Map<String, Object> result = new HashMap<>();
        if(authDAO.getAuth(authID)){

        }
        return null;
    }
}
