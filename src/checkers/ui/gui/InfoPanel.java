package checkers.ui.gui;

import checkers.logic.CheckersGame;
import checkers.logic.GameState;
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

    // Timer components
    private JLabel turnTimerLabel;   // 10-sec countdown per turn
    private JLabel matchTimerLabel;  // total match clock (counts up)
    private PieceIcon pieceIcon;     // animated piece showing whose turn

    private javax.swing.Timer turnTimer;
    private javax.swing.Timer matchTimer;
    private int turnSecondsLeft = 10;
    private int matchSecondsElapsed = 0;

    // Color scheme
    private static final Color BG_COLOR      = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR  = new Color(70,  70,  70);
    private static final Color RED_ACCENT    = new Color(220, 70,  70);
    private static final Color BLACK_ACCENT  = new Color(50,  50,  50);
    private static final Color WARN_COLOR    = new Color(220, 100, 30);  // orange when ≤3 s

    // ── tiny component that draws a checker piece ──────────────────────────
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
    // ──────────────────────────────────────────────────────────────────────

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
        int y = 20;

        // ── Title ──
        JLabel titleLabel = new JLabel("GAME INFO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBounds(20, y, 220, 30);
        add(titleLabel);

        addSep(y + 35);
        y += 50;

        // ── Players ──
        player1Label = new JLabel();
        player1Label.setFont(new Font("Arial", Font.BOLD, 16));
        player1Label.setForeground(RED_ACCENT);
        player1Label.setBounds(20, y, 220, 25);
        add(player1Label);

        JLabel p1Color = new JLabel("• Red Pieces");
        p1Color.setFont(new Font("Arial", Font.PLAIN, 14));
        p1Color.setForeground(new Color(120, 120, 120));
        p1Color.setBounds(20, y + 25, 220, 20);
        add(p1Color);
        y += 55;

        player2Label = new JLabel();
        player2Label.setFont(new Font("Arial", Font.BOLD, 16));
        player2Label.setForeground(BLACK_ACCENT);
        player2Label.setBounds(20, y, 220, 25);
        add(player2Label);

        JLabel p2Color = new JLabel("• Black Pieces");
        p2Color.setFont(new Font("Arial", Font.PLAIN, 14));
        p2Color.setForeground(new Color(120, 120, 120));
        p2Color.setBounds(20, y + 25, 220, 20);
        add(p2Color);
        y += 60;

        addSep(y);
        y += 15;

        // ── Current Turn header ──
        JLabel turnTitle = new JLabel("Current Turn:");
        turnTitle.setFont(new Font("Arial", Font.BOLD, 14));
        turnTitle.setForeground(ACCENT_COLOR);
        turnTitle.setBounds(20, y, 220, 20);
        add(turnTitle);
        y += 25;

        // piece icon + player name side-by-side
        pieceIcon = new PieceIcon();
        pieceIcon.setBounds(20, y, 36, 36);

        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setBounds(64, y + 6, 176, 25);
        add(turnLabel);   // added first → renders behind

        add(pieceIcon);   // added last  → renders in front
        y += 45;

        // ── Turn timer (10-sec countdown) ──
        JLabel turnTimerTitle = new JLabel("Time Left:");
        turnTimerTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        turnTimerTitle.setForeground(new Color(120, 120, 120));
        turnTimerTitle.setBounds(20, y, 100, 18);
        add(turnTimerTitle);

        turnTimerLabel = new JLabel("10s");
        turnTimerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        turnTimerLabel.setForeground(new Color(0, 150, 0));
        turnTimerLabel.setBounds(20, y + 18, 100, 30);
        add(turnTimerLabel);

        // ── Match timer (total elapsed) ──
        JLabel matchTimerTitle = new JLabel("Match Time:");
        matchTimerTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        matchTimerTitle.setForeground(new Color(120, 120, 120));
        matchTimerTitle.setBounds(130, y, 110, 18);
        add(matchTimerTitle);

        matchTimerLabel = new JLabel("0:00");
        matchTimerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        matchTimerLabel.setForeground(ACCENT_COLOR);
        matchTimerLabel.setBounds(130, y + 18, 110, 30);
        add(matchTimerLabel);
        y += 65;

        addSep(y);
        y += 10;

        // ── Game status ──
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setBounds(20, y, 220, 60);
        statusLabel.setVerticalAlignment(SwingConstants.TOP);
        add(statusLabel);
        y += 70;

    }

    // ── Timer logic ───────────────────────────────────────────────────────

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
                // time up — make a random valid move for the current player
                makeRandomMove();
            }
        });
        turnTimer.start();

        // Match elapsed — fires every second
        matchTimer = new javax.swing.Timer(1000, e -> {
            if (!game.getEngine().getState().isOver()) {
                matchSecondsElapsed++;
                int m = matchSecondsElapsed / 60;
                int s = matchSecondsElapsed % 60;
                matchTimerLabel.setText(m + ":" + String.format("%02d", s));
            }
        });
        matchTimer.start();
    }

    /**
     * When the turn timer expires, pick a random valid Move for the current
     * player, apply it directly via the engine, then refresh the UI.
     */
    private void makeRandomMove() {
        List<Move> validMoves = game.getEngine().getValidMoves();
        if (validMoves == null || validMoves.isEmpty()) {
            parent.onTurnTimeout();   // no moves → game should be over
            return;
        }
        Move randomMove = validMoves.get(new Random().nextInt(validMoves.size()));
        game.getEngine().applyMove(randomMove);
        resetTurnTimer();
        updateInfo();
        parent.repaint();
    }

    /** Call this whenever a move is made (turn changed) to reset the countdown. */
    public void resetTurnTimer() {
        turnSecondsLeft = 10;
        refreshTurnTimerLabel();
    }

    public void stopTimers() {
        if (turnTimer  != null) turnTimer.stop();
        if (matchTimer != null) matchTimer.stop();
    }

    private void refreshTurnTimerLabel() {
        turnTimerLabel.setText(turnSecondsLeft + "s");
        if (turnSecondsLeft <= 3) {
            turnTimerLabel.setForeground(WARN_COLOR);   // orange warning
        } else if (turnSecondsLeft <= 6) {
            turnTimerLabel.setForeground(new Color(200, 160, 0)); // yellow
        } else {
            turnTimerLabel.setForeground(new Color(0, 150, 0));   // green
        }
    }

    // ── Public update called by GamePanel after every move ────────────────

    public void updateInfo() {
        player1Label.setText(game.getP1().getName());
        player2Label.setText(game.getP2().getName());

        Piece.Color currentTurn = game.getEngine().getCurrentTurn();

        // Update piece icon + player name
        pieceIcon.setColor(currentTurn);
        if (currentTurn == Piece.Color.RED) {
            turnLabel.setText(game.getP1().getName());
            turnLabel.setForeground(RED_ACCENT);
        } else {
            turnLabel.setText(game.getP2().getName());
            turnLabel.setForeground(BLACK_ACCENT);
        }

        // Game status
        GameState.Status status = game.getEngine().getState().getStatus();
        if (status == GameState.Status.ONGOING) {
            statusLabel.setText("");
        } else if (status == GameState.Status.RED_WINS) {
            statusLabel.setText("<html><div style='text-align:center;'>🏆<br/>"
                    + game.getP1().getName() + "<br/>WINS!</div></html>");
            statusLabel.setForeground(RED_ACCENT);
            stopTimers();
        } else if (status == GameState.Status.BLACK_WINS) {
            statusLabel.setText("<html><div style='text-align:center;'>🏆<br/>"
                    + game.getP2().getName() + "<br/>WINS!</div></html>");
            statusLabel.setForeground(BLACK_ACCENT);
            stopTimers();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void addSep(int y) {
        JSeparator sep = new JSeparator();
        sep.setBounds(20, y, 220, 2);
        sep.setForeground(new Color(200, 200, 200));
        add(sep);
    }
}