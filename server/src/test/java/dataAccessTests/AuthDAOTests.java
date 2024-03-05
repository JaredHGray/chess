package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;

import java.util.UUID;

public class AuthDAOTests {

    public AuthDAOTests() throws DataAccessException {
    }
    private final AuthDAO authDAO = new SQLAuthDAO();

    @Test
    @Order(1)
    @DisplayName("Create AuthToken")
    public void newAuth() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(newUser.username(), authToken);
        var newAuth = authDAO.getAuth(authToken);
        Assertions.assertNotNull(newAuth, "User already has token");
    }

    @Test
    @Order(2)
    @DisplayName("AuthToken Already Exists")
    public void badAuth() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");

        newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertFalse(newAuth, "Should Not have generated new token");
    }

    @Test
    @Order(3)
    @DisplayName("Get User from AuthToken")
    public void getUser() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        var checkUser = authDAO.getAuth(authToken);
        Assertions.assertEquals(newUser.username(), checkUser);
    }

    @Test
    @Order(4)
    @DisplayName("Get User from Bad AuthToken")
    public void getBadUser() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        authToken = UUID.randomUUID().toString();
        var checkUser = authDAO.getAuth(authToken);
        Assertions.assertNotEquals(newUser.username(), checkUser, "No user should have been returned");
    }

    @Test
    @Order(5)
    @DisplayName("Delete AuthToken")
    public void deleteAuth() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");

        var checkUser = authDAO.deleteAuth(authToken);
        Assertions.assertTrue(checkUser, "No user was deleted");
    }

    @Test
    @Order(6)
    @DisplayName("Delete Bad AuthToken")
    public void deleteBadAuth() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        authToken = UUID.randomUUID().toString();
        var checkUser = authDAO.deleteAuth(authToken);
        Assertions.assertFalse(checkUser, "No user should have been deleted");
    }

    @Test
    @Order(7)
    @DisplayName("Get AuthToken from User")
    public void getToken() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        var checkUser = authDAO.getUser(newUser.username());
        Assertions.assertEquals(authToken, checkUser);
    }

    @Test
    @Order(8)
    @DisplayName("Get AuthToken from Bad User")
    public void getBadToken() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();

        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        var checkUser = authDAO.getUser("Gary");
        Assertions.assertNotEquals(authToken, checkUser, "No authToken should have been returned");
    }

    @Test
    @Order(8)
    @DisplayName("Clear Auth")
    public void clearAuth() throws TestException, DataAccessException {
        authDAO.clearAuth();
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        String authToken = UUID.randomUUID().toString();
        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
        authDAO.clearAuth();
        Assertions.assertNull(authDAO.getUser(newUser.username()));
    }
}
