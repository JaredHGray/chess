import chess.*;
import com.google.gson.Gson;
import ui.ChessClient;

import java.io.*;
import java.net.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // Specify the desired endpoint
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ChessClient(serverUrl).run();
    }
}


//connect to serverFacade and pass it a url