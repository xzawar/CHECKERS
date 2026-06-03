package checkers.logic;

import checkers.model.*;

// Simple wrapper that creates the engine and two players.
// UI classes talk to this instead of directly to GameEngine.
public class CheckersGame {
    private GameEngine engine;
    private Player p1, p2;

    public CheckersGame(String name1, String name2) {
        engine = new GameEngine();
        p1 = new Player(name1, Piece.Color.RED);
        p2 = new Player(name2, Piece.Color.BLACK);
    }

    public GameEngine getEngine() { return engine; }
    public Player getP1()         { return p1; }
    public Player getP2()         { return p2; }

    // Returns whichever player's turn it currently is
    public Player currentPlayer() {
        return engine.getCurrentTurn() == Piece.Color.RED ? p1 : p2;
    }
}
