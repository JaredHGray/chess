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
            ResponseHelper.unauthorizedAccess(result);
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
            ResponseHelper.unauthorizedAccess(result);
        }
        return result;
    }

    public Map<String, Object> joinGame(int gameID, String playerColor, String authToken) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        GameData findGame = gameDAO.findGame(gameID);
        String user = authDAO.getAuth(authToken);

        if(gameID <= 0 || authToken == null || authToken.isEmpty()){
            result.put("message", "Error: bad request");
            Results badResult = new Results(result);
            result.put("code", 400);
            result.put("data", badResult.getData());
        } else if(user != null){
            if(playerColor == null || playerColor.isEmpty()){
                ResponseHelper.successResult(result);
            } else if(findGame != null){
                if((findGame.whiteUsername() != null && playerColor.equals("WHITE")) || (findGame.blackUsername() != null && playerColor.equals("BLACK"))){
                    result.put("message", "Error: already taken");
                    Results badResult = new Results(result);
                    result.put("code", 403);
                    result.put("data", badResult.getData());
                } else{
                    gameDAO.joinGame(gameID, user, playerColor);
                    ResponseHelper.successResult(result);
                }
            }
        } else {
            ResponseHelper.unauthorizedAccess(result);
        }
        return result;
    }

    public void clearGames() throws DataAccessException{
        gameDAO.clearGames();
    }
    private int generateID(){
        return new Random().nextInt(10000);
    }
}
