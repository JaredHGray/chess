package service;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.GameData;
import model.AuthData;
import dataAccess.GameDAO;

public class GameService {

    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){

        this.gameDAO = gameDAO;
    }

}
