package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChessClient {

//    private String visitorName = null;
//    private final ServerFacade server;
//    private final String serverUrl;
//
//    public ChessClient(ServerFacade server, String serverUrl) {
//        this.server = server;
//        this.serverUrl = serverUrl;
//    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        initialMenu(out);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        executeInitialChoice(choice, out);
        loginMenu(out);
        choice = scanner.nextInt();
        executeGameChoice(choice, out);
    }

    private static void initialMenu(PrintStream out) {
        out.println("Welcome to ♕ 240 Chess Server ♕");
        out.println("Enter your selection");
        out.println("1. Help");
        out.println("2. Quit");
        out.println("3. Login");
        out.println("4. Register");
        out.print("Enter choice: ");
    }

    private static void executeInitialChoice(int choice, PrintStream out) {
        switch (choice) {
            case 1:
                initialHelp(out);
                break;
            case 2:
                quit(out);
                break;
            case 3:
                login(out);
                break;
            case 4:
                register(out);
                break;
            default:
                out.println("Invalid choice");
        }
    }

    private static void loginMenu(PrintStream out){
        out.println("Enter your selection");
        out.println("1. Help");
        out.println("2. Logout");
        out.println("3. Create Game");
        out.println("4. List Games");
        out.println("5. Join Game");
        out.println("6. Observe Game");
        out.print("Enter choice: ");
    }

    private static void executeGameChoice(int choice, PrintStream out) {
        switch (choice) {
            case 1:
                userHelp(out);
                break;
            case 2:
                logout(out);
                break;
            case 3:
                createGame(out);
                break;
            case 4:
                listGames(out);
                break;
            case 5:
                joinGame(out);
                break;
            case 6:
                observeGame(out);
                break;
            default:
                out.println("Invalid choice");
        }
    }

    private static void observeGame(PrintStream out) {
        out.println("Observe Game option selected");
    }

    private static void joinGame(PrintStream out) {
        out.println("Join Game option selected");
    }

    private static void listGames(PrintStream out) {
        out.println("List Games option selected");
    }

    private static void createGame(PrintStream out) {
        out.println("Create Game option selected");
    }

    private static void logout(PrintStream out) {
        out.println("Logout option selected");
    }

    private static void userHelp(PrintStream out) {
        out.println("Help menu option selected");
        out.println("Help: You are lost and confused, in need of guidance on what to do");
        out.println("Logout: Sign out of the chess server and return to the initial menu");
        out.println("Create Game: Allows you to create a new chess game on the server");
        out.println("List Games: Lists all current games on the server");
        out.println("Join Game: Allows you to specify which chess game you want to join and what color you want to play");
        out.println("Observe Game: Allows you to specify which chess game you want to observe");
        out.println();
    }

    public static void initialHelp(PrintStream out) {
        out.println("Help menu option selected");
        out.println("Help: You are lost and confused, in need of guidance on what to do");
        out.println("Quit: No longer play the game of chess");
        out.println("Login: Sign in with username and password to access your account");
        out.println("Register: Sign up for a chess account with username, password, and email");
        out.println();
    }

    public static void quit(PrintStream out) {
        out.println("Quit menu option selected");
    }

    public static void login(PrintStream out) {
        out.println("Login menu option selected");
    }

    public static void register(PrintStream out) {
        out.println("Register menu option selected");
    }
}
