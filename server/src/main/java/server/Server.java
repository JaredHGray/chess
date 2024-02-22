package server;

import com.google.gson.JsonObject;
import org.eclipse.jetty.server.Authentication;
import spark.*;
import java.nio.file.Paths;
import com.google.gson.Gson;
import java.util.*;
import service.UserService;
import service.GameService;
import dataAccess.*;
import model.*;

public class Server {

    private UserService userService = null;
    private GameService gameService = null;
    private final UserDAO userDAO = new MemoryUserDAO(); //memory only for memory\
    private final AuthDAO authDAO = new MemoryAuthData();
    private final GameDAO gameDAO = new MemoryGameDAO();

    public Server() {
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        // Register endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void createRoutes(){
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::makeGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearDatabase);
    }

    private String registerUser(Request req, Response res) throws DataAccessException{
        var registerUser = new Gson().fromJson(req.body(), UserData.class);
        var newUser = userService.addUser(registerUser); //also return status code
        res.status((Integer) newUser.get("code"));
        return new Gson().toJson((JsonObject) newUser.get("data"));
    }

    private String loginUser(Request req, Response res) throws DataAccessException {
        var login = new Gson().fromJson(req.body(), UserData.class);
        var loginUser = userService.loginUser(login);
        res.status((Integer) loginUser.get("code"));
        return new Gson().toJson((JsonObject) loginUser.get("data"));
    }

    private String logoutUser(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("authorization");
        var logoutUser = userService.logoutUser(authToken);
        res.status((Integer) logoutUser.get("code"));
        return new Gson().toJson((JsonObject) logoutUser.get("data"));
    }

    private String listGames(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("authorization");
        var allGames = gameService.listGames(authToken);
        res.status((Integer) allGames.get("code"));
        return new Gson().toJson((JsonObject) allGames.get("data"));
    }

    private String makeGame(Request req, Response res) throws DataAccessException {
        var newGame = new Gson().fromJson(req.body(), GameData.class);
        String authID = req.headers("authorization");
        var createGame = gameService.createGame(newGame, authID);
        res.status((Integer) createGame.get("code"));
        return new Gson().toJson((JsonObject) createGame.get("data"));
    }

    private String joinGame(Request req, Response res) throws DataAccessException{
        var joinGame = new Gson().fromJson(req.body(), JsonObject.class);
        String authToken = req.headers("authorization");
        String playerColor = joinGame.getAsJsonPrimitive("playerColor").getAsString();
        int gameID = joinGame.getAsJsonPrimitive("gameID").getAsInt();
        var findGame = gameService.joinGame(gameID, playerColor, authToken);
        res.status((Integer) findGame.get("code"));
        return new Gson().toJson((JsonObject) findGame.get("data"));
    }

    private String clearDatabase(Request req, Response res){
        try{
            /**celar database functon*/
            res.status(200);
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
        return "{\"message\": \"Error: description\"}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
