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

        if(newGame.gameName() == null || newGame.gameName().isEmpty() || authID == null || authID.isEmpty()){
            result.put("message", "Error: bad request");
            Results badResult = new Results(result);
            result.put("code", 400);
            result.put("data", badResult.getData());
        } else if(username != null){
            gameDAO.createGame(newGame, gameID);
            result.put("gameID", gameID);
            Results successResult = new Results(result);
            result.put("code", 200);
            result.put("data", successResult.getData());
        } else {
            result.put("message", "Error: unauthorized");
            Results badResult = new Results(result);
            result.put("code", 401);
            result.put("data", badResult.getData());
        }
        return result;
    }

    public Map<String, Object> listGames(String authToken) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(authDAO.getAuth(authToken) != null){
            result.put("games", gameDAO.listGames());
            Results successResult = new Results(result);
            result.put("code", 200);
            result.put("data", successResult.getData());
        } else {
            result.put("message", "Error: unauthorized");
            Results badResult = new Results(result);
            result.put("code", 401);
            result.put("data", badResult.getData());
        }
        return result;
    }
    private int generateID(){
        return new Random().nextInt(10000);
    }
}
