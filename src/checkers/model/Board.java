package checkers.model;

public class Board {
    public static final int SIZE = 6;

    private Piece[][] grid = new Piece[SIZE][SIZE];

    public Board() {
        init();
    }

    private void init() {
        // BLACK gets top 2 rows on odd squares
        for (int r = 0; r < 2; r++)
            for (int c = 0; c < SIZE; c++)
                if ((r + c) % 2 == 1)
                    grid[r][c] = new Piece(Piece.Color.BLACK);

        // RED gets bottom 2 rows on odd squares
        for (int r = SIZE - 2; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if ((r + c) % 2 == 1)
                    grid[r][c] = new Piece(Piece.Color.RED);
    }

    public Piece getPiece(int r, int c)          { return grid[r][c]; }
    public void  setPiece(int r, int c, Piece p) { grid[r][c] = p; }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public void applyMove(Move m) {
        Piece p = grid[m.fromRow][m.fromCol];
        grid[m.toRow][m.toCol]     = p;
        grid[m.fromRow][m.fromCol] = null;

        if (m.isJump) {
            int midR = (m.fromRow + m.toRow) / 2;
            int midC = (m.fromCol + m.toCol) / 2;
            grid[midR][midC] = null;
        }

        if (p.getColor() == Piece.Color.RED   && m.toRow == 0)        p.makeKing();
        if (p.getColor() == Piece.Color.BLACK  && m.toRow == SIZE - 1) p.makeKing();
    }
}
