package checkers.ui.gui;

import checkers.logic.CheckersGame;
import checkers.logic.GameState;
import checkers.model.Board;
import checkers.model.Move;
import checkers.model.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class InfoPanel extends JPanel {
    private CheckersGame game;
    private GamePanel parent;

    private JLabel turnLabel;
    private JLabel statusLabel;
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel p1MetaLabel;   // "• Red Pieces: 6"
    private JLabel p2MetaLabel;   // "• Black Pieces: 6"
    private JLabel p1TimeLabel;   // "Total: 0:00"
    private JLabel p2TimeLabel;   // "Total: 0:00"

    // Timer components
    private JLabel turnTimerLabel;   // per-turn countdown
    private JLabel matchTimerLabel;  // total match clock (counts up)
    private PieceIcon pieceIcon;     // animated piece showing whose turn

    private javax.swing.Timer turnTimer;
    private javax.swing.Timer matchTimer;
    private int turnSecondsLeft = 10;
    private int matchSecondsElapsed = 0;

    private static final int TURN_LIMIT = 10;

    // Per-player total game timers
    private int redTotalSeconds = 0;
    private int blackTotalSeconds = 0;

    // Turn-time accounting (for average-turn-time statistics)
    private int turnElapsed = 0;                 // seconds spent on the live turn
    private Piece.Color activeColor = Piece.Color.RED; // whose turn the clock counts
    private int redTurns = 0,  redTurnSeconds = 0;
    private int blackTurns = 0, blackTurnSeconds = 0;

    private boolean recorded = false; // ensures stats are saved only once

    // Color scheme
    private static final Color BG_COLOR      = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR  = new Color(70,  70,  70);
    private static final Color RED_ACCENT    = new Color(220, 70,  70);
    private static final Color BLACK_ACCENT  = new Color(50,  50,  50);
    private static final Color WARN_COLOR    = new Color(220, 100, 30);  // orange when ≤3 s
    private static final Color BUTTON_COLOR  = new Color(50, 50, 50);
    private static final Color BUTTON_HOVER  = new Color(80, 80, 80);

    // ── tiny component that draws a checker piece ─────────────────────
    private static class PieceIcon extends JComponent {
        private Piece.Color color = Piece.Color.RED;

        PieceIcon() {
            setPreferredSize(new Dimension(36, 36));
            setSize(36, 36);
        }

        void setColor(Piece.Color c) { color = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int pad = 3;

            // shadow
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(pad + 2, pad + 2, w - pad * 2, h - pad * 2);

            // outer ring
            Color outer = (color == Piece.Color.RED) ? new Color(220, 70, 70) : new Color(50, 50, 50);
            Color inner = (color == Piece.Color.RED) ? new Color(180, 50, 50) : new Color(30, 30, 30);

            g2.setColor(outer);
            g2.fillOval(pad, pad, w - pad * 2, h - pad * 2);

            // inner circle
            int ipad = pad + 5;
            g2.setColor(inner);
            g2.fillOval(ipad, ipad, w - ipad * 2, h - ipad * 2);

            // highlight gloss
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillOval(pad + 4, pad + 3, (w - pad * 2) / 2, (h - pad * 2) / 3);
        }
    }
    // ───────────────────────────────────────────────────

    public InfoPanel(CheckersGame game, GamePanel parent) {
        this.game   = game;
        this.parent = parent;

        setPreferredSize(new Dimension(260, 650));
        setBackground(BG_COLOR);
        setLayout(null);

        initComponents();
        startTimers();
        updateInfo();
    }

    private void initComponents() {
        // ── Title ──
        JLabel titleLabel = new JLabel("GAME INFO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBounds(20, 20, 220, 30);
        add(titleLabel);
        addSep(55);

        // ── Player 1 (Red) ──
        player1Label = new JLabel();
        player1Label.setFont(new Font("Arial", Font.BOLD, 16));
        player1Label.setForeground(RED_ACCENT);
        player1Label.setBounds(20, 68, 220, 25);
        add(player1Label);

        p1MetaLabel = new JLabel("\u2022 Red Pieces: 6");
        p1MetaLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        p1MetaLabel.setForeground(new Color(120, 120, 120));
        p1MetaLabel.setBounds(20, 92, 220, 18);
        add(p1MetaLabel);

        p1TimeLabel = new JLabel("Total time: 0:00");
        p1TimeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        p1TimeLabel.setForeground(new Color(120, 120, 120));
        p1TimeLabel.setBounds(20, 110, 220, 18);
        add(p1TimeLabel);

        // ── Player 2 (Black) ──
        player2Label = new JLabel();
        player2Label.setFont(new Font("Arial", Font.BOLD, 16));
        player2Label.setForeground(BLACK_ACCENT);
        player2Label.setBounds(20, 138, 220, 25);
        add(player2Label);

        p2MetaLabel = new JLabel("\u2022 Black Pieces: 6");
        p2MetaLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        p2MetaLabel.setForeground(new Color(120, 120, 120));
        p2MetaLabel.setBounds(20, 162, 220, 18);
        add(p2MetaLabel);

        p2TimeLabel = new JLabel("Total time: 0:00");
        p2TimeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        p2TimeLabel.setForeground(new Color(120, 120, 120));
        p2TimeLabel.setBounds(20, 180, 220, 18);
        add(p2TimeLabel);

        addSep(208);

        // ── Current Turn header ──
        JLabel turnTitle = new JLabel("Current Turn:");
        turnTitle.setFont(new Font("Arial", Font.BOLD, 14));
        turnTitle.setForeground(ACCENT_COLOR);
        turnTitle.setBounds(20, 218, 220, 20);
        add(turnTitle);

        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setBounds(20, 244, 176, 25);
        add(turnLabel);

        pieceIcon = new PieceIcon();
        pieceIcon.setBounds(202, 240, 36, 36);
        add(pieceIcon);

        // ── Turn timer (countdown) ──
        JLabel turnTimerTitle = new JLabel("Time Left:");
        turnTimerTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        turnTimerTitle.setForeground(new Color(120, 120, 120));
        turnTimerTitle.setBounds(20, 284, 100, 18);
        add(turnTimerTitle);

        turnTimerLabel = new JLabel(TURN_LIMIT + "s");
        turnTimerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        turnTimerLabel.setForeground(new Color(0, 150, 0));
        turnTimerLabel.setBounds(20, 302, 100, 30);
        add(turnTimerLabel);

        // ── Match timer (total elapsed) ──
        JLabel matchTimerTitle = new JLabel("Match Time:");
        matchTimerTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        matchTimerTitle.setForeground(new Color(120, 120, 120));
        matchTimerTitle.setBounds(130, 284, 110, 18);
        add(matchTimerTitle);

        matchTimerLabel = new JLabel("0:00");
        matchTimerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        matchTimerLabel.setForeground(ACCENT_COLOR);
        matchTimerLabel.setBounds(130, 302, 110, 30);
        add(matchTimerLabel);

        addSep(340);

        // ── Game status ──
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setBounds(20, 350, 220, 56);
        statusLabel.setVerticalAlignment(SwingConstants.TOP);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel);

        // ── Control buttons ──
        JButton drawButton = createButton("OFFER DRAW");
        drawButton.setBounds(20, 414, 220, 32);
        drawButton.addActionListener(e -> offerDraw());
        add(drawButton);

        JButton dualButton = createButton("DUAL SCREEN");
        dualButton.setBounds(20, 452, 220, 32);
        dualButton.addActionListener(e -> parent.toggleDualScreen());
        add(dualButton);

        JButton newGameButton = createButton("NEW GAME");
        newGameButton.setBounds(20, 490, 220, 32);
        newGameButton.addActionListener(e -> parent.newGame());
        add(newGameButton);

        JButton menuButton = createButton("MAIN MENU");
        menuButton.setBounds(20, 528, 220, 32);
        menuButton.addActionListener(e -> parent.returnToMenu());
        add(menuButton);
    }

    // ── Timer logic ───────────────────────────────────────────

    private void startTimers() {
        // Turn countdown — fires every second
        turnTimer = new javax.swing.Timer(1000, e -> {
            if (game.getEngine().getState().isOver()) {
                stopTimers();
                return;
            }
            turnSecondsLeft--;
            refreshTurnTimerLabel();
            if (turnSecondsLeft <= 0) {
                makeRandomMove();   // time up — auto-move for the current player
            }
        });
        turnTimer.start();

        // Match elapsed + per-player totals — fires every second
        matchTimer = new javax.swing.Timer(1000, e -> {
            if (game.getEngine().getState().isOver()) return;

            matchSecondsElapsed++;
            turnElapsed++;
            if (game.getEngine().getCurrentTurn() == Piece.Color.RED) {
                redTotalSeconds++;
            } else {
                blackTotalSeconds++;
            }
            matchTimerLabel.setText(formatTime(matchSecondsElapsed));
            p1TimeLabel.setText("Total time: " + formatTime(redTotalSeconds));
            p2TimeLabel.setText("Total time: " + formatTime(blackTotalSeconds));
        });
        matchTimer.start();
    }

    /**
     * When the turn timer expires, pick a random valid Move for the current
     * player, apply it, then refresh every view through the GamePanel.
     */
    private void makeRandomMove() {
        List<Move> validMoves = game.getEngine().getValidMoves();
        if (validMoves == null || validMoves.isEmpty()) {
            parent.onTurnTimeout();   // no moves → game should be over
            return;
        }
        Move randomMove = validMoves.get(new Random().nextInt(validMoves.size()));
        game.getEngine().applyMove(randomMove);
        parent.updateDisplay();       // resets turn timer, updates info, repaints all
    }

    /** Called whenever a move is made (turn changed) to reset the countdown. */
    public void resetTurnTimer() {
        // Record the turn that just finished for average-turn-time stats.
        if (activeColor == Piece.Color.RED) {
            redTurns++;
            redTurnSeconds += turnElapsed;
        } else {
            blackTurns++;
            blackTurnSeconds += turnElapsed;
        }
        // The next turn belongs to whoever is now on the clock.
        activeColor = game.getEngine().getCurrentTurn();
        turnElapsed = 0;
        turnSecondsLeft = TURN_LIMIT;
        refreshTurnTimerLabel();
    }

    public void stopTimers() {
        if (turnTimer  != null) turnTimer.stop();
        if (matchTimer != null) matchTimer.stop();
    }

    private void refreshTurnTimerLabel() {
        turnTimerLabel.setText(turnSecondsLeft + "s");
        if (turnSecondsLeft <= 3) {
            turnTimerLabel.setForeground(WARN_COLOR);
        } else if (turnSecondsLeft <= 6) {
            turnTimerLabel.setForeground(new Color(200, 160, 0));
        } else {
            turnTimerLabel.setForeground(new Color(0, 150, 0));
        }
    }

    private void offerDraw() {
        if (game.getEngine().getState().isOver()) return;
        game.getEngine().declareDraw();
        updateInfo();
        parent.refreshViews();
    }

    // ── Public update called by GamePanel after every move ──────────────────

    public void updateInfo() {
        player1Label.setText(game.getP1().getName());
        player2Label.setText(game.getP2().getName());

        Board board = game.getEngine().getBoard();
        p1MetaLabel.setText("\u2022 Red Pieces: "  + board.countPieces(Piece.Color.RED));
        p2MetaLabel.setText("\u2022 Black Pieces: " + board.countPieces(Piece.Color.BLACK));

        Piece.Color currentTurn = game.getEngine().getCurrentTurn();
        pieceIcon.setColor(currentTurn);
        if (currentTurn == Piece.Color.RED) {
            turnLabel.setText(game.getP1().getName());
            turnLabel.setForeground(RED_ACCENT);
        } else {
            turnLabel.setText(game.getP2().getName());
            turnLabel.setForeground(BLACK_ACCENT);
        }

        GameState.Status status = game.getEngine().getState().getStatus();
        if (status == GameState.Status.ONGOING) {
            statusLabel.setForeground(new Color(120, 120, 120));
            statusLabel.setText("Status: In Progress");
        } else if (status == GameState.Status.RED_WINS) {
            statusLabel.setForeground(RED_ACCENT);
            statusLabel.setText("<html><div style='text-align:center;'>\uD83C\uDFC6<br/>"
                    + game.getP1().getName() + "<br/>WINS!</div></html>");
            finishGame();
        } else if (status == GameState.Status.BLACK_WINS) {
            statusLabel.setForeground(BLACK_ACCENT);
            statusLabel.setText("<html><div style='text-align:center;'>\uD83C\uDFC6<br/>"
                    + game.getP2().getName() + "<br/>WINS!</div></html>");
            finishGame();
        } else if (status == GameState.Status.DRAW) {
            statusLabel.setForeground(ACCENT_COLOR);
            statusLabel.setText("<html><div style='text-align:center;'>\uD83E\uDD1D<br/>DRAW</div></html>");
            finishGame();
        }
    }

    private void finishGame() {
        stopTimers();
        if (!recorded) {
            recorded = true;
            parent.onGameOver();
        }
    }

    // ── Getters used for statistics + dual-screen mirror ───────────────────

    public int getMatchSeconds()    { return matchSecondsElapsed; }
    public int getRedTotalSeconds() { return redTotalSeconds; }
    public int getBlackTotalSeconds(){ return blackTotalSeconds; }
    public int getRedTurns()        { return redTurns; }
    public int getRedTurnSeconds()  { return redTurnSeconds; }
    public int getBlackTurns()      { return blackTurns; }
    public int getBlackTurnSeconds(){ return blackTurnSeconds; }
    public int getTurnSecondsLeft() { return turnSecondsLeft; }

    public String getMatchTimeString() { return formatTime(matchSecondsElapsed); }

    public String getCurrentTurnName() {
        return (game.getEngine().getCurrentTurn() == Piece.Color.RED)
                ? game.getP1().getName() : game.getP2().getName();
    }

    // ── Helpers ─────────────────────────────────────────────

    private static String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return m + ":" + String.format("%02d", s);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(BUTTON_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { button.setBackground(BUTTON_COLOR); }
        });
        return button;
    }

    private void addSep(int y) {
        JSeparator sep = new JSeparator();
        sep.setBounds(20, y, 220, 2);
        sep.setForeground(new Color(200, 200, 200));
        add(sep);
    }
}
