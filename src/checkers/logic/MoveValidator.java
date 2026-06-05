package checkers.logic;

import checkers.model.*;
import java.util.*;

// Finds all legal moves for the current player.
// Rule: if any jump is available, you MUST jump (no simple moves allowed).
public class MoveValidator {

    public List<Move> getValidMoves(Board board, Piece.Color turn) {
        List<Move> jumps  = new ArrayList<>();
        List<Move> simple = new ArrayList<>();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null || p.getColor() != turn) continue;
                jumps.addAll(findJumps(board, r, c, p));
                simple.addAll(findSimple(board, r, c, p));
            }
        }

        // Jumps are mandatory — return only jumps if any exist
        return jumps.isEmpty() ? simple : jumps;
    }

    // A jump: skip over an enemy piece into an empty square
    private List<Move> findJumps(Board board, int r, int c, Piece p) {
        List<Move> list = new ArrayList<>();
        for (int dr : moveDirections(p)) {
            for (int dc : new int[]{-1, 1}) {
                int midR = r + dr,     midC = c + dc;     // enemy square
                int endR = r + 2 * dr, endC = c + 2 * dc; // landing square
                if (!board.inBounds(endR, endC)) continue;
                Piece mid = board.getPiece(midR, midC);
                if (mid != null && mid.getColor() != p.getColor()
                        && board.getPiece(endR, endC) == null) {
                    list.add(new Move(r, c, endR, endC, true));
                }
            }
        }
        return list;
    }

    // A  simple move: one step diagonally to an empty square
    private List<Move> findSimple(Board board, int r, int c, Piece p) {
        List<Move> list = new ArrayList<>();
        for (int dr : moveDirections(p)) {
            for (int dc : new int[]{-1, 1}) {
                int nr = r + dr, nc = c + dc;
                if (board.inBounds(nr, nc) && board.getPiece(nr, nc) == null)
                    list.add(new Move(r, c, nr, nc, false));
            }
        }
        return list;
    }

    // Normal pieces move one direction; kings move both ways
    private int[] moveDirections(Piece p) {
        if (p.isKing()) return new int[]{-1, 1};
        return p.getColor() == Piece.Color.RED ? new int[]{-1} : new int[]{1};
    }
}
