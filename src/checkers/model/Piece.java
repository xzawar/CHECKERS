package checkers.model;

public class Piece {
    public enum Color { RED, BLACK }

    private Color color;
    private boolean isKing;

    public Piece(Color color) {
        this.color = color;
        this.isKing = false;
    }

    public Color getColor()  { return color; }
    public boolean isKing()  { return isKing; }
    public void makeKing()   { isKing = true; }

    @Override
    public String toString() {
        if (isKing) return color == Color.RED ? "R" : "B";
        return color == Color.RED ? "r" : "b";
    }
}
