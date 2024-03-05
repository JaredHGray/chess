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
    private final UserDAO userDAO = new SQLUserDAO();

    @Test
    @Order(1)
    @DisplayName("Create AuthToken")
    public void addUser() throws TestException, DataAccessException {
        var newUser = new UserData("newUser", "abc123", "nu@gmail.com");
        userDAO.addUser(newUser);
        String authToken = UUID.randomUUID().toString();
        var newAuth = authDAO.createAuth(newUser.username(), authToken);
        Assertions.assertTrue(newAuth, "User already has token");
    }

}
