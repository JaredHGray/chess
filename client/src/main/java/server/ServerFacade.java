package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerUser(UserData user) {
        try {
            var path = "/user";
            return this.makeRequest("POST", path, user, AuthData.class, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthData loginUser(UserData user) {
        try {
            var path = "/session";
            return this.makeRequest("POST", path, user, AuthData.class, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logoutUser(String authToken) {
        try {
            var path = "/session";
            this.makeRequest("DELETE", path, null, null, authToken);
        } catch (Exception e) {e.printStackTrace();}
    }

    public GameData[] listGames(String authToken) {
        try {
            var path = "/game";
            record ListGamesResponse(GameData[] games) {
            }
            ListGamesResponse response = this.makeRequest("GET", path, null, ListGamesResponse.class, authToken);
            return response.games();
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }

    public GameData makeGame(GameData game, String authToken) {
        try {
            var path = "/game";
            return this.makeRequest("POST", path, game, GameData.class, authToken);
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }

    public void joinGame(int gameID, String playerColor, String authToken) {
        try {
            var path = "/game";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("gameID", gameID);
            requestBody.put("playerColor", playerColor);
            this.makeRequest("PUT", path, requestBody, GameData.class, authToken);
        } catch (Exception e) {
        e.printStackTrace();
        }
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String header) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            // Set authorization header
            if (header != null && !header.isEmpty()) {
                http.setRequestProperty("authorization", header);
            }
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        int statusCode = http.getResponseCode();
        if (!isSuccessful(statusCode)) {
            String errorMessage = String.valueOf(statusCode);
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(http.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                JsonObject errorJson = new Gson().fromJson(errorResponse.toString(), JsonObject.class);
                if (errorJson != null && errorJson.has("message")) {
                    errorMessage += "\n" + errorJson.get("message").getAsString() + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new RuntimeException(errorMessage);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
