package server;

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
    private final UserDAO userDAO = null;

    private final GameDAO gameDAO = null;

    public Server() {
        this.userService = new UserService(userDAO);
        this.gameService = new GameService(gameDAO);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
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
//        //get data from the response body
//        String username = req.queryParams("username");
//        String password = req.queryParams("password");
//        String email = req.queryParams("email");
//
//        //check if the user already exists
//        if(/**getUser function*/){
//            res.status(403);
//            return "{\"message\": \"Error: already taken\"}";
//        }
//        //check if valid request
//        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
//            res.status(400);
//            return "{\"message\": \"Error: bad request\"}"; //try in service
//        }
//        try{
//            //create the new user
//            /**createUser function*/
//            //register a new auth token with user
//            String authToken = generateToken();
//            /**createAuth function*/
//            //return success request
//            res.status(200);
//            res.type("\"application/json\"");
//            return "{\"username\":\"" + username + "\", \"authToken\":\"" + authToken + "\"}";
//        } catch (Exception e) {
//            // Return a failure response with status code 500 for unexpected errors
//            res.status(500);
//            res.type("application/json");
//            return "{\"message\": \"Error: description\"}";
//        }

        var registerUser = new Gson().fromJson(req.body(), UserData.class);
        registerUser = userService.addUser(registerUser);
        return new Gson().toJson(registerUser);
    }

    private String loginUser(Request req, Response res){
        //get data from the response body
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        //check if the user already exists
//        if(/**getUser function*/){ //check for matching password too
//            res.status(401);
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
        try{
            //register a new auth token with user
            String authToken = generateToken();
            /**createAuth function*/
            //return success request
            res.status(200);
            res.type("\"application/json\"");
            return "{\"username\":\"" + username + "\", \"authToken\":\"" + authToken + "\"}";
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
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

    private String makeGame(Request req, Response res){
        //get data from the response body
        String authToken = req.headers("authorization");
        String gameName = req.queryParams("gameName");

//        if(/**validToken*/){
//            // Return a failure response with status code 401 for unauthorized
//            res.status(401);
//            res.type("application/json");
//            return "{\"message\": \"Error: unauthorized\"}";
//        }
        try{
            /**makeGame function*/
            //return success request
            res.status(200);
            return "{\"gameID\": \"number\"}";
        } catch (Exception e) {
            // Return a failure response with status code 500 for unexpected errors
            res.status(500);
            res.type("application/json");
            return "{\"message\": \"Error: description\"}";
        }
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
        return null;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
