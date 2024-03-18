package service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.GameData;
import model.AuthData;
import dataAccess.GameDAO;

import java.util.*;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    Map<Integer, List<String>> gameObservers;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Map<String, Object> createGame(GameData newGame, String authID) throws DataAccessException {
        Map<String, Object> result = new HashMap<>();
        String username = authDAO.getAuth(authID);
        int gameID = generateID();

        if(newGame.gameName() == null || newGame.gameName().isEmpty() || authID == null || authID.isEmpty()){
            ResponseHelper.badRequest(result);
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
        gameObservers = new HashMap<>();
        GameData findGame = gameDAO.findGame(gameID);
        String user = authDAO.getAuth(authToken);

        if(gameID <= 0 || authToken == null || authToken.isEmpty()){
            ResponseHelper.badRequest(result);
        } else if(user != null){
            if(playerColor == null || playerColor.isEmpty()){
                List<String> player = new ArrayList<>();
                player.add(user);
                gameObservers.put(gameID, player);
                ResponseHelper.successResult(result);
            } else if(findGame != null){
                if((findGame.whiteUsername() != null && playerColor.equals("WHITE")) || (findGame.blackUsername() != null && playerColor.equals("BLACK"))){
                    ResponseHelper.takenError(result);
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
