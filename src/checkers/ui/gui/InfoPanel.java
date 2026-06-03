package checkers.ui.gui;

import checkers.logic.CheckersGame;
import checkers.logic.GameState;
import checkers.model.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfoPanel extends JPanel {
    private CheckersGame game;
    private GamePanel parent;
    private JLabel turnLabel;
    private JLabel statusLabel;
    private JLabel player1Label;
    private JLabel player2Label;
    private JButton menuButton;
    private JButton newGameButton;
    
    // Color scheme
    private static final Color BG_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(70, 70, 70);
    private static final Color RED_ACCENT = new Color(220, 70, 70);
    private static final Color BLACK_ACCENT = new Color(50, 50, 50);
    private static final Color BUTTON_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_HOVER = new Color(80, 80, 80);
    
    public InfoPanel(CheckersGame game, GamePanel parent){
        this.game = game;
        this.parent = parent;
        
        setPreferredSize(new Dimension(260, 600));
        setBackground(BG_COLOR);
        setLayout(null);
        
        initComponents();
        updateInfo();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("GAME INFO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBounds(20, 20, 220, 30);
        add(titleLabel);
        
        // Divider
        JSeparator sep1 = new JSeparator();
        sep1.setBounds(20, 55, 220, 2);
        sep1.setForeground(new Color(200, 200, 200));
        add(sep1);
        
        // Player 1 info
        player1Label = new JLabel();
        player1Label.setFont(new Font("Arial", Font.BOLD, 16));
        player1Label.setForeground(RED_ACCENT);
        player1Label.setBounds(20, 80, 220, 25);
        add(player1Label);
        
        JLabel p1ColorLabel = new JLabel("• Red Pieces");
        p1ColorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        p1ColorLabel.setForeground(new Color(120, 120, 120));
        p1ColorLabel.setBounds(20, 105, 220, 20);
        add(p1ColorLabel);
        
        // Player 2 info
        player2Label = new JLabel();
        player2Label.setFont(new Font("Arial", Font.BOLD, 16));
        player2Label.setForeground(BLACK_ACCENT);
        player2Label.setBounds(20, 150, 220, 25);
        add(player2Label);
        
        JLabel p2ColorLabel = new JLabel("• Black Pieces");
        p2ColorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        p2ColorLabel.setForeground(new Color(120, 120, 120));
        p2ColorLabel.setBounds(20, 175, 220, 20);
        add(p2ColorLabel);
        
        // Divider
        JSeparator sep2 = new JSeparator();
        sep2.setBounds(20, 220, 220, 2);
        sep2.setForeground(new Color(200, 200, 200));
        add(sep2);
        
        // Current turn
        JLabel turnTitleLabel = new JLabel("Current Turn:");
        turnTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        turnTitleLabel.setForeground(ACCENT_COLOR);
        turnTitleLabel.setBounds(20, 245, 220, 20);
        add(turnTitleLabel);
        
        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setBounds(20, 270, 220, 25);
        add(turnLabel);
        
        // Game status
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setBounds(20, 310, 220, 60);
        statusLabel.setVerticalAlignment(SwingConstants.TOP);
        add(statusLabel);
        
        // New Game button
        newGameButton = createStyledButton("NEW GAME");
        newGameButton.setBounds(30, 435, 200, 40);
        newGameButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Start a new game with the same players?",
                "New Game",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                parent.returnToMenu();
            }
        });
        add(newGameButton);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        
        return button;
    }
    
    public void updateInfo(){
        
        player1Label.setText(game.getP1().getName());
        player2Label.setText(game.getP2().getName());
        
        // Update current turn
        Piece.Color currentTurn = game.getEngine().getCurrentTurn();
        if (currentTurn == Piece.Color.RED) {
            turnLabel.setText(game.getP1().getName());
            turnLabel.setForeground(RED_ACCENT);
        } else {
            turnLabel.setText(game.getP2().getName());
            turnLabel.setForeground(BLACK_ACCENT);
        }
        
        // Update game status
        GameState.Status status = game.getEngine().getState().getStatus();
        if (status == GameState.Status.ONGOING){
            statusLabel.setText("");
        } else if (status == GameState.Status.RED_WINS){
            statusLabel.setText("<html><div style='text-align: center;'>🏆<br/>" + 
                              game.getP1().getName() + "<br/>WINS!</div></html>");
            statusLabel.setForeground(RED_ACCENT);
        } else if (status == GameState.Status.BLACK_WINS){
            statusLabel.setText("<html><div style='text-align: center;'>🏆<br/>" + 
                              game.getP2().getName() + "<br/>WINS!</div></html>");
            statusLabel.setForeground(BLACK_ACCENT);
        }
    }
}
