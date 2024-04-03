import ui.ChessClient;

public class Main {
    public static void main(String[] args) throws Exception {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ChessClient(serverUrl).run();
    }
}

//use a lamda for notificationHandler - define a method and pass in a notificationHandler