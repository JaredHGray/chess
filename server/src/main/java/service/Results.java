package service;

import com.google.gson.JsonObject;

import java.util.Map;


public class Results {

//    private final int responseCode;
//    private final String errorMessage;
    private final JsonObject data;

//    public Results(int responseCode, String errorMessage, String username, String authToken) {
//        this.responseCode = responseCode;
//        this.errorMessage = errorMessage;
//
//        this.data = new JsonObject();
//        if (responseCode == 200) {
//            this.data.addProperty("username", username);
//            this.data.addProperty("authToken", authToken);
//        } else {
//            this.data.addProperty("message", errorMessage);
//        }
//    }

    public Results(Map<String, Object> createResults) {
        this.data = new JsonObject();
        for (Map.Entry<String, Object> entry : createResults.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            this.data.addProperty(key, (String) value);
        }
    }

    public JsonObject getData() {
        return data;
    }

//    public int getResponseCode() {
//        return responseCode;
//    }
    public String toJson() {
        return data.toString();
    }
}

