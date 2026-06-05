package checkers.ui.gui;

import checkers.data.DataStore;
import checkers.data.MatchRecord;
import checkers.data.PlayerStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Displays the logged-in player's profile stats and recent match history.
// Uses the same minimalistic color scheme as the rest of the UI.
public class StatsPanel extends JPanel {
    private CheckersGUI parent;
    private String username;  // null = guest (shouldn't normally open this screen)

    private static final Color BG_COLOR     = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(70, 70, 70);
    private static final Color RED_ACCENT   = new Color(220, 70, 70);
    private static final Color BUTTON_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_HOVER = new Color(80, 80, 80);
    private static final Color DIVIDER      = new Color(200, 200, 200);
    private static final Color GRAY_TEXT    = new Color(120, 120, 120);

    public StatsPanel(CheckersGUI parent, String username) {
        this.parent   = parent;
        this.username = username;
        setPreferredSize(new Dimension(600, 700));
        setBackground(BG_COLOR);
        setLayout(null);
        initComponents();
    }

    private void initComponents() {
        // ── Title ──
        JLabel titleLabel = new JLabel("PLAYER STATS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBounds(0, 35, 600, 45);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        String nameText = (username != null) ? username : "Guest";
        JLabel nameLabel = new JLabel(nameText);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setForeground(GRAY_TEXT);
        nameLabel.setBounds(0, 82, 600, 22);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel);

        addSep(115);

        PlayerStats s = (username != null) ? DataStore.get().getProfile(username) : null;

        if (s == null) {
            JLabel noData = new JLabel("No profile data found.");
            noData.setFont(new Font("Arial", Font.PLAIN, 16));
            noData.setForeground(GRAY_TEXT);
            noData.setBounds(0, 140, 600, 25);
            noData.setHorizontalAlignment(SwingConstants.CENTER);
            add(noData);
        } else {
            // ── Stats grid ──
            int y = 130;
            int col1x = 60, col2x = 320;
            int colW = 240, rH = 24;

            y = addStatRow("Games Played",   String.valueOf(s.gamesPlayed),     col1x, col2x, y, colW, rH);
            y = addStatRow("Wins",            String.valueOf(s.wins),            col1x, col2x, y, colW, rH);
            y = addStatRow("Losses",          String.valueOf(s.losses),          col1x, col2x, y, colW, rH);
            y = addStatRow("Draws",           String.valueOf(s.draws),           col1x, col2x, y, colW, rH);
            y += 8;
            addSep(y); y += 14;
            y = addStatRow("Total Play Time", formatTime((int) s.totalPlayTime), col1x, col2x, y, colW, rH);
            y = addStatRow("Avg Turn Time",   String.format("%.1fs", s.averageTurnTime()),
                           col1x, col2x, y, colW, rH);
            y = addStatRow("Fastest Win",
                           s.fastestWin > 0 ? formatTime(s.fastestWin) : "N/A",
                           col1x, col2x, y, colW, rH);
            y = addStatRow("Longest Game",    formatTime(s.longestGame),         col1x, col2x, y, colW, rH);

            addSep(y + 8);
            y += 24;
        }

        // ── Recent match history ──
        int hy = (s == null) ? 180 : 376;

        JLabel histTitle = new JLabel("Recent Matches");
        histTitle.setFont(new Font("Arial", Font.BOLD, 18));
        histTitle.setForeground(ACCENT_COLOR);
        histTitle.setBounds(60, hy, 480, 24);
        add(histTitle);
        hy += 30;

        List<MatchRecord> history = DataStore.get().matchesFor(username, 5);
        if (history.isEmpty()) {
            JLabel noHist = new JLabel("No matches played yet.");
            noHist.setFont(new Font("Arial", Font.PLAIN, 14));
            noHist.setForeground(GRAY_TEXT);
            noHist.setBounds(60, hy, 480, 22);
            add(noHist);
            hy += 22;
        } else {
            // Header row
            addHistRow("Date", "Players", "Winner", "Time", hy, true);
            hy += 22;
            for (MatchRecord r : history) {
                String players = r.player1 + " vs " + r.player2;
                addHistRow(r.dateTime, players, r.winner, r.durationString(), hy, false);
                hy += 22;
            }
        }

        // ── Back button ──
        JButton backButton = createButton("BACK TO MENU");
        backButton.setBounds(200, 648, 200, 38);
        backButton.addActionListener(e -> parent.backToMenuFromStats());
        add(backButton);
    }

    private int addStatRow(String label, String value, int col1x, int col2x,
                           int y, int colW, int rH) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));
        lbl.setForeground(GRAY_TEXT);
        lbl.setBounds(col1x, y, colW, rH);
        add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.BOLD, 15));
        val.setForeground(ACCENT_COLOR);
        val.setBounds(col2x, y, colW, rH);
        add(val);

        return y + rH + 4;
    }

    private void addHistRow(String date, String players, String winner,
                            String time, int y, boolean header) {
        Font f = header
                ? new Font("Arial", Font.BOLD, 12)
                : new Font("Arial", Font.PLAIN, 12);
        Color c = header ? ACCENT_COLOR : GRAY_TEXT;

        addCell(date,    60,  y, 120, 20, f, c);
        addCell(players, 185, y, 190, 20, f, c);
        addCell(winner,  380, y, 110, 20, f, c);
        addCell(time,    500, y,  60, 20, f, c);
    }

    private void addCell(String text, int x, int y, int w, int h, Font f, Color c) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(f);
        lbl.setForeground(c);
        lbl.setBounds(x, y, w, h);
        add(lbl);
    }

    private void addSep(int y) {
        JSeparator sep = new JSeparator();
        sep.setBounds(60, y, 480, 2);
        sep.setForeground(DIVIDER);
        add(sep);
    }

    private static String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return m + ":" + String.format("%02d", s);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
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
}
