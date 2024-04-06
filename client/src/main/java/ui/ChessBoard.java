package ui;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = "   ";
    private static final String[] rowHeaders = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String[] columnHeaders = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
    private static chess.ChessBoard chessboard;

    public void run(boolean whitePerspective, chess.ChessBoard board) {
        chessboard = board;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (whitePerspective) {
            drawChessBoard(out, true, null, null);  // Print from white player perspective
        } if (!whitePerspective) {
            drawChessBoard(out, false, null, null);  // Print from black player perspective
        }
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    public void highlightMoves(boolean whitePerspective, chess.ChessBoard board, Collection<ChessMove> validMoves, ChessPosition chosenPiece) {
        chessboard = board;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (whitePerspective) {
            drawChessBoard(out, true, validMoves, chosenPiece);  // Print from white player perspective
        } if (!whitePerspective) {
            drawChessBoard(out, false, validMoves, chosenPiece);  // Print from black player perspective
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

    private static void drawChessBoard(PrintStream out, boolean whitePerspective, Collection<ChessMove> validMoves, ChessPosition chosenPiece) {
        colHeaders(out, whitePerspective);

        for (int boardRow = whitePerspective ? BOARD_SIZE_IN_SQUARES - 1 : 0;
             whitePerspective ? boardRow >= 0 : boardRow < BOARD_SIZE_IN_SQUARES;
             boardRow += (whitePerspective ? -1 : 1)) {

            printHeaderText(out, columnHeaders[boardRow]);
            drawRowOfSquares(out, boardRow, validMoves, chosenPiece);
            printHeaderText(out, columnHeaders[boardRow]);
            resetBackground(out);
            out.println();
        }
        colHeaders(out, whitePerspective);
    }

    private static void drawRowOfSquares(PrintStream out, int rowNumber, Collection<ChessMove> validMoves, ChessPosition chosenPiece) {
        String currentPiece;
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                ChessPosition currentPosition = new ChessPosition(rowNumber + 1, boardCol + 1);
                if ((boardCol + rowNumber) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

                if (chosenPiece != null && chosenPiece.equals(currentPosition)) {
                    // Highlight chosen piece in yellow
                    out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                    currentPiece = discoverPiece(chessboard.getPiece(currentPosition).getPieceType(), chessboard.getPiece(currentPosition).getTeamColor());
                } else if (validMoves != null && checkMoves(validMoves, currentPosition)) {
                    // Highlight available moves in green
                    out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                    ChessPiece piece = chessboard.getPiece(currentPosition);
                    if (piece != null) {
                        ChessPiece.PieceType pieceType = piece.getPieceType();
                        ChessGame.TeamColor teamColor = piece.getTeamColor();
                        if (pieceType != null && teamColor != null) {
                            currentPiece = discoverPiece(pieceType, teamColor);
                        } else {currentPiece = EMPTY;}
                    } else {currentPiece = EMPTY;}
                } else if(chessboard.getBoard()[rowNumber][boardCol] == null){
                    currentPiece = EMPTY;
                } else {
                    currentPiece = discoverPiece(chessboard.getPiece(currentPosition).getPieceType(), chessboard.getPiece(currentPosition).getTeamColor());
                }
                printStarterBoard(out, currentPiece, rowNumber);
                resetBackground(out);
            }
    }

    private static boolean checkMoves(Collection<ChessMove> moves, ChessPosition currentPosition){
        for(ChessMove move : moves){
            if(move.getEndPosition().equals(currentPosition)){
                return true;
            }
        }
        return false;
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

    private static String discoverPiece(ChessPiece.PieceType type, ChessGame.TeamColor color){
        String piece;
        if(color == ChessGame.TeamColor.WHITE){
            piece = switch (type) {
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case KING -> EscapeSequences.WHITE_KING;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else{
            piece = switch (type) {
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case KING -> EscapeSequences.BLACK_KING;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
    return piece;
    }
}