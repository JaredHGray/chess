package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class Results {

    private final int responseCode;
    private final String errorMessage;
    private final JsonObject data;

    public Results(int responseCode, String errorMessage, String username, String authToken) {
        this.responseCode = responseCode;
        this.errorMessage = errorMessage;

        this.data = new JsonObject();
        if (responseCode == 200) {
            this.data.addProperty("username", username);
            this.data.addProperty("authToken", authToken);
        } else {
            this.data.addProperty("message", errorMessage);
        }
    }

    public JsonObject getData() {
        return data;
    }

    public String toJson() {
        return data.toString();
    }
}
