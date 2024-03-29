package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = "   ";
    private static final String[] rowHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String[] columnHeaders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
    private static String[][] chessboard;

    public void run(boolean whitePerspective) {
        initializeChessboard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (whitePerspective) {
            drawChessBoard(out, true);  // Print from white player perspective
        } if (!whitePerspective) {
            drawChessBoard(out, false);  // Print from black player perspective
        }
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void colHeaders(PrintStream out, boolean whitePerspective) {
        if (!whitePerspective) {
            setGray(out);
            out.print(EMPTY);
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                printHeaderText(out, rowHeaders[boardCol]);
            }
            out.print(EMPTY);
            resetBackground(out);
            out.println();
        } else {
            setGray(out);
            out.print(EMPTY);
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                printHeaderText(out, rowHeaders[boardCol]);
            }
            out.print(EMPTY);
            resetBackground(out);
            out.println();
        }
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        out.print(player);
    }

    private static void drawChessBoard(PrintStream out, boolean whitePerspective) {
        colHeaders(out, whitePerspective);

        for (int boardRow = whitePerspective ? BOARD_SIZE_IN_SQUARES - 1 : 0;
             whitePerspective ? boardRow >= 0 : boardRow < BOARD_SIZE_IN_SQUARES;
             boardRow += (whitePerspective ? -1 : 1)) {

            printHeaderText(out, columnHeaders[boardRow]);
            drawRowOfSquares(out, boardRow);
            printHeaderText(out, columnHeaders[boardRow]);
            resetBackground(out);
            out.println();
        }

        colHeaders(out, whitePerspective);
    }

    private static void drawRowOfSquares(PrintStream out, int rowNumber) {
        String currentPiece;
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if ((boardCol + rowNumber) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }
                if(chessboard[rowNumber][boardCol] == null){
                    currentPiece = EMPTY;
                } else {
                    currentPiece = chessboard[rowNumber][boardCol];
                }
                printStarterBoard(out, currentPiece, rowNumber);
                resetBackground(out);
            }
    }

    private static void initializeChessboard() {
        chessboard = new String[BOARD_SIZE_IN_SQUARES][BOARD_SIZE_IN_SQUARES];
        // Set up pawns
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            chessboard[1][col] = EscapeSequences.BLACK_PAWN; // Black pawns
            chessboard[6][col] = EscapeSequences.WHITE_PAWN; // White pawns
        }

        String[] whitePieces = {EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK};
        String[] blackPieces = {EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KING, EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK};

        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
            chessboard[0][col] = blackPieces[col];
            chessboard[7][col] = whitePieces[col];
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

    private static void resetBackground(PrintStream out) {
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void printStarterBoard(PrintStream out, String player, int row) {
        if(row > 3){
            out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        } else {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        }
        out.print(player);
        setWhite(out);
    }
}