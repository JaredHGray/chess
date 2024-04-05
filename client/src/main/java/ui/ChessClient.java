package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.notificationMessage;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static java.lang.System.out;
//import chess.ChessBoard;

public class ChessClient {

    private String registeredUsername = null;
    private String authToken = null;
    private final String serverUrl;

    private GameData[] games = null;
    private final ServerFacade server;
    ChessBoard printBoard = new ChessBoard();
    private GameData chosenGame;
    private ChessGame.TeamColor playerColor;
    ChessGame webSocketGame;
    private final NotificationHandler notificationHandler = notification->{
        out.println("yellow");
        out.println(notification);
    };
    private WebSocketFacade ws;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
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

    private void initialMenu(PrintStream out) {
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

    private void loginMenu(PrintStream out){
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

    private void gamePlayMenu(PrintStream out){
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
        printBoard.run(true, chosenGame.game().getBoard());
        out.println();
        printBoard.run(false, chosenGame.game().getBoard());
    }

    private void leaveGame(PrintStream out){
        out.println();
        out.println("Leave option selected");
        ws.leaveGameSocket(chosenGame.gameID(), authToken);
    }

    private void makeMove(PrintStream out){
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Make Move option selected");
        out.print("Enter coordinates of piece to move(ex: 'a2'):");
        String piece = scanner.nextLine();
        out.print("Enter coordinates of destination(ex: 'c4'):");
        String dest = scanner.nextLine();
        ChessPosition startPosition = getPosition(piece);
        ChessPosition endPosition = getPosition(dest);

        ChessPiece startPiece = webSocketGame.getBoard().getPiece(startPosition);
        ChessMove move;
        if(isPromotionMove(startPiece, endPosition)){
            out.print("Enter desired promotion piece type(ex: 'Q' for Queen):");
            String type = scanner.nextLine();
            ChessPiece.PieceType promotion = getPromotionPieceType(type);
            move = new ChessMove(startPosition, endPosition, promotion);
        } else {
            move = new ChessMove(startPosition, endPosition, null);
        }
        ws.makeMoveSocket(authToken, chosenGame.gameID(), move);
    }

    private boolean isPromotionMove(ChessPiece pieceAtStart, ChessPosition endPosition) {
        if (pieceAtStart.getPieceType() == ChessPiece.PieceType.PAWN) {
            return ((pieceAtStart.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) ||
                    (pieceAtStart.getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1));
        }
        return false;
    }

    private ChessPiece.PieceType getPromotionPieceType(String promotionPiece) {
        return switch (promotionPiece.toUpperCase()) {
            case "Q" -> ChessPiece.PieceType.QUEEN;
            case "R" -> ChessPiece.PieceType.ROOK;
            case "B" -> ChessPiece.PieceType.BISHOP;
            case "N" -> ChessPiece.PieceType.KNIGHT;
            default -> ChessPiece.PieceType.QUEEN;
        };
    }

    private void resignGame(PrintStream out){
        out.println();
        out.println("Resign option selected");
        ws.resignGameSocket(authToken, chosenGame.gameID());
    }

    private void highlightChoices(PrintStream out){
        Scanner scanner = new Scanner(System.in);
        out.println();
        out.println("Highlight Legal Moves option selected");
        out.print("Enter piece to highlight(ex: 'a2'):");
        String piece = scanner.nextLine();
        ChessPosition startPosition = getPosition(piece);
        Collection<ChessMove> validMoves = webSocketGame.validMoves(startPosition);
        boolean white = playerColor == ChessGame.TeamColor.WHITE;
        printBoard.highlightMoves(white, webSocketGame.getBoard(), validMoves);
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
        chosenGame = games[gameChoice-1];
        try{
            server.joinGame(chosenGame.gameID(), null, authToken);
            printBoard.run(true, chosenGame.game().getBoard());
            out.println();
            printBoard.run(false, chosenGame.game().getBoard());
            out.println(chosenGame.gameName() + " successfully joined as an observer");
            ws.observePlayerSocket(chosenGame.gameID(), authToken);
            gamePlayMenu(out);
            do{
                gameChoice = scanner.nextInt();
                executeMoveChoice(gameChoice, out);
            } while (gameChoice != 3 && gameChoice != 5);
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
        playerColor = teamColor(pieceColor);
        chosenGame = games[gameChoice-1];
        try{
            server.joinGame(chosenGame.gameID(), pieceColor.toUpperCase(), authToken);
            printBoard.run(true, chosenGame.game().getBoard());
            out.println();
            printBoard.run(false, chosenGame.game().getBoard());
            out.println(chosenGame.gameName() + " successfully joined");
            ws.joinPlayerSocket(chosenGame.gameID(), playerColor, authToken);
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

    private void userHelp(PrintStream out) {
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

    public void initialHelp(PrintStream out) {
        out.println();
        out.println("Help menu option selected");
        out.println("Help: You are lost and confused, in need of guidance on what to do");
        out.println("Quit: No longer play the game of chess");
        out.println("Login: Sign in with username and password to access your account");
        out.println("Register: Sign up for a chess account with username, password, and email");
        out.println();
    }

    public void gameHelp(PrintStream out){
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
            ws = new WebSocketFacade(serverUrl);
            ws.setMessageListener(new ServerMessageHandler());
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
            ws = new WebSocketFacade(serverUrl);
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
            out.println("Failure: " + e.getMessage());
        }
    }

    private ChessGame.TeamColor teamColor(String playerColor){
        if(playerColor.equalsIgnoreCase("white")){
            return ChessGame.TeamColor.WHITE;
        }else if(playerColor.equalsIgnoreCase("black")){
            return ChessGame.TeamColor.BLACK;
        }else{
            return null;
        }
    }

    private ChessPosition getPosition(String position){
        String letters = "abcdefgh";
        char fileChar = position.charAt(0);
        int col = letters.indexOf(fileChar) + 1;
        int row = Integer.parseInt(position.substring(1));
        return new ChessPosition(row, col);
    }

    private class ServerMessageHandler implements WebSocketFacade.ServerMessageListener {
        @Override
        public void onLoadGame(LoadGameMessage message) {
            webSocketGame = message.getGame();
            printBoard.run(true, webSocketGame.getBoard());
            out.println();
            printBoard.run(false, webSocketGame.getBoard());
        }

        @Override
        public void onNotification(notificationMessage message) {
            Scanner scanner = new Scanner(System.in);
            out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            out.println();
            out.println("NOTIFICATION: " + message.getMessage());
            out.print(EscapeSequences.RESET_TEXT_COLOR);
            out.println();
            out.print("Enter choice: ");
            int gameChoice = scanner.nextInt();
            executeMoveChoice(gameChoice, out);
        }

        @Override
        public void onError(ErrorMessage message) {
            Scanner scanner = new Scanner(System.in);
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            out.println();
            out.println("ERROR: " + message.getErrorMessage());
            out.print(EscapeSequences.RESET_TEXT_COLOR);
            out.print("Enter choice: ");
            int gameChoice = scanner.nextInt();
            executeMoveChoice(gameChoice, out);
        }
    }
}
