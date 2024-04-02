package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerFacade;
//import chess.ChessBoard;

public class ChessClient {

    private String registeredUsername = null;
    private String authToken = null;

    private GameData[] games = null;
    private final ServerFacade server;
    ChessBoard printBoard = new ChessBoard();

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
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
        out.println("1. Help");
        out.println("2. Quit");
        out.println("3. Login");
        out.println("4. Register");
        out.print("Enter choice: ");
    }

    private void executeInitialChoice(int choice, PrintStream out) {
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
        out.println("1. Help");
        out.println("2. Logout");
        out.println("3. Create Game");
        out.println("4. List Games");
        out.println("5. Join Game");
        out.println("6. Observe Game");
        out.print("Enter choice: ");
    }

    private void executeGameChoice(int choice, PrintStream out) {
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

    private static void gamePlayMenu(PrintStream out){
        out.println("1. Help");
        out.println("2. Redraw Chess Board");
        out.println("3. Leave");
        out.println("4. Make Move");
        out.println("5. Resign");
        out.println("6. Highlight Legal Choices");
        out.print("Enter choice: ");
    }

    private void executeMoveChoice(int choice, PrintStream out) {
        switch (choice) {
            case 1:
                gameHelp(out);
                break;
            case 2:
                drawChessBoard(out);
                break;
            case 3:
                leaveGame(out);
                break;
            case 4:
                makeMove(out);
                break;
            case 5:
                resignGame(out);
                break;
            case 6:
                highlightChoices(out);
                break;
            default:
                out.println("Invalid choice");
        }
        if(choice != 3 && choice != 5){gamePlayMenu(out);}
    }

    private void drawChessBoard(PrintStream out){
        out.println();
        out.println("Redraw Chessboard option selected");
//        printBoard.run(true);
//        out.println();
//        printBoard.run(false);
    }

    private void leaveGame(PrintStream out){
        out.println();
        out.println("Leave option selected");
    }

    private void makeMove(PrintStream out){
        out.println();
        out.println("Make Move option selected");
    }

    private void resignGame(PrintStream out){
        out.println();
        out.println("Resign option selected");
    }

    private void highlightChoices(PrintStream out){
        out.println();
        out.println("Highlight Legal Moves option selected");
    }

    private void observeGame(PrintStream out) {
        getGames();
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Observe Game option selected");
        out.print("Enter number for the game to be observed: ");
        int gameChoice = scanner.nextInt();
        scanner.nextLine();
        if (gameChoice < 1 || gameChoice > games.length) {
            out.println("Invalid game number.");
            return;
        }
        GameData chosenGame = games[gameChoice-1];
        try{
            server.joinGame(chosenGame.gameID(), null, authToken);
            printBoard.run(true, chosenGame.game().getBoard());
            out.println();
            printBoard.run(false, chosenGame.game().getBoard());
            out.println(chosenGame.gameName() + " successfully joined as an observer");
        } catch (DataAccessException e) {
            System.out.println("Failure: " + e.getMessage());
        }
    }

    private void joinGame(PrintStream out) {
        getGames();
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Join Game option selected");
        out.print("Enter number for the game to be joined: ");
        int gameChoice = scanner.nextInt();
        scanner.nextLine();
        if (gameChoice < 1 || gameChoice > games.length) {
            out.println("Invalid game number.");
            return;
        }
        out.print("Enter desired piece color: ");
        String pieceColor = scanner.nextLine();
        GameData chosenGame = games[gameChoice-1];
        try{
            server.joinGame(chosenGame.gameID(), pieceColor.toUpperCase(), authToken);
            printBoard.run(true, chosenGame.game().getBoard());
            out.println();
            printBoard.run(false, chosenGame.game().getBoard());
            out.println(chosenGame.gameName() + " successfully joined");
            gamePlayMenu(out);
            do{
                gameChoice = scanner.nextInt();
                executeMoveChoice(gameChoice, out);
            } while (gameChoice != 3 && gameChoice != 5);
        } catch (DataAccessException e) {
            System.out.println("Failure: " + e.getMessage());
        }
    }

    private void listGames(PrintStream out) {
        out.println();
        out.println("List Games option selected");
        int count = 1;
        getGames();
        out.printf("%-5s %-20s %-15s %-15s\n", "ID", "Game Name", "White Player", "Black Player");
        out.println("--------------------------------------------------------");
        for (var game : games) {
            out.printf("%-5d %-20s %-15s %-15s\n", count, game.gameName(), game.whiteUsername(), game.blackUsername());
            count++;
        }
        out.println();
    }

    private void createGame(PrintStream out) {
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Create Game option selected");
        out.print("Enter game name: ");
        try {
            String gameName = scanner.nextLine();
            GameData gameInfo = new GameData(0, null, null, gameName, null);
            server.makeGame(gameInfo, authToken);
            out.println("Game titled: " + gameName + " created successfully");
        } catch (DataAccessException e) {
            System.out.println("Game creation failed: " + e.getMessage());
        }
    }

    private void logout(PrintStream out) {
        out.println();
        out.println("Logout option selected");
        try {
            server.logoutUser(authToken);
        } catch (DataAccessException e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
        registeredUsername = null;
        authToken = null;
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

    public static void gameHelp(PrintStream out){
        out.println();
        out.println("Help menu option selected");
        out.println("Redraw Chess Board: You are lost and confused, in need of guidance on what to do");
        out.println("Leave: No longer play your current game of chess");
        out.println("Make Move: Input a valid move on the chessboard");
        out.println("Resign: Forfeit the game of chess you are currently playing");
        out.println("Highlight Legal Moves: Input the location of a chess piece for which you want to highlight its legal moves");
        out.println();
    }

    public void quit(PrintStream out) {
        out.println();
        out.println("Quit menu option selected");
        registeredUsername = null;
        authToken = null;
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
            AuthData response = server.loginUser(newUser);
            System.out.println("Login successful");
            registeredUsername = response.username();
            authToken = response.authToken();
            loginMenu(out);
            int gameChoice;
            do {
                gameChoice = scanner.nextInt();
                executeGameChoice(gameChoice, out);
            } while (gameChoice != 2);
        } catch (DataAccessException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void register(PrintStream out) {
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
            AuthData response = server.registerUser(newUser);
            System.out.println("Registration successful");
            registeredUsername = response.username();
            authToken = response.authToken();
            loginMenu(out);
            int gameChoice;
            do {
                gameChoice = scanner.nextInt();
                executeGameChoice(gameChoice, out);
            } while (gameChoice != 2);
        } catch (DataAccessException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void getGames() {
        try {
            games = server.listGames(authToken);
        } catch (DataAccessException e) {
            System.out.println("Failure: " + e.getMessage());
        }
    }
}
