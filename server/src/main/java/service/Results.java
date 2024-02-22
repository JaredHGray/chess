package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.GameData;

import java.util.Map;
import java.util.Set;


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
            } else if (value instanceof Set<?>) {
                JsonArray gamesArray = new JsonArray();
                for (Object gameData : (Set<?>) value) {
                    if (gameData instanceof GameData) {
                        JsonObject gameDataJson = convertGameDataToJson((GameData) gameData);
                        gamesArray.add(gameDataJson);
                    }
                }
                this.data.add("games", gamesArray);
            }
        }
    }

    private JsonObject convertGameDataToJson(GameData gameData) {
        JsonObject gameDataJson = new JsonObject();
        gameDataJson.addProperty("gameID", gameData.gameID());
        gameDataJson.addProperty("whiteUsername", gameData.whiteUsername());
        gameDataJson.addProperty("blackUsername", gameData.blackUsername());
        gameDataJson.addProperty("gameName", gameData.gameName());

        return gameDataJson;
    }

    public JsonObject getData() {
        return data;
    }
}

