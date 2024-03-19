package clientTests;

import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

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
    void clearDatabases() throws DataAccessException {
        facade.clearDatabase();
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
}
