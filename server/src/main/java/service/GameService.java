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
import java.util.Random;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){

        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Map<String, Object> createGame(GameData newGame, String authID) throws DataAccessException {
        Map<String, Object> result = new HashMap<>();
        String username = authDAO.getAuth(authID);
        int gameID = generateID();
        if(username != null){
            gameDAO.createGame(newGame, username, gameID);
            result.put("gameID", gameID);
            Results successResult = new Results(result);
            result.put("code", 200);
            result.put("data", successResult.getData());
        }
        return result;
    }

    private int generateID(){
        return new Random().nextInt(10000);
    }
}
