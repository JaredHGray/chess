package service;

import com.google.gson.JsonObject;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public Map<String, Object> addUser(UserData registerUser) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(registerUser.username() == null || registerUser.username().isEmpty() || registerUser.password() == null || registerUser.password().isEmpty() || registerUser.email() == null || registerUser.email().isEmpty()){
            Results badResult = new Results(400, "Error: bad request", null, null);
            result.put("code", badResult.getResponseCode());
            result.put("data", badResult.getData());
        }else if(userDAO.getUser(registerUser) == null){
            userDAO.addUser(registerUser);
            String authToken = generateToken();
            authDAO.createAuth(registerUser.username(), authToken);
            Results successResult = new Results(200, null, registerUser.username(), authToken);
            result.put("code", successResult.getResponseCode());
            result.put("data", successResult.getData());
        } else {
            Results badResult = new Results(403, "Error: already taken", null, null);
            result.put("code", badResult.getResponseCode());
            result.put("data", badResult.getData());
        }
        return result;
    }

    public Map<String, Object> loginUser(UserData user) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(userDAO.verifyUser(user) != null){
            String authToken = generateToken();
            authDAO.createAuth(user.username(), authToken);
            Results successResult = new Results(200, null, user.username(), authToken);
            result.put("code", successResult.getResponseCode());
            result.put("data", successResult.getData());
        } else {
            Results badResult = new Results(401, "Error: unauthorized", null, null);
            result.put("code", badResult.getResponseCode());
            result.put("data", badResult.getData());
        }
        return result;
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }


}
