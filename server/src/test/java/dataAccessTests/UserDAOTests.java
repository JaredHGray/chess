package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;

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
    @Test
    @Order(3)
    @DisplayName("Find User")
    public void findUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(findUser, "User is not found in the database");
        Assertions.assertEquals(newUser.username(), findUser.username(), "Wrong username found");
    }

    @Test
    @Order(4)
    @DisplayName("Find Non-Existent User")
    public void findBadUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var findUser = userDAO.getUser(newUser);
        Assertions.assertNotNull(findUser);
        var newUserLogin = new UserData("fakeUser", "123abc", "apple@gmail.com");
        var findResult = userDAO.getUser(newUserLogin);
        Assertions.assertNull(findResult, "This user should not exist");
    }

    @Test
    @Order(5)
    @DisplayName("Login User")
    public void loginUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var findUser = userDAO.verifyUser(newUser);
        Assertions.assertNotNull(findUser, "User is not found in the database");
        Assertions.assertEquals(newUser.username(), findUser.username(), "Wrong username found");
        Assertions.assertEquals(newUser.password(), findUser.password(), "Passwords do not match");
    }

    @Test
    @Order(6)
    @DisplayName("Invalid Login")
    public void invalidLogin() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        var findUser = userDAO.verifyUser(newUser);
        Assertions.assertNotNull(findUser, "User is not found in the database");

        var newUserLogin = new UserData("newUser", "123abc", "apple@gmail.com");
        var findResult = userDAO.verifyUser(newUserLogin);
        Assertions.assertNull(findResult, "Passwords do not match");
    }

    @Test
    @Order(7)
    @DisplayName("Clear Users")
    public void clearUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        userDAO.clearUsers();
        Assertions.assertNull(userDAO.getUser(newUser));
    }
}
