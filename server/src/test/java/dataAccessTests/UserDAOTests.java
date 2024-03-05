package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;
import service.GameService;
import service.UserService;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTests {

    public UserDAOTests() throws DataAccessException {
    }

    private final UserDAO userDAO = new SQLUserDAO();

    @Test
    @Order(1)
    @DisplayName("Normal User Registration")
    public void addUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var fetchedUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(fetchedUser, "User should exist in the database");
        Assertions.assertEquals(newUser.username(), fetchedUser.username());
    }

    @Test
    @Order(2)
    @DisplayName("Re-Register User")
    public void addUserTwice() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var newUserAgain = userDAO.addUser(newUser);
        Assertions.assertFalse(newUserAgain, "User should not exist in the database");
    }

//    @Order(3)
//    @DisplayName("Login User")
//    public void loginUser() throws TestException, DataAccessException {
//        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
//        userService.addUser(newUser);
//        var findUser = userDAO.getUser(newUser);
//        Assertions.assertNotNull(findUser);
//        Assertions.assertEquals(newUser.username(), findUser.username());
//        var newUserLogin = new UserData("newUser", "abc123", null);
//        var loginResult = userService.loginUser(newUserLogin);
//        Assertions.assertEquals(200, loginResult.get("code"));
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("Invalid Login")
//    public void invalidLogin() throws TestException, DataAccessException {
//        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
//        userService.addUser(newUser);
//        var findUser = userDAO.getUser(newUser);
//        Assertions.assertNotNull(findUser);
//        Assertions.assertEquals(newUser.username(), findUser.username());
//        var newUserLogin = new UserData("newUser", "123abc", null);
//        var loginResult = userService.loginUser(newUserLogin);
//        Assertions.assertEquals(401, loginResult.get("code"));
//    }

//    @Test
//    @Order(7)
//    @DisplayName("Clear Users")
//    public void clearUsers() throws TestException, DataAccessException {
//        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
//        userService.addUser(newUser);
//        userService.clearUsers();
//        Assertions.assertNull(userDAO.getUser(newUser));
//    }
}
