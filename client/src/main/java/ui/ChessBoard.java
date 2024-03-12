package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";
    private static final String[] rowHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String[] columnHeaders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EMPTY);
        rowHeaders(out);
        drawChessBoard(out);
        out.print(EMPTY);
        rowHeaders(out);

        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void rowHeaders(PrintStream out) {
        setGray(out);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            printHeaderText(out, rowHeaders[boardCol]);
        }
        setBlack(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            printHeaderText(out, columnHeaders[boardRow]);
            drawRowOfSquares(out, boardRow);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
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
            out.println();
    }

    private static void drawVerticalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            //setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setGray(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
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