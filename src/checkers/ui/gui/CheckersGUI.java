package checkers.ui.gui;

import javax.swing.*;
import java.awt.*;

public class CheckersGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    public CheckersGUI() {
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Use CardLayout to switch between menu and game
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        menuPanel = new MenuPanel(this);
        
        // Add panels to card layout
        mainPanel.add(menuPanel, "MENU");
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void startNewGame(String player1Name, String player2Name) {
        // Create new game panel
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
        cardLayout.show(mainPanel, "MENU");
        pack();
        setLocationRelativeTo(null);
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
