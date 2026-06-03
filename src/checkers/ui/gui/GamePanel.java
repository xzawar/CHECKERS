package checkers.ui.gui;

import checkers.logic.CheckersGame;
import checkers.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GamePanel extends JPanel {
    private CheckersGUI parent;
    private CheckersGame game;
    private BoardPanel boardPanel;
    private InfoPanel infoPanel;
    
    // Color scheme
    private static final Color BG_COLOR = new Color(250, 250, 250);
    
    public GamePanel(CheckersGUI parent, String player1Name, String player2Name) {
        this.parent = parent;
        this.game = new CheckersGame(player1Name, player2Name);
        
        setPreferredSize(new Dimension(800, 700));
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(20, 20));
        
        // Add padding around the edges
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create board panel
        boardPanel = new BoardPanel(game, this);
        add(boardPanel, BorderLayout.CENTER);
        
        // Create info panel
        infoPanel = new InfoPanel(game, this);
        add(infoPanel, BorderLayout.EAST);
    }
    
    
    public void returnToMenu() {
        parent.returnToMenu();
    }
    
    public void updateDisplay() {
        boardPanel.repaint();
        infoPanel.updateInfo();
    }
}
