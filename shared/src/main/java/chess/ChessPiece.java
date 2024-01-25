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
    //class variables initialized
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
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
    /**figure out which piece type is being tested then call a piece moves function based on that*/
        return switch (type) {
            case KING -> calculateKing(board, myPosition);
            case QUEEN -> calculateQueen(board, myPosition);
            case BISHOP -> calculateBishop(board, myPosition);
            case KNIGHT -> calculateKnight(board, myPosition);
            case ROOK -> calculateRook(board, myPosition);
            case PAWN -> calculatePawn(board, myPosition);
        };
    }

    /**function to calculate the possible moves of the king piece*/
    private Set<ChessMove> calculateKing(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();

        // Possible directions the king can move (any direction one spot)
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

        // Possible directions the queen can move (straight lines and diagonals)
        int[][] possDirections = {{1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}};

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Loop to go through the whole list of possible directions and check each spot
        for(int[] dir: possDirections){
            for(int i = 1; i < boardSize; i++){
                //creates next square to check on the board by incrementing by 1 and running through all possible directions
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];

                if(validateMove(newRow, newCol, board, myPosition)){
                    //if valid, add move to list of possible moves and continue the loop
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy){
                        //stop searching if queen takes enemy
                        break;
                    }
                }else{
                    //stop searching this direction if it is blocked by friendly piece or out of bounds
                    break;
                }
            }
        }
        //return HashSet of valid moves for the queen
        return validMoves;
    }

    /**function to calculate the possible moves of the bishop piece*/
    private Set<ChessMove> calculateBishop(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int boardSize = 8;

        // Possible directions the bishop can move (diagonals)
        int[][] possDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Loop to go through the whole list of possible directions and check each spot
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

        // Possible directions the knight can move (L-shape)
        int[][] possDirections = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for(int[] dir: possDirections){
            //iterate through each possible move
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

    /**function to calculate the possible moves of the rook piece*/
    private Set<ChessMove> calculateRook(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int boardSize = 8;

        // Possible directions the rook can move (straight lines)
        int[][] possDirections = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

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
                        //stop searching if rook takes enemy
                        break;
                    }
                }else{
                    //stop searching this direction if it is blocked by friendly piece or out of bounds
                    break;
                }
            }
        }
        //return HashSet of valid moves for the rook
        return validMoves;
    }

    /**function to validate possible moves of chess pieces(except pawns)*/
    public boolean validateMove(int row, int col, ChessBoard board, ChessPosition myPosition){
        int boardSize = 8;
        enemy = false;
        //make sure the new position is within the parameters of the board
        if((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)){
            if(board.getPiece(new ChessPosition(row,col)) != null){
                if(board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(row,col)).getTeamColor()){
                    //if square on board is occupied, discover the color of piece
                    enemy = true;
                    return true;
                }
            }
            //check if that space is already occupied or if the space is occupied by the opponent
            return board.getPiece(new ChessPosition(row, col)) == null || enemy;
        }
        return false;
    }

    /**function to calculate the possible moves of the pawn piece*/
    private Set<ChessMove> calculatePawn(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int player = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1; //determine which piece color it is
        ChessPosition validPosition = new ChessPosition(row+player, col);

        if (promotionRow(row) && (validatePawn(row+player, col, board))){ //valid move
            //if pawn is getting promoted, provide promotion options
            promotionMoves(validMoves, myPosition, validPosition);
        } else if(initialRow(row)){
            for(int i = 1; i < 3; i++){ //if it is the initial move of the pawn
                if(validatePawn(row+(player*i), col, board)){
                    validPosition = new ChessPosition(row+(i*player), col);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                }else{
                    break;
                }
            }
        } else if(validatePawn(row+player, col, board)) { //basic forward movement of pawn
            validMoves.add(new ChessMove(myPosition, validPosition, null));
        }
        //check if the pawn can capture diagonally
        diagonalPawnMoves(validMoves, row, col, player, myPosition, board);

        //return HashSet of valid moves for the pawn
        return validMoves;
    }

    /**function to check if pawn is on a promoting row*/
    private boolean promotionRow(int row){
        return (pieceColor == ChessGame.TeamColor.WHITE && row == 7) || //white piece
                (pieceColor == ChessGame.TeamColor.BLACK && row == 2); //black piece
    }

    /**function to check if pawn is in starting position*/
    private boolean initialRow(int row){
        return (pieceColor == ChessGame.TeamColor.WHITE && row == 2) || //white piece
                (pieceColor == ChessGame.TeamColor.BLACK && row == 7); //black piece
    }

    /**function to create Promotion Moves*/
    private void promotionMoves(Set<ChessMove> validMoves, ChessPosition myPosition, ChessPosition validPosition){
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.KNIGHT));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.ROOK));
    }

    /**function to check diagonal moves of pawn*/
    private void diagonalPawnMoves(Set<ChessMove> validMoves, int row, int col, int player, ChessPosition myPosition, ChessBoard board){
        //check diagonals for capture capabilities
        int[][] possDirections = {{1, 1}, {1,-1}};

        for(int[] dir: possDirections){
            //creates next square to check on the board by incrementing by 1 and running through the possible directions to go
            int newRow = row + player * dir[0];
            int newCol = col + player * dir[1];

            ChessPosition validPosition = new ChessPosition(newRow, newCol);

            if(enemyChecker(newRow, newCol, board, myPosition)) {
                if(promotionRow(row)){
                    //if pawn is getting promoted, provide promotion options
                    promotionMoves(validMoves, myPosition, validPosition);
                }else{
                    //if valid, add move to list of possible moves and continue the loop
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                }
            }
        }
    }

    /**function to validate if pawn can move*/
    private boolean validatePawn(int row, int col, ChessBoard board){
        int boardSize = 8;
        //make sure the new position is within the parameters of the board
        return (row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)
                && board.getPiece(new ChessPosition(row, col)) == null; //check if space is occupied
    }

    /**function to check if square is occupied*/
    private boolean enemyChecker(int row, int col, ChessBoard board, ChessPosition myPosition){
        int boardSize = 8;
        enemy = false;

        //make sure the new position is within the parameters of the board
        if((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)) {
            if (board.getPiece(new ChessPosition(row, col)) != null) {
                //if square on board is occupied, discover the color of piece
                enemy = board.getPiece(new ChessPosition(row,col)).getTeamColor() != board.getPiece(myPosition).getTeamColor();
            }
            //check if the space is occupied by the opponent
            return enemy;
        }
        return false;
    }
}



