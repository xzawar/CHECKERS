package checkers.logic;

import checkers.model.*;

// Tracks if the game is still going, or who won.
// Simple rule: if the current player has no moves left, they lose.
public class GameState {
    public enum Status { ONGOING, RED_WINS, BLACK_WINS }

    private Status status = Status.ONGOING;

    public void evaluate(Board board, Piece.Color currentTurn, MoveValidator mv) {
        if (mv.getValidMoves(board, currentTurn).isEmpty()) {
            // Current player has no moves → they lose
            status = (currentTurn == Piece.Color.RED) ? Status.BLACK_WINS : Status.RED_WINS;
        }
    }

    public Status  getStatus() { return status; }
    public boolean isOver()    { return status != Status.ONGOING; }
}
