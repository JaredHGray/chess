package server;

import com.google.gson.JsonObject;
import server.websockets.WebSocketHandler;
import spark.*;
import com.google.gson.Gson;
import service.UserService;
import service.GameService;
import dataAccess.*;
import model.*;

import java.util.Map;

public class Server {

    private final UserService userService;
    private final GameService gameService;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        try{
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", webSocketHandler);
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
        String playerColor = null;
        if (joinGame.has("playerColor")) { // Check if playerColor exists in the request
            playerColor = joinGame.getAsJsonPrimitive("playerColor").getAsString();
        }
        int gameID = joinGame.getAsJsonPrimitive("gameID").getAsInt();
        var findGame = gameService.joinGame(gameID, playerColor, authToken);
        res.status((Integer) findGame.get("code"));
        //webSocketHandler.joinGameMessage((String) findGame.get("user"), playerColor, authToken, gameID);
        return new Gson().toJson((JsonObject) findGame.get("data"));
    }

    private String clearDatabase(Request req, Response res) throws DataAccessException{
        gameService.clearGames();
        userService.clearUsers();
        userService.clearAuth();
        res.status(200);
        return new Gson().toJson(new JsonObject());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
