package server;

import spark.*;
import java.nio.file.Paths;
import com.google.gson.Gson;
import java.util.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        var webDir = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "web");
        Spark.externalStaticFileLocation(webDir.toString());

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void createRoutes(){
        Spark.post("/user", (req, res) -> registerUser(req, res));

    }

    private String registerUser(Request req, Response res){
        //get data from the response body
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String email = req.queryParams("email");

        //check if the user already exists
        if(/**getUser function*/){
            res.status(403);
            return "{\"message\": \"Error: already taken\"}";
        }
        //check if valid request
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
            res.status(400);
            return "{\"message\": \"Error: bad request\"}";
        }
        //create the new user
        /**createUser function*/
        //register a new auth token with user
        String authToken = generateToken();
        /**createAuth function*/
        //return success request
        res.status(200);
        res.type("\"application/json\"");
        return "{\"username\":\"" + username + "\", \"authToken\":\"" + authToken + "\"}";
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
