package checkers.logic;

import checkers.model.*;

// Tracks if the game is still going, or who won.
// Simple rule: if the current player has no moves left, they lose.
// DRAW can be reached by mutual agreement (Offer Draw button) or by the
// "no capture for a long time" rule handled in GameEngine.
public class GameState {
    public enum Status { ONGOING, RED_WINS, BLACK_WINS, DRAW }

    private Status status = Status.ONGOING;

    public void evaluate(Board board, Piece.Color currentTurn, MoveValidator mv) {
        // Don't override a finished game (e.g. an agreed draw).
        if (status != Status.ONGOING) return;

        // Check if current player has no pieces left
        if (board.countPieces(currentTurn) == 0) {
            status = (currentTurn == Piece.Color.RED) ? Status.BLACK_WINS : Status.RED_WINS;
            return;
        }

        // Check if current player has no valid moves
        if (mv.getValidMoves(board, currentTurn).isEmpty()) {
            status = (currentTurn == Piece.Color.RED) ? Status.BLACK_WINS : Status.RED_WINS;
        }
    }

    /** Force a draw (mutual agreement or the no-progress rule). */
    public void setDraw() {
        if (status == Status.ONGOING) {
            status = Status.DRAW;
        }
    }

    public Status  getStatus() { return status; }
    public boolean isOver()    { return status != Status.ONGOING; }
    public boolean isDraw()    { return status == Status.DRAW; }
}
