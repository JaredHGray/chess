package service;
import dataAccess.DataAccessException;
import model.GameData;

public class GameService {
    private final DataAccessException dataAccess;

    public GameService(DataAccessException dataAccess) {
        this.dataAccess = dataAccess;
    }
}
