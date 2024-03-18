package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import dataAccess.DataAccessException;
import model.UserData;
import server.ServerFacade;

public class ChessClient {

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }

    public void run() throws DataAccessException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        Scanner scanner = new Scanner(System.in);
        int choice;
        out.println("Welcome to ♕ 240 Chess Server ♕");
        do {
            initialMenu(out);
            choice = scanner.nextInt();
            executeInitialChoice(choice, out);
        } while (choice != 2);
    }

    private static void initialMenu(PrintStream out) {
        out.println("Enter your selection");
        out.println("1. Help");
        out.println("2. Quit");
        out.println("3. Login");
        out.println("4. Register");
        out.print("Enter choice: ");
    }

    private void executeInitialChoice(int choice, PrintStream out) throws DataAccessException {
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
        if(choice != 2){loginMenu(out);}
    }

    private static void observeGame(PrintStream out) {
        out.println();
        out.println("Observe Game option selected");
    }

    private static void joinGame(PrintStream out) {
        out.println();
        out.println("Join Game option selected");
    }

    private static void listGames(PrintStream out) {
        out.println();
        out.println("List Games option selected");
    }

    private static void createGame(PrintStream out) {
        out.println();
        out.println("Create Game option selected");
    }

    private static void logout(PrintStream out) {
        out.println();
        out.println("Logout option selected");
    }

    private static void userHelp(PrintStream out) {
        out.println();
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
        out.println();
        out.println("Help menu option selected");
        out.println("Help: You are lost and confused, in need of guidance on what to do");
        out.println("Quit: No longer play the game of chess");
        out.println("Login: Sign in with username and password to access your account");
        out.println("Register: Sign up for a chess account with username, password, and email");
        out.println();
    }

    public static void quit(PrintStream out) {
        out.println();
        out.println("Quit menu option selected");
    }

    public void login(PrintStream out) {
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Login menu option selected");
        out.print("Enter username: ");
        String username = scanner.nextLine();
        out.print("Enter password: ");
        String password = scanner.nextLine();
        out.println();

        UserData newUser = new UserData(username, password, null);
        try {
            server.loginUser(newUser);
            // Registration successful
            System.out.println("Login successful");
            loginMenu(out);
            int gameChoice;
            do {
                gameChoice = scanner.nextInt();
                executeGameChoice(gameChoice, out);
            } while (gameChoice != 2);
        } catch (DataAccessException e) {
            // Registration failed
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void register(PrintStream out) throws DataAccessException {
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Register menu option selected");
        out.print("Enter desired username: ");
        String username = scanner.nextLine();
        out.print("Enter desired password: ");
        String password = scanner.nextLine();
        out.print("Enter email address: ");
        String email = scanner.nextLine();
        out.println();

        UserData newUser = new UserData(username, password, email);
        try {
            server.registerUser(newUser);
            // Registration successful
            System.out.println("Registration successful");
            loginMenu(out);
            int gameChoice;
            do {
                gameChoice = scanner.nextInt();
                executeGameChoice(gameChoice, out);
            } while (gameChoice != 2);
        } catch (DataAccessException e) {
            // Registration failed
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}
