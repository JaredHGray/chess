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
            ResponseHelper.badRequest(result);
        }else if(userDAO.getUser(registerUser) == null){
            userDAO.addUser(registerUser);
            String authToken = generateToken();
            authDAO.createAuth(registerUser.username(), authToken);
            ResponseHelper.generateAuth(registerUser, authToken, result);
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
            String authToken = generateToken();
            authDAO.createAuth(user.username(), authToken);
            ResponseHelper.generateAuth(user, authToken, result);
        } else {
            ResponseHelper.unauthorizedAccess(result);
        }
        return result;
    }

    public Map<String, Object> logoutUser(String authToken) throws DataAccessException{
        Map<String, Object> result = new HashMap<>();
        if(authDAO.deleteAuth(authToken)){
            ResponseHelper.successResult(result);
        }else{
            ResponseHelper.unauthorizedAccess(result);
        }
        return result;
    }

    public void clearUsers() throws DataAccessException{
        userDAO.clearUsers();
    }

    public void clearAuth() throws DataAccessException{
        authDAO.clearAuth();
    }

    private String generateToken(){return UUID.randomUUID().toString();}
}
