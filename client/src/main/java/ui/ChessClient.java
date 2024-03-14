package ui;

import server.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        executeChoice(choice);
    }

    private static void executeChoice(int choice) {
        switch (choice) {
            case 1:
                help();
                break;
            case 2:
                quit();
                break;
            case 3:
                login();
                break;
            case 4:
                register();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    public static void help() {
        System.out.println("Help menu option selected");
    }

    public static void quit() {
        System.out.println("Quit menu option selected");
    }

    public static void login() {
        System.out.println("Login menu option selected");
    }

    public static void register() {
        System.out.println("Register menu option selected");
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
}
