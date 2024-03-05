package dataAccessTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;

import java.util.Random;

public class GameDAOTests {

    public GameDAOTests() throws DataAccessException {
    }
    private final GameDAO gameDAO = new SQLGameDAO();

    @Test
    @Order(1)
    @DisplayName("Create Game")
    public void createGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        var createGame = gameDAO.createGame(newGame, gameID);
        Assertions.assertTrue(createGame, "should have created the game");
    }

    @Test
    @Order(2)
    @DisplayName("Create Repeat Game")
    public void createGameTwice() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);
        var createGame = gameDAO.createGame(newGame, gameID);
        Assertions.assertFalse(createGame, "should not have created the game");
    }

    @Test
    @Order(3)
    @DisplayName("List Games")
    public void listGames() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);
        gameDAO.createGame(newGame, gameID);

        gameID = new Random().nextInt(10000);
        newGame = new GameData(gameID, null, null, "newGame2", null);
        gameDAO.createGame(newGame, gameID);
        gameDAO.createGame(newGame, gameID);
        var listGames = gameDAO.listGames();
        Assertions.assertNotNull(listGames, "missing games in database");
    }

    @Test
    @Order(4)
    @DisplayName("List Games Error")
    public void listBadGames() throws TestException, DataAccessException {
        gameDAO.clearGames();
        var listGames = gameDAO.listGames();
        Assertions.assertTrue(listGames.isEmpty(), "no games should be in database");
    }

    @Test
    @Order(5)
    @DisplayName("Find Game")
    public void findGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);

        var findGame = gameDAO.findGame(gameID);
        Assertions.assertEquals(findGame.gameID(), gameID, "could not find game in database");
    }

    @Test
    @Order(6)
    @DisplayName("Find Bad Game")
    public void findBadGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);

        var findGame = gameDAO.findGame(10);
        Assertions.assertNull(findGame, "should not have returned anything on query");
    }

    @Test
    @Order(7)
    @DisplayName("Join Game")
    public void joinGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);

        var joinGame = gameDAO.joinGame(gameID, "joinUser", "WHITE");
        Assertions.assertTrue(joinGame, "game not joined successfully");
    }

    @Test
    @Order(8)
    @DisplayName("Join Bad Game")
    public void joinBadGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);

        var joinGame = gameDAO.joinGame(10, "joinUser", "WHITE");
        Assertions.assertFalse(joinGame, "game joined successfully");
    }

    @Test
    @Order(9)
    @DisplayName("Clear Games")
    public void clearGame() throws TestException, DataAccessException {
        gameDAO.clearGames();
        int gameID = new Random().nextInt(10000);
        var newGame = new GameData(gameID, null, null, "newGame", null);
        gameDAO.createGame(newGame, gameID);
        gameDAO.clearGames();
        Assertions.assertNull(gameDAO.findGame(gameID));
    }
}
