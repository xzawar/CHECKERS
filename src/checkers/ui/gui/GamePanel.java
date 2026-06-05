package checkers.ui.gui;

import checkers.logic.CheckersGame;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private CheckersGUI parent;
    private CheckersGame game;
    private BoardPanel boardPanel;
    private InfoPanel infoPanel;

    // Dual-screen mirror (optional)
    private SecondScreenFrame mirrorFrame;
    private BoardPanel mirrorBoard;

    // Color scheme
    private static final Color BG_COLOR = new Color(250, 250, 250);

    public GamePanel(CheckersGUI parent, String player1Name, String player2Name) {
        this.parent = parent;
        this.game = new CheckersGame(player1Name, player2Name);

        setPreferredSize(new Dimension(800, 700));
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(20, 20));

        // Add padding around the edges
        setBorder(BorderFactory.createEmptyBorder(70, 20, 20, 20));

        // Create board panel
        boardPanel = new BoardPanel(game, this);
        add(boardPanel, BorderLayout.CENTER);

        // Create info panel
        infoPanel = new InfoPanel(game, this);
        add(infoPanel, BorderLayout.EAST);
    }

    public CheckersGame getGame() { return game; }
    public InfoPanel getInfoPanel() { return infoPanel; }

    // ── Refresh helpers ───────────────────────────────────────

    private void refreshAll(boolean resetTimer) {
        if (resetTimer) infoPanel.resetTurnTimer();
        infoPanel.updateInfo();
        boardPanel.repaint();
        if (mirrorBoard != null) mirrorBoard.repaint();
        if (mirrorFrame != null) mirrorFrame.refresh();
    }

    /** Called after a real move — resets the per-turn countdown. */
    public void updateDisplay() { refreshAll(true); }

    /** Refresh views without touching the turn timer (e.g. after Offer Draw). */
    public void refreshViews() { refreshAll(false); }

    public void onTurnTimeout() { refreshAll(false); }

    // ── Navigation ─────────────────────────────────────────

    public void returnToMenu() {
        cleanup();
        parent.returnToMenu();
    }

    public void newGame() {
        String p1 = game.getP1().getName();
        String p2 = game.getP2().getName();
        cleanup();
        parent.startNewGame(p1, p2);
    }

    private void cleanup() {
        infoPanel.stopTimers();
        closeDualScreen();
    }

    // ── Statistics ─────────────────────────────────────────

    /** Called once when the game reaches a terminal state. */
    public void onGameOver() {
        parent.recordResult(game, infoPanel);
    }

    // ── Dual-screen multiplayer ─────────────────────────────────

    public void toggleDualScreen() {
        if (mirrorFrame != null) {
            closeDualScreen();
            return;
        }
        // Flipped board so the second player reads it from their own screen.
        mirrorBoard = new BoardPanel(game, this, true);
        mirrorFrame = new SecondScreenFrame(this, mirrorBoard);
        mirrorFrame.showOnSecondScreen();
        refreshViews();
    }

    private void closeDualScreen() {
        if (mirrorFrame != null) {
            mirrorFrame.dispose();
            mirrorFrame = null;
            mirrorBoard = null;
        }
    }
}
