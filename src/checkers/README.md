# Checkers Game - Beautiful GUI Version

A minimalistic 6x6 Checkers game with a beautiful GUI built in Java 8.

## Features

✨ **Beautiful Minimalistic Design**
- Clean, modern interface with smooth interactions
- Elegant color scheme (grays, reds, and blacks)
- Visual feedback for selected pieces and valid moves
- Animated hover effects on buttons

🎮 **Game Features**
- Main menu to enter player names
- Interactive game board with click-to-move
- Visual indicators for valid moves
- King pieces with crown symbol (♔)
- Game status display
- New game and menu navigation options

## How to Compile and Run

### Requirements
- Java 8 or higher
- Java Development Kit (JDK)

### Compilation Steps

1. Navigate to the project directory:
   ```bash
   cd "New folder"
   ```

2. Create a bin directory for compiled classes:
   ```bash
   mkdir -p bin
   ```

3. Compile all Java files:
   ```bash
   javac -d bin model/*.java logic/*.java ui/gui/*.java
   ```

4. Run the GUI:
   ```bash
   java -cp bin checkers.ui.gui.CheckersGUI
   ```

### Alternative: Using Package Structure

If the above doesn't work, you can compile from the parent directory:

```bash
javac -d . New\ folder/model/*.java New\ folder/logic/*.java New\ folder/ui/gui/*.java
java checkers.ui.gui.CheckersGUI
```

## How to Play

1. **Start Screen**: Enter names for Player 1 (Red) and Player 2 (Black)
2. **Click START GAME** to begin
3. **Select a piece** by clicking on it (it will highlight in green)
4. **Valid move destinations** will show as blue circles
5. **Click a valid destination** to move your piece
6. **Jump over opponent pieces** to capture them
7. **Reach the opposite end** to become a King (gets a crown ♔)
8. **Win** by capturing all opponent pieces or blocking all their moves

## Game Rules

- Red pieces move first
- Pieces can move diagonally forward on dark squares
- Pieces can jump over opponent pieces to capture them
- Multiple jumps are allowed in sequence
- Kings can move both forward and backward
- A piece becomes a King when it reaches the opposite end of the board
- The game ends when one player has no valid moves left

## Project Structure

```
New folder/
├── model/
│   ├── Board.java        # 6x6 game board
│   ├── Piece.java        # Red/Black pieces with King status
│   ├── Player.java       # Player information
│   └── Move.java         # Move representation
├── logic/
│   ├── CheckersGame.java # Game controller
│   ├── GameEngine.java   # Core game logic
│   ├── MoveValidator.java# Valid move calculation
│   └── GameState.java    # Win/loss detection
└── ui/
    ├── console/
    │   └── ConsoleUI.java # Original console UI
    └── gui/
        ├── CheckersGUI.java  # Main window (CardLayout)
        ├── MenuPanel.java    # Start screen with player input
        ├── GamePanel.java    # Game container
        ├── BoardPanel.java   # Interactive board rendering
        └── InfoPanel.java    # Game info and controls
```

## GUI Components

### MenuPanel
- Clean title with "CHECKERS" branding
- Two text fields for player names with placeholders
- Styled START GAME button
- Decorative red and black piece icons
- Instructions at the bottom

### GamePanel
- Contains BoardPanel (left) and InfoPanel (right)
- Uses BorderLayout with padding for clean spacing

### BoardPanel
- 6x6 checkerboard with light and dark squares
- Pieces rendered as circles with depth effect
- Green highlight for selected piece
- Blue circles showing valid move destinations
- Gold crown (♔) for King pieces
- Click-based interaction

### InfoPanel
- Player names and colors
- Current turn indicator
- Win/loss status with trophy emoji
- How to Play instructions
- NEW GAME button
- MAIN MENU button

## Color Scheme

- **Background**: Light gray (#FAFAFA)
- **Buttons**: Dark gray (#323232) with hover effect (#505050)
- **Red Player**: #DC4646 (primary) / #B43232 (dark)
- **Black Player**: #323232 (primary) / #1E1E1E (dark)
- **Board Light Squares**: #F0E6DC
- **Board Dark Squares**: #B48C6E
- **Selected Highlight**: Green with transparency
- **Valid Move**: Blue with transparency
- **King Crown**: Gold (#FFD700)

## Credits

Created by: Ahmad Zawar
Game: 6x6 Checkers
UI: Beautiful Minimalistic Design
Year: 2025
