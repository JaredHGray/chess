package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";
    private static final String[] rowHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String[] columnHeaders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawChessBoard(out);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void colHeaders(PrintStream out) {
        setGray(out);
        out.print(EMPTY);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            printHeaderText(out, rowHeaders[boardCol]);
        }
        out.print(EMPTY);
        setBlack(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        out.print(player);
    }

    private static void drawChessBoard(PrintStream out) {
        colHeaders(out);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            printHeaderText(out, columnHeaders[boardRow]);
            drawRowOfSquares(out, boardRow);
            printHeaderText(out, columnHeaders[boardRow]);
            setBlack(out);
            out.println();
        }
        colHeaders(out);
    }

    private static void drawRowOfSquares(PrintStream out, int rowNumber) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if ((boardCol + rowNumber) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }
                printPlayer(out, rand.nextBoolean() ? X : O);
                setBlack(out);
            }
    }

    private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setGray(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        out.print(player);
        setWhite(out);
    }
}