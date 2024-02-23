package service;

import com.google.gson.JsonObject;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;

import javax.xml.transform.Result;
import java.lang.reflect.Method;
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
            result.put("message", "Error: bad request");
            Results badResult = new Results(result);
            result.put("code", 400);
            result.put("data", badResult.getData());
        }else if(userDAO.getUser(registerUser) == null){
            userDAO.addUser(registerUser);
            generateAuth(registerUser, result);
        } else {
            result.put("message", "Error: already taken");
            Results badResult = new Results(result);
            result.put("code", 403);
            result.put("data", badResult.getData());
        }
        return result;
    }

    public Map<String, Object> loginUser(UserData user) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(userDAO.verifyUser(user) != null){
            generateAuth(user, result);
        } else {
            unauthorizedAccess(result);
        }
        return result;
    }

    public Map<String, Object> logoutUser(String authToken) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(authDAO.deleteAuth(authToken)){
            result.put("", "");
            Results successResult = new Results(result);
            result.put("code", 200);
            result.put("data", successResult.getData());
        }else{
              unauthorizedAccess(result);
        }
        return result;
    }

    public void clearUsers() throws DataAccessException{
        userDAO.clearUsers();
    }

    public void clearAuth() throws DataAccessException{
        authDAO.clearAuth();
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    private void generateAuth(UserData user, Map<String, Object> result) throws DataAccessException {
        String authToken = generateToken();
        authDAO.createAuth(user.username(), authToken);
        result.put("username", user.username());
        result.put("authToken", authToken);
        Results successResult = new Results(result);
        result.put("code", 200);
        result.put("data", successResult.getData());
    }

    private void unauthorizedAccess(Map<String, Object> result){
        result.put("message", "Error: unauthorized");
        Results badResult = new Results(result);
        result.put("code", 401);
        result.put("data", badResult.getData());
    }
//base service try to make methods for each version of 200, 400, 403, 401
}
