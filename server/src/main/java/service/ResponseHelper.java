package service;

import dataAccess.DataAccessException;
import model.UserData;

import java.util.Map;

public class ResponseHelper {

    public static void generateAuth(UserData user, String authToken, Map<String, Object> result) throws DataAccessException {
        result.put("username", user.username());
        result.put("authToken", authToken);
        Results successResult = new Results(result);
        result.put("code", 200);
        result.put("data", successResult.getData());
    }

    public static void unauthorizedAccess(Map<String, Object> result) {
        result.put("message", "Error: unauthorized");
        Results badResult = new Results(result);
        result.put("code", 401);
        result.put("data", badResult.getData());
    }

    public static void successResult(Map<String, Object> result) {
        result.put("", "");
        Results successResult = new Results(result);
        result.put("code", 200);
        result.put("data", successResult.getData());
    }

}
