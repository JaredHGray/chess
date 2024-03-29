package serviceTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import service.GameService;
import service.UserService;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class gameServiceTests {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);

    @Test
    @Order(1)
    @DisplayName("Create Game")
    public void createGame() throws TestException, DataAccessException {
        var newGame = new GameData(0, null, null, "newGame", null);
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());

        var result = gameService.createGame(newGame, userAuth);
        Assertions.assertEquals(200, result.get("code"));
    }

    @Test
    @Order(2)
    @DisplayName("Create Game with Bad Authentication")
    public void badAuthCreate() throws TestException, DataAccessException {
        var newGame = new GameData(0, null, null, "newGame", null);
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);

        var result = gameService.createGame(newGame, "123-456");
        Assertions.assertEquals(401, result.get("code"));
    }

    @Test
    @Order(3)
    @DisplayName("list all games")
    public void listGames() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());

        gameService.createGame(new GameData(0, null, null, "newGame", null), userAuth);
        gameService.createGame(new GameData(0, null, null, "newGame2", null), userAuth);
        gameService.createGame(new GameData(0, null, null, "newGame3", null), userAuth);

        var actual = gameService.listGames(userAuth);
        Assertions.assertEquals(200, actual.get("code"));
    }

    @Test
    @Order(4)
    @DisplayName("list games with bad authentication")
    public void listBadAuth() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());

        gameService.createGame(new GameData(0, null, null, "newGame", null), userAuth);
        gameService.createGame(new GameData(0, null, null, "newGame2", null), userAuth);
        gameService.createGame(new GameData(0, null, null, "newGame3", null), userAuth);

        var actual = gameService.listGames("123-456");
        Assertions.assertEquals(401, actual.get("code"));
    }

    @Test
    @Order(5)
    @DisplayName("join game")
    public void joinGame() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());
        var newGame = new GameData(0, null, null, "newGame", null);
        var gameID =  gameService.createGame(newGame, userAuth);
        var playerColor = "BLACK";
        var actual = gameService.joinGame((Integer) gameID.get("gameID"), playerColor, userAuth);
        Assertions.assertEquals(200, actual.get("code"));
    }

    @Test
    @Order(6)
    @DisplayName("join bad team color")
    public void badColorJoin() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());
        var newGame = new GameData(0, null, null, "newGame", null);
        var gameID =  gameService.createGame(newGame, userAuth);
        var playerColor = "BLACK";
        gameService.joinGame((Integer) gameID.get("gameID"), playerColor, userAuth);
        var actual = gameService.joinGame((Integer) gameID.get("gameID"), playerColor, userAuth);
        Assertions.assertEquals(403, actual.get("code"));
    }

    @Test
    @Order(7)
    @DisplayName("Clear Games")
    public void clearUser() throws TestException, DataAccessException {
        var newGame = new GameData(0, null, null, "newGame", null);
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());

        gameService.createGame(newGame, userAuth);

        gameService.clearGames();
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }
}
