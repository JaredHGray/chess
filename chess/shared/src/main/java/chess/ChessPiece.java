package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    //class variables intialized
    PieceType type;
    ChessGame.TeamColor pieceColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    boolean enemy;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    /** figure out which piece type is being tested then call a piece moves function based on that*/
        switch(type) {
            case KING:
                return calculateKing(board, myPosition);
            case QUEEN:
                return calculateQueen(board, myPosition);
            case BISHOP:
                return calculateBishop(board, myPosition);
            case KNIGHT:
                return calculateKnight(board, myPosition);
            case ROOK:
                throw new RuntimeException("Not implemented");
            case PAWN:
                throw new RuntimeException("Not implemented");
            default:
                throw new RuntimeException("Unknown game piece");
        }
    }

    /**function to calculate the possible moves of the king piece*/
    private Set<ChessMove> calculateKing(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        //calculate possible directions the king can move
        //kings can move in any direction one spot
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                //creates next square to check on the board by incrementing by 1 in each direction
                int newRow = row + i;
                int newCol = col + j;
                if(validateMove(newRow, newCol, board, myPosition)){
                    //if valid, add move to list of possible moves and continue the loop
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy){
                        //stop searching if king takes enemy
                        break;
                    }
                }
            }
        }
        //return HashSet of valid moves for the king
        return validMoves;
    }

    /**function to calculate the possible moves of the queen piece*/
    private Set<ChessMove> calculateQueen(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int boardSize = 8;
        //calculate possible directions the queen can move
        //queens can move in straight lines and diagonals as far as there is open space
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[][] possDirections = {{1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}};

        for(int[] dir: possDirections){
            for(int i = 1; i < boardSize; i++){
                //creates next square to check on the board by incrementing by 1 and running through the possible directions to go
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];
                if(validateMove(newRow, newCol, board, myPosition)){
                    //if valid, add move to list of possible moves and continue the loop
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy){
                        //stop searching if bishop takes enemy
                        break;
                    }
                }else{
                    //stop searching this direction if it is blocked by friendly piece or out of bounds
                    break;
                }
            }
        }

        return validMoves;
    }

    /**function to calculate the possible moves of the bishop piece*/
    private Set<ChessMove> calculateBishop(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int boardSize = 8;
        //calculate possible directions the bishop can move
        //bishops can move in any diagonal direction
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[][] possDirections = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        //create loop to go through the whole list of possible directions and check each spot to see if it is valid to move there
        for(int[] dir: possDirections){
            for(int i = 1; i < boardSize; i++){
                //creates next square to check on the board by incrementing by 1 and running through the possible directions to go
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];
                if(validateMove(newRow, newCol, board, myPosition)){
                    //if valid, add move to list of possible moves and continue the loop
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy){
                        //stop searching if bishop takes enemy
                        break;
                    }
                }else{
                    //stop searching this direction if it is blocked by friendly piece or out of bounds
                    break;
                }
            }
        }
        //return HashSet of valid moves for the bishop
        return validMoves;
    }

    /**function to calculate the possible moves of the knight piece*/
    private Set<ChessMove> calculateKnight(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        //calculate possible directions the knight can move
        //knights can move in an L shape, moving 2 squares in one direction and 1 square in the other direction
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[][] possDirections = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {1,-2}, {-1,2}, {-1,-2}};
        for(int[] dir: possDirections){ //iterate through each possible move
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if(validateMove(newRow, newCol, board, myPosition)){
                //if valid, add move to list of possible moves and continue the loop
                ChessPosition validPosition = new ChessPosition(newRow, newCol);
                validMoves.add(new ChessMove(myPosition, validPosition, null));
            }
        }
        //return HashSet of valid moves for the knight
        return validMoves;
    }

    private boolean validateMove(int row, int col, ChessBoard board, ChessPosition myPosition){
        int boardSize = 8;
        enemy = false;

        //make sure the new position is in the parameters of the board
        if((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)){
            if(board.getPiece(new ChessPosition(row,col)) != null){
                //if square on board is occupied, discover color of piece
                enemy = board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(myPosition).getTeamColor();
            }
            //check to see if that space is already occupied or if the space is occupied by the opponent
            return board.getPiece(new ChessPosition(row, col)) == null || enemy;
        }
        return false;
    }

}

