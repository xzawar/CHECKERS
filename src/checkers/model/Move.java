package checkers.model;

public class Move {
    public final int fromRow, fromCol, toRow, toCol;
    public final boolean isJump;

    public Move(int fromRow, int fromCol, int toRow, int toCol, boolean isJump) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow   = toRow;
        this.toCol   = toCol;
        this.isJump  = isJump;
    }

    @Override
    public String toString() {
        return "(" + fromRow + "," + fromCol + ") -> (" + toRow + "," + toCol + ")"
               + (isJump ? " [JUMP]" : "");
    }
}
