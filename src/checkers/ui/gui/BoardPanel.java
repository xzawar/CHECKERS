package checkers.ui.gui;

import checkers.logic.CheckersGame;
import checkers.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BoardPanel extends JPanel {
    private CheckersGame game;
    private GamePanel gamePanel;
    private static final int CELL_SIZE = 80;
    private static final int BOARD_SIZE = 6;

    // When true the board is rendered rotated 180° so the opponent can read it
    // from their own screen (used for dual-screen local multiplayer).
    private final boolean flipped;

    // Color scheme
    private static final Color LIGHT_SQUARE = new Color(240, 230, 220);
    private static final Color DARK_SQUARE = new Color(180, 140, 110);
    private static final Color RED_PIECE = new Color(220, 70, 70);
    private static final Color RED_PIECE_DARK = new Color(180, 50, 50);
    private static final Color BLACK_PIECE = new Color(50, 50, 50);
    private static final Color BLACK_PIECE_DARK = new Color(30, 30, 30);
    private static final Color SELECTED_HIGHLIGHT = new Color(100, 200, 100, 150);
    private static final Color VALID_MOVE_HIGHLIGHT = new Color(100, 150, 255, 100);
    private static final Color KING_CROWN = new Color(255, 215, 0);

    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Move> validMoves = null;

    public BoardPanel(CheckersGame game, GamePanel gamePanel) {
        this(game, gamePanel, false);
    }

    public BoardPanel(CheckersGame game, GamePanel gamePanel, boolean flipped) {
        this.game = game;
        this.gamePanel = gamePanel;
        this.flipped = flipped;
        setPreferredSize(new Dimension(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // Map a screen pixel to a logical board coordinate, accounting for flipping.
    private int toLogical(int displayIndex) {
        return flipped ? (BOARD_SIZE - 1 - displayIndex) : displayIndex;
    }

    private void handleClick(int x, int y) {
        if (game.getEngine().getState().isOver()) {
            return;
        }

        int dispCol = x / CELL_SIZE;
        int dispRow = y / CELL_SIZE;

        if (dispRow < 0 || dispRow >= BOARD_SIZE || dispCol < 0 || dispCol >= BOARD_SIZE) {
            return;
        }

        int row = toLogical(dispRow);
        int col = toLogical(dispCol);

        Board board = game.getEngine().getBoard();
        Piece clickedPiece = board.getPiece(row, col);

        // If a piece is selected
        if (selectedRow != -1 && selectedCol != -1) {
            // Try to move to clicked position
            Move attemptedMove = new Move(selectedRow, selectedCol, row, col, false);

            // Check if this move is valid
            boolean validMove = false;
            if (validMoves != null) {
                for (Move m : validMoves) {
                    if (m.fromRow == selectedRow && m.fromCol == selectedCol &&
                        m.toRow == row && m.toCol == col) {
                        attemptedMove = m; // Use the actual move (with isJump flag)
                        validMove = true;
                        break;
                    }
                }
            }

            if (validMove) {
                game.getEngine().applyMove(attemptedMove);
                selectedRow = -1;
                selectedCol = -1;
                validMoves = null;
                repaint();
                gamePanel.updateDisplay();
            } else if (clickedPiece != null &&
                       clickedPiece.getColor() == game.getEngine().getCurrentTurn()) {
                // Select a different piece
                selectPiece(row, col);
            } else {
                // Invalid move, deselect
                selectedRow = -1;
                selectedCol = -1;
                validMoves = null;
                repaint();
            }
        } else {
            // No piece selected, try to select clicked piece
            if (clickedPiece != null &&
                clickedPiece.getColor() == game.getEngine().getCurrentTurn()) {
                selectPiece(row, col);
            }
        }
    }

    private void selectPiece(int row, int col) {
        selectedRow = row;
        selectedCol = col;

        // Get all valid moves for current player
        List<Move> allMoves = game.getEngine().getValidMoves();

        // Filter moves that start from selected piece
        validMoves = new java.util.ArrayList<>();
        for (Move m : allMoves) {
            if (m.fromRow == row && m.fromCol == col) {
                validMoves.add(m);
            }
        }

        repaint();
    }

    /** Clear any current selection (used to keep mirrored boards tidy). */
    public void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        validMoves = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Board board = game.getEngine().getBoard();

        // Draw board squares (iterate logical coords, place at display coords)
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int dispRow = toLogical(row);  // symmetric mapping
                int dispCol = toLogical(col);
                int x = dispCol * CELL_SIZE;
                int y = dispRow * CELL_SIZE;

                // Checkerboard pattern (based on logical coords so colors stay put)
                if ((row + col) % 2 == 0) {
                    g2.setColor(LIGHT_SQUARE);
                } else {
                    g2.setColor(DARK_SQUARE);
                }
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g2.setColor(SELECTED_HIGHLIGHT);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Highlight valid move destinations
                if (validMoves != null) {
                    for (Move m : validMoves) {
                        if (m.toRow == row && m.toCol == col) {
                            g2.setColor(VALID_MOVE_HIGHLIGHT);
                            g2.fillOval(x + CELL_SIZE/2 - 15, y + CELL_SIZE/2 - 15, 30, 30);
                        }
                    }
                }
            }
        }

        // Draw pieces
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    int dispRow = toLogical(row);
                    int dispCol = toLogical(col);
                    drawPiece(g2, piece, dispCol * CELL_SIZE, dispRow * CELL_SIZE);
                }
            }
        }
    }

    private void drawPiece(Graphics2D g2, Piece piece, int x, int y) {
        int centerX = x + CELL_SIZE / 2;
        int centerY = y + CELL_SIZE / 2;
        int radius = 28;

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(centerX - radius + 2, centerY - radius + 4, radius * 2, radius * 2);

        // Draw main piece
        if (piece.getColor() == Piece.Color.RED) {
            g2.setColor(RED_PIECE);
        } else {
            g2.setColor(BLACK_PIECE);
        }
        g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Draw inner circle for depth
        if (piece.getColor() == Piece.Color.RED){
            g2.setColor(RED_PIECE_DARK);
        } else {
            g2.setColor(BLACK_PIECE_DARK);
        }
        g2.fillOval(centerX - radius + 4, centerY - radius + 4, radius * 2 - 8, radius * 2 - 8);

        // Draw king crown
        if (piece.isKing()) {
            g2.setColor(KING_CROWN);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();
            String crown = "\u2654";
            int textWidth = fm.stringWidth(crown);
            int textHeight = fm.getAscent();
            g2.drawString(crown, centerX - textWidth/2, centerY + textHeight/2 - 2);
        }
    }
}
