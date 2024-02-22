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
    private final AuthDAO authDAO = new MemoryAuthData();
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


}
