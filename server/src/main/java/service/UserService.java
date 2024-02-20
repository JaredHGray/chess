package service;

import com.google.gson.JsonObject;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public JsonObject addUser(UserData registerUser) throws DataAccessException{
        if(userDAO.getUser(registerUser) == null){
            userDAO.addUser(registerUser);
            String authToken = generateToken();
            authDAO.createAuth(registerUser.username(), authToken);
            String data = "{username: " + registerUser.username() + ", authToken: " + authToken + "}";
            Results successResult = new Results(200, null, registerUser.username(), authToken);
            return successResult.getData();
        } else {
            Results badResult = new Results(403, "Error: already taken", null, null);
            return badResult.getData();
        }
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }


}
