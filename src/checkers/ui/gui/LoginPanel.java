package checkers.ui.gui;

import checkers.data.DataStore;
import checkers.data.PlayerStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private CheckersGUI parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    // Color scheme (matches MenuPanel)
    private static final Color BG_COLOR     = new Color(250, 250, 250);
    private static final Color ACCENT_COLOR = new Color(70, 70, 70);
    private static final Color BUTTON_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_HOVER = new Color(80, 80, 80);
    private static final Color RED_ACCENT   = new Color(220, 70, 70);
    private static final Color BLACK_ACCENT = new Color(50, 50, 50);

    public LoginPanel(CheckersGUI parent) {
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
        titleLabel.setBounds(0, 70, 600, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sign in or play as a guest");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setBounds(0, 135, 600, 20);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitleLabel);

        // Decorative checkers icons
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED_ACCENT);
                g2.fillOval(30, 0, 60, 60);
                g2.setColor(new Color(180, 50, 50));
                g2.fillOval(35, 3, 50, 50);
                g2.setColor(BLACK_ACCENT);
                g2.fillOval(130, 0, 60, 60);
                g2.setColor(new Color(30, 30, 30));
                g2.fillOval(135, 3, 50, 50);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setBounds(200, 175, 200, 60);
        add(iconPanel);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(ACCENT_COLOR);
        userLabel.setBounds(150, 270, 300, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setForeground(ACCENT_COLOR);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        usernameField.setBounds(150, 300, 300, 45);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setForeground(ACCENT_COLOR);
        passLabel.setBounds(150, 360, 300, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setForeground(ACCENT_COLOR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        passwordField.setBounds(150, 390, 300, 45);
        add(passwordField);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });

        // Message label (errors / info)
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        messageLabel.setForeground(RED_ACCENT);
        messageLabel.setBounds(150, 440, 300, 20);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel);

        // Buttons
        JButton loginButton = createButton("LOG IN");
        loginButton.setBounds(150, 470, 145, 45);
        loginButton.addActionListener(e -> doLogin());
        add(loginButton);

        JButton signupButton = createButton("SIGN UP");
        signupButton.setBounds(305, 470, 145, 45);
        signupButton.addActionListener(e -> doSignup());
        add(signupButton);

        JButton guestButton = createButton("PLAY AS GUEST");
        guestButton.setBounds(150, 525, 300, 45);
        guestButton.addActionListener(e -> parent.enterMenu(null));
        add(guestButton);

        JLabel hint = new JLabel("<html><center>Log in to track your stats and match history,<br/>"
                + "or jump straight in as a guest.</center></html>");
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(new Color(150, 150, 150));
        hint.setBounds(0, 600, 600, 50);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        add(hint);
    }

    private void doLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        if (user.isEmpty()) {
            setError("Please enter a username.");
            return;
        }
        DataStore ds = DataStore.get();
        if (!ds.profileExists(user)) {
            setError("No such account. Try Sign Up.");
            return;
        }
        PlayerStats s = ds.authenticate(user, pass);
        if (s == null) {
            setError("Incorrect password.");
            return;
        }
        parent.enterMenu(s.username);
    }

    private void doSignup() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        if (user.isEmpty()) {
            setError("Please enter a username.");
            return;
        }
        DataStore ds = DataStore.get();
        if (ds.profileExists(user)) {
            setError("That username already exists.");
            return;
        }
        PlayerStats s = ds.createProfile(user, pass);
        if (s == null) {
            setError("Could not create account.");
            return;
        }
        parent.enterMenu(s.username);
    }

    private void setError(String msg) {
        messageLabel.setForeground(RED_ACCENT);
        messageLabel.setText(msg);
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
