package service;

import com.google.gson.JsonObject;

import java.util.Map;


public class Results {

    private final JsonObject data;

    public Results(Map<String, Object> createResults) {
        this.data = new JsonObject();
        for (Map.Entry<String, Object> entry : createResults.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                this.data.addProperty(key, (String) value);
            } else if (value instanceof Integer) {
                this.data.addProperty(key, (Integer) value);
            }
        }
    }

    public JsonObject getData() {
        return data;
    }
}

