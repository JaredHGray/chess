package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //initialize the board
    private final ChessPiece[][] board;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard() {
        //set board size
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //need to first see if something is on the position first
        if(board[position.getRow()-1][position.getColumn()-1] == null) {
            board[position.getRow()-1][position.getColumn()-1] = piece;
        } else{
            throw new RuntimeException("Position on Chessboard already Occupied");
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

            return board[position.getRow()-1][position.getColumn()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int row = 1;
        int col = 1;
        ChessPosition position;
        ChessPiece piece;

        /** White Pieces of the Board*/

        /**White Rooks*/
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(position, piece);
        col = 8;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(position, piece);
        /**White Knights*/
        col = 2;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);
        col = 7;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);
        /**White Bishops*/
        col = 3;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(position, piece);
        col = 6;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(position, piece);
        /**White Queen*/
        col = 4;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(position, piece);
        /**White King*/
        col = 5;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(position, piece);
        /** White Pawns*/
        row = 2;
        for (col = 1; col <= 8; col++) {
            position = new ChessPosition(row, col);
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
        }

        /**Black Pieces of the Board*/
        row = 8;
        col = 1;
        /**Black Rooks*/
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(position, piece);
        col = 8;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(position, piece);
        /**Black Knights*/
        col = 2;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);
        col = 7;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);
        /**Black Bishops*/
        col = 3;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(position, piece);
        col = 6;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(position, piece);
        /**Black Queen*/
        col = 4;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(position, piece);
        /**Black King*/
        col = 5;
        position = new ChessPosition(row, col);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(position, piece);
        /** Black Pawns*/
        row = 7;
        for (col = 1; col <= 8; col++) {
            position = new ChessPosition(row, col);
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
        }
    }
}
