package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import service.GameService;
import service.UserService;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class userServiceTests {

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthData();
    private final GameDAO gameDAO = new MemoryGameDAO();

    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);

    @Test
    @Order(1)
    @DisplayName("Normal User Registration")
    public void registerUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        var result = userService.addUser(newUser);
        Assertions.assertEquals(200, result.get("code"));
    }

    @Test
    @Order(2)
    @DisplayName("Re-Register User")
    public void registerTwice() throws TestException, DataAccessException {
        var originalUser = new UserData("newUser", "abc123", "nu@gmail.com");
        var newUser = new UserData("newUser", "123abc", "un@gmail.com");
        userService.addUser(originalUser);
        var findUser = userDAO.getUser(originalUser);
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(newUser.username(), findUser.username());
        var result = userService.addUser(newUser);
        Assertions.assertEquals(403, result.get("code"));
    }

    @Test
    @Order(3)
    @DisplayName("Login User")
    public void loginUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(newUser.username(), findUser.username());
        var newUserLogin = new UserData("newUser", "abc123", null);
        var loginResult = userService.loginUser(newUserLogin);
        Assertions.assertEquals(200, loginResult.get("code"));
    }

    @Test
    @Order(4)
    @DisplayName("Invalid Login")
    public void invalidLogin() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(newUser.username(), findUser.username());
        var newUserLogin = new UserData("newUser", "123abc", null);
        var loginResult = userService.loginUser(newUserLogin);
        Assertions.assertEquals(401, loginResult.get("code"));
    }

    @Test
    @Order(5)
    @DisplayName("Logout User")
    public void logoutUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(newUser.username(), findUser.username());
        var userAuth = authDAO.getUser(newUser.username());
        var logoutResult = userService.logoutUser(userAuth);
        Assertions.assertEquals(200, logoutResult.get("code"));
    }

    @Test
    @Order(6)
    @DisplayName("Invalid Logout Attempt")
    public void invalidLogout() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        // Assert that the user is not null, indicating successful registration
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(newUser.username(), findUser.username());
        var userAuth = authDAO.getUser(newUser.username());
        userService.logoutUser(userAuth);
        var logoutResult = userService.logoutUser(userAuth);
        Assertions.assertEquals(401, logoutResult.get("code"));
    }
    @Test
    @Order(7)
    @DisplayName("Clear Users")
    public void clearUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        userService.clearUsers();
        Assertions.assertNull(userDAO.getUser(newUser));
    }

    @Test
    @Order(8)
    @DisplayName("Clear Auth")
    public void clearAuth() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userService.addUser(newUser);
        var userAuth = authDAO.getUser(newUser.username());
        userService.clearAuth();
        Assertions.assertNull(authDAO.getAuth(userAuth));
    }
}
