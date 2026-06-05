package checkers.ui.gui;

import checkers.data.DataStore;
import checkers.data.MatchRecord;
import checkers.data.PlayerStats;
import checkers.logic.CheckersGame;
import checkers.logic.GameState;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckersGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private StatsPanel statsPanel;

    // Logged-in account username (null = guest)
    private String currentUser = null;

    public CheckersGUI() {
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Start at the login / guest screen
        loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, "LOGIN");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Account flow ────────────────────────────────────────

    /** Called by LoginPanel once a user logs in or chooses guest. */
    public void enterMenu(String username) {
        this.currentUser = username; // null for guest
        showMenu();
    }

    public String getCurrentUser() { return currentUser; }
    public boolean isLoggedIn()    { return currentUser != null; }

    private void showMenu() {
        if (menuPanel != null) {
            mainPanel.remove(menuPanel);
        }
        menuPanel = new MenuPanel(this, currentUser);
        mainPanel.add(menuPanel, "MENU");
        cardLayout.show(mainPanel, "MENU");
        pack();
        setLocationRelativeTo(null);
    }

    public void logout() {
        currentUser = null;
        cardLayout.show(mainPanel, "LOGIN");
        pack();
        setLocationRelativeTo(null);
    }

    // ── Stats screen ───────────────────────────────────────

    public void showStats() {
        if (statsPanel != null) {
            mainPanel.remove(statsPanel);
        }
        statsPanel = new StatsPanel(this, currentUser);
        mainPanel.add(statsPanel, "STATS");
        cardLayout.show(mainPanel, "STATS");
        pack();
        setLocationRelativeTo(null);
    }

    public void backToMenuFromStats() {
        cardLayout.show(mainPanel, "MENU");
        pack();
        setLocationRelativeTo(null);
    }

    // ── Game flow ─────────────────────────────────────────

    public void startNewGame(String player1Name, String player2Name) {
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }
        gamePanel = new GamePanel(this, player1Name, player2Name);
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
        pack();
        setLocationRelativeTo(null);
    }

    public void returnToMenu() {
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
            gamePanel = null;
        }
        showMenu();
    }

    // ── Statistics recording ───────────────────────────────────

    /** Persist match history and update player profiles when a game ends. */
    public void recordResult(CheckersGame game, InfoPanel info) {
        DataStore ds = DataStore.get();

        String redName   = game.getP1().getName();   // Player 1 = RED
        String blackName = game.getP2().getName();    // Player 2 = BLACK
        GameState.Status status = game.getEngine().getState().getStatus();
        int matchSeconds = info.getMatchSeconds();

        String winner;
        if (status == GameState.Status.RED_WINS)        winner = redName;
        else if (status == GameState.Status.BLACK_WINS) winner = blackName;
        else                                            winner = "Draw";

        // Match history (always recorded)
        String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        ds.addMatch(new MatchRecord(dt, redName, blackName, winner, matchSeconds));

        // Player 1 stats → logged-in account (if any)
        PlayerStats redProfile = (currentUser != null) ? ds.getProfile(currentUser) : null;
        if (redProfile != null) {
            String res = (status == GameState.Status.RED_WINS) ? "WIN"
                       : (status == GameState.Status.BLACK_WINS) ? "LOSS" : "DRAW";
            redProfile.recordGame(res, matchSeconds, info.getRedTurns(), info.getRedTurnSeconds());
            ds.saveProfile(redProfile);
        }

        // Player 2 stats → only if a profile with that name already exists
        PlayerStats blackProfile = ds.getProfile(blackName);
        if (blackProfile != null && blackProfile != redProfile) {
            String res = (status == GameState.Status.BLACK_WINS) ? "WIN"
                       : (status == GameState.Status.RED_WINS) ? "LOSS" : "DRAW";
            blackProfile.recordGame(res, matchSeconds, info.getBlackTurns(), info.getBlackTurnSeconds());
            ds.saveProfile(blackProfile);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new CheckersGUI());
    }
}
