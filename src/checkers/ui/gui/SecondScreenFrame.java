package checkers.ui.gui;

import javax.swing.*;
import java.awt.*;

// A second window that mirrors the live game so the opponent can play from
// their own screen. The board it holds shares the same game state, so any
// move on either window stays perfectly in sync.
public class SecondScreenFrame extends JFrame {
    private final GamePanel gamePanel;
    private final BoardPanel mirrorBoard;
    private final JLabel turnLabel;
    private final JLabel timeLabel;

    private static final Color BG_COLOR     = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(70, 70, 70);

    public SecondScreenFrame(GamePanel gamePanel, BoardPanel mirrorBoard) {
        this.gamePanel   = gamePanel;
        this.mirrorBoard = mirrorBoard;

        setTitle("Checkers \u2014 Player 2 View");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(BG_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header showing whose turn + countdown (kept minimal and on-theme)
        JPanel header = new JPanel(new GridLayout(1, 2));
        header.setOpaque(false);

        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setForeground(ACCENT_COLOR);
        header.add(turnLabel);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timeLabel.setForeground(ACCENT_COLOR);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(timeLabel);

        content.add(header, BorderLayout.NORTH);
        content.add(mirrorBoard, BorderLayout.CENTER);

        JLabel hint = new JLabel("Synced with main window \u2022 board oriented for Player 2");
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(new Color(150, 150, 150));
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(hint, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        refresh();
    }

    /** Place the window on a second monitor if one exists, else beside the main window. */
    public void showOnSecondScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        if (screens.length > 1) {
            Rectangle bounds = screens[1].getDefaultConfiguration().getBounds();
            int x = bounds.x + (bounds.width  - getWidth())  / 2;
            int y = bounds.y + (bounds.height - getHeight()) / 2;
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    /** Refresh the mirror's header + board from the shared game state. */
    public void refresh() {
        if (gamePanel == null) return;
        InfoPanel info = gamePanel.getInfoPanel();
        if (info != null) {
            turnLabel.setText("Turn: " + info.getCurrentTurnName());
            timeLabel.setText(info.getTurnSecondsLeft() + "s  \u2022  " + info.getMatchTimeString());
        }
        if (mirrorBoard != null) mirrorBoard.repaint();
    }
}
