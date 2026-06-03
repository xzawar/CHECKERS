package checkers.logic;

import checkers.model.*;
import java.util.List;

// The brain. Holds the board, whose turn it is, and game status.
// You call applyMove() to make a move; it validates, executes, and switches turns.
public class GameEngine {
    private Board        board;
    private Piece.Color  currentTurn;
    private MoveValidator validator;
    private GameState    state;

    public GameEngine() {
        board        = new Board();
        currentTurn  = Piece.Color.RED;   // RED always goes first
        validator    = new MoveValidator();
        state        = new GameState();
    }

    public Board       getBoard()       { return board; }
    public Piece.Color getCurrentTurn() { return currentTurn; }
    public GameState   getState()       { return state; }
    public List<Move>  getValidMoves()  { return validator.getValidMoves(board, currentTurn); }

    // Try to apply a move. Returns true if legal, false if illegal.
    public boolean applyMove(Move m) {
        List<Move> valid = getValidMoves();

        // Check if the move exists in the legal list
        boolean legal = valid.stream().anyMatch(v ->
            v.fromRow == m.fromRow && v.fromCol == m.fromCol &&
            v.toRow   == m.toRow   && v.toCol   == m.toCol);

        if (!legal) return false;

        board.applyMove(m);

        // After move, check next player's situation with switched turn
        Piece.Color nextTurn = (currentTurn == Piece.Color.RED)
                             ? Piece.Color.BLACK : Piece.Color.RED;
        state.evaluate(board, nextTurn, validator);

        if (!state.isOver()) currentTurn = nextTurn;
        return true;
    }
}
