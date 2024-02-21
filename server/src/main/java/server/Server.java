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

    private String logoutUser(Request req, Response res){
        //get data from the response body
        String authToken = req.headers("authorization");

//        if(/**validToken*/){
//            // Return a failure response with status code 401 for unauthorized
//            res.status(401);
//            res.type("application/json");
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
        try{
            /**logout function*/
            //return success request
            res.status(200);
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
        return null;
    }

    private String listGames(Request req, Response res){
        //get data from the response body
        String authToken = req.headers("authorization");

//        if(/**validToken*/){
//            // Return a failure response with status code 401 for unauthorized
//            res.status(401);
//            res.type("application/json");
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
        try{
            /**gameData[] function*/
            //return success request
            res.status(200);
            return "{\"games\": \"gameData\"}";
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
    }

    private String makeGame(Request req, Response res) throws DataAccessException {
        var newGame = new Gson().fromJson(req.body(), GameData.class);
        String authID = req.headers("authorization");
        var createGame = gameService.createGame(newGame, authID);
        res.status((Integer) createGame.get("code"));
        return new Gson().toJson((JsonObject) createGame.get("data"));

//        if(/**validToken*/){
//            // Return a failure response with status code 401 for unauthorized
//            res.status(401);
//            res.type("application/json");
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
//        try{
//            /**makeGame function*/
//            //return success request
//            res.status(200);
//            return "{\"gameID\": \"number\"}";
//        } catch (Exception e) {
//            // Return a failure response with status code 500 for unexpected errors
//            res.status(500);
//            res.type("application/json");
//            return "{\"message\": \"Error: description\"}";
//        }
    }

    private String joinGame(Request req, Response res){
        //get data from the response body
        String authToken = req.headers("authorization");
        String gameID = req.queryParams("gameID");
        String playerColor = req.queryParams("playerColor");

//        if(/**validToken*/){
//            // Return a failure response with status code 401 for unauthorized
//            res.status(401);
//            res.type("application/json");
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
        //check if valid request
        if (gameID == null || gameID.isEmpty() || playerColor == null || playerColor.isEmpty()) {
            res.status(400);
            return "{\"message\": \"Error: bad request\"}";
        }
        try{
            /**joinGame function*/
            res.status(200);
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
        return null;
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
