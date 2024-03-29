package clientTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Objects;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private final UserDAO userDAO = new SQLUserDAO();
    private final GameDAO gameDAO = new SQLGameDAO();
    private final AuthDAO authDAO = new SQLAuthDAO();

    public ServerFacadeTests() throws DataAccessException {
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabases() throws Exception {
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuth();
    }

    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerError() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.registerUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.registerUser(user);
        });
    }

    @Test
    void login() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.registerUser(user);
        user = new UserData("player1", "password", null);
        var authData = facade.loginUser(user);
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginError() throws Exception {
        UserData user = new UserData("player1", "password", null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.loginUser(user);
        });
    }

    @Test
    void logout() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        facade.logoutUser(authData.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.listGames(authData.authToken());
        });
    }

    @Test
    void logoutError() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.registerUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.logoutUser("1234-abc");
        });
    }

    @Test
    void listGames() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, "TestGame", null);
        facade.makeGame(newGame, authData.authToken());
        Assertions.assertNotNull(facade.listGames(authData.authToken()));
    }

    @Test
    void listGamesError() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, "TestGame", null);
        facade.makeGame(newGame, authData.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.listGames("1234-abc");
        });
    }

    @Test
    void createGame() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, "TestGame", null);
        var game = facade.makeGame(newGame, authData.authToken());
        Assertions.assertTrue(game.gameID() > 0);
    }

    @Test
    void createGameError() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, null, null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.makeGame(newGame, authData.authToken());
        });
    }

    @Test
    void joinGame() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, "TestGame", null);
        var game = facade.makeGame(newGame, authData.authToken());
        facade.joinGame(game.gameID(), "WHITE", authData.authToken());
        for(GameData search: facade.listGames(authData.authToken())){
            Assertions.assertTrue(search.gameID() == game.gameID() && Objects.equals(search.whiteUsername(), authData.username()));
        }
    }

    @Test
    void joinGameError() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.registerUser(user);
        GameData newGame = new GameData(0, null, null, "TestGame", null);
        var game = facade.makeGame(newGame, authData.authToken());
        facade.joinGame(game.gameID(), "WHITE", authData.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.joinGame(game.gameID(), "WHITE", authData.authToken());
        });
    }
}
