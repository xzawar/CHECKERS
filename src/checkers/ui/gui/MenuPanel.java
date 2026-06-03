package checkers.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {
    private CheckersGUI parent;
    private JTextField player1Field;
    private JTextField player2Field;
    
    // Color scheme - minimalistic and clean
    private static final Color BG_COLOR = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(70, 70, 70);
    private static final Color BUTTON_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_HOVER = new Color(80, 80, 80);
    private static final Color RED_ACCENT = new Color(220, 70, 70);
    private static final Color BLACK_ACCENT = new Color(50, 50, 50);
    
    public MenuPanel(CheckersGUI parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(600, 700));
        setBackground(BG_COLOR);
        setLayout(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("CHECKERS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 52));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBounds(0, 80, 600, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("6x6 Board Game");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setBounds(0, 145, 600, 20);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitleLabel);
        
        // Decorative checkers icons
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Red piece
                g2.setColor(RED_ACCENT);
                g2.fillOval(30, 0, 60, 60);
                g2.setColor(new Color(180, 50, 50));
                g2.fillOval(35, 3, 50, 50);
                
                // Black piece
                g2.setColor(BLACK_ACCENT);
                g2.fillOval(130, 0, 60, 60);
                g2.setColor(new Color(30, 30, 30));
                g2.fillOval(135, 3, 50, 50);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setBounds(200, 190, 200, 60);
        add(iconPanel);
        
        // Player 1 section
        JLabel player1Label = new JLabel("Player 1");
        player1Label.setFont(new Font("Arial", Font.BOLD, 14));
        player1Label.setForeground(RED_ACCENT);
        player1Label.setBounds(150, 280, 300, 25);
        add(player1Label);
        
        player1Field = createStyledTextField("Enter Player 1");
        player1Field.setBounds(150, 310, 300, 45);
        add(player1Field);
        
        // Player 2 section
        JLabel player2Label = new JLabel("Player 2");
        player2Label.setFont(new Font("Arial", Font.BOLD, 14));
        player2Label.setForeground(BLACK_ACCENT);
        player2Label.setBounds(150, 380, 300, 25);
        add(player2Label);
        
        player2Field = createStyledTextField("Enter Player 2 ");
        player2Field.setBounds(150, 410, 300, 45);
        add(player2Field);
        
        // Add Enter key listeners
        player1Field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    player2Field.requestFocus();
                }
            }
        });
        
        player2Field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
            }
        });
        
        // Start button
        JButton startButton = createStyledButton("START GAME");
        startButton.setBounds(200, 510, 200, 50);
        startButton.addActionListener(e -> startGame());
        add(startButton);
        
        // Instructions
        JLabel instructLabel = new JLabel("<html><center>Click on a piece to select it,<br/>then click on a valid square to move</center></html>");
        instructLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructLabel.setForeground(new Color(150, 150, 150));
        instructLabel.setBounds(0, 600, 600, 50);
        instructLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructLabel);
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(ACCENT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Placeholder functionality
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(ACCENT_COLOR);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        
        return field;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
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
    
    private void startGame() {
        String p1Name = player1Field.getText();
        String p2Name = player2Field.getText();
        
        // Validate input
        if (p1Name.isEmpty() || p1Name.equals("Enter Player 1 name")) {
            p1Name = "Player 1";
        }
        if (p2Name.isEmpty() || p2Name.equals("Enter Player 2 name")) {
            p2Name = "Player 2";
        }
        
        parent.startNewGame(p1Name, p2Name);
    }
}