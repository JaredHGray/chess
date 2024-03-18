package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
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

    public AuthData registerUser(UserData user) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void loginUser(UserData user) throws DataAccessException {
        var path = "/session";
        this.makeRequest("POST", path, user, UserData.class, null);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData[] listGames(String authToken) throws DataAccessException {
        var path = "/game";
        record listGameResponse(GameData[] game) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class, authToken);
        return response.game();
    }

    public GameData makeGame(GameData game, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class, authToken);
    }

    public void joinGame(GameData game, String authToken) throws DataAccessException {
        var path = "/game";
        this.makeRequest("PUT", path, game, GameData.class, authToken);
    }

    public void clearDatabase() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE",path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String header) throws DataAccessException {
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
            throw new DataAccessException(ex.getMessage());
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
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
            throw new DataAccessException(errorMessage);
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
