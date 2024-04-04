package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard gameBoard;
    ChessBoard testBoard;
    TeamColor turn;
    private boolean modifiedCopy;

    ChessMove dangerPiece;

    public ChessGame() {
        gameBoard = new ChessBoard();
        turn = TeamColor.WHITE;
        modifiedCopy = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", turn=" + turn +
                '}';
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allValid = new HashSet<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if(piece != null){
            TeamColor teamPiece = piece.getTeamColor();
                for(ChessMove move : gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition)){
                    testBoard = new ChessBoard(gameBoard);
                    testBoard.movePiece(move.getStartPosition(), move.getEndPosition(), testBoard.getPiece(move.getStartPosition()));
                    modifiedCopy = true;
                    if(!isInCheck(teamPiece)){
                        allValid.add(move);
                    }
                    modifiedCopy = false;
                }
        }
        return allValid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionType;
        ChessPiece promotion;
        boolean moveValid = false;
        //have to call valid move function
        for(ChessMove check : validMoves(startPosition)){
            if(check.equals(move)){
                moveValid = true;
                break;
            }
        }
        //make deep copy of the board
        testBoard = new ChessBoard(gameBoard);
        //move piece on testBoard
        testBoard.movePiece(startPosition, endPosition, testBoard.getPiece(startPosition));
        modifiedCopy = true;
        //modifiedCopy = false;
        if(!moveValid){  //move is illegal if the chess piece cannot move there,
            throw new InvalidMoveException("Invalid move: The chess piece cannot move to the specified position.");
        } // if it’s not the corresponding team's turn,
        if(getTeamTurn() != gameBoard.getPiece(startPosition).getTeamColor()){
            throw new InvalidMoveException("Invalid move: It is not your turn.");
        } // or if the move leaves the team’s king in danger
        if(isInCheck(getTeamTurn())){
            throw new InvalidMoveException("Invalid move: Will leave your king in check.");
        }
        if(gameBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN){
            if((move.getEndPosition().getRow() == 8 && getTeamTurn() == TeamColor.WHITE) || (move.getEndPosition().getRow() == 1 && getTeamTurn() == TeamColor.BLACK)){
                promotionType = move.getPromotionPiece();
                promotion = new ChessPiece(getTeamTurn(), promotionType);
                //update board with new promotion piece
                gameBoard.movePiece(startPosition, endPosition, promotion);
            }else {
                // The conditions for promotion are not met, update board with the original piece move
                gameBoard.movePiece(startPosition, endPosition, gameBoard.getPiece(startPosition));
            }
        }else{
            //update board with new piece move(not pawn)
            gameBoard.movePiece(startPosition, endPosition, gameBoard.getPiece(startPosition));
        }
        //switch turns
        setTeamTurn((getTeamTurn() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) { //applies to current team
        // Use testBoard based on the flag
        testBoard = modifiedCopy ? testBoard : new ChessBoard(gameBoard);
        return isKingInDanger(testBoard, teamColor);
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        testBoard = new ChessBoard(gameBoard);
        return isKingInDanger(testBoard, teamColor);
    }

    private boolean isKingInDanger(ChessBoard board, TeamColor teamColor){
        ChessPosition opposingPosition;
        ChessPosition kingPosition = findKing(teamColor);
        TeamColor opposingColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                opposingPosition = new ChessPosition(i, j);
                if (board.getPiece(opposingPosition) != null && board.getPiece(opposingPosition).getTeamColor() == opposingColor) {
                    for (ChessMove check : board.getPiece(opposingPosition).pieceMoves(board, opposingPosition)) {
                        if (check.getEndPosition().equals(kingPosition)) {
                            dangerPiece = check;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition position;

        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                position = new ChessPosition(i, j);
                if(gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor){
                    for(ChessMove move : validMoves(position)){
                        testBoard = new ChessBoard(gameBoard);
                        //move piece on testBoard
                        testBoard.movePiece(move.getStartPosition(), move.getEndPosition(), testBoard.getPiece(move.getStartPosition()));
                        modifiedCopy = true;
                        if(!isInCheck(teamColor)){
                            // If at least one valid move is found that doesn't result in the king being checked,
                            // the team is not in stalemate
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private ChessPosition findKing(TeamColor teamColor){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                if(testBoard.getPiece(position) != null && testBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && testBoard.getPiece(position).getTeamColor() == teamColor){
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
