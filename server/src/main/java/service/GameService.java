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

    public Object createGame(GameData newGame, String authID) throws DataAccessException {
        Map<String, Object> result = new HashMap<>();
        String username = authDAO.getAuth(authID);
        int gameID = generateID();
        if(username != null){
            gameDAO.creatGame(newGame, username, gameID);
           // Results successResult = new Results(200, null, gameID, null);
           // result.put("code", successResult.getResponseCode());
            //result.put("data", successResult.getData());
        }
        return null;
    }

    private int generateID(){
        return new Random().nextInt(10000);
    }
}
