# Checkers GUI - Visual Design Guide

## Menu Screen (600x700px)

```
┌─────────────────────────────────────────────────┐
│                                                 │
│                                                 │
│                  CHECKERS                       │
│                 6x6 Board Game                  │
│                                                 │
│                   ●  ◐                          │
│                  (Red)(Black)                   │
│                                                 │
│          Player 1 (Red)                         │
│          ┌─────────────────────┐                │
│          │ Enter Player 1 name │                │
│          └─────────────────────┘                │
│                                                 │
│          Player 2 (Black)                       │
│          ┌─────────────────────┐                │
│          │ Enter Player 2 name │                │
│          └─────────────────────┘                │
│                                                 │
│                                                 │
│          ┌─────────────────┐                    │
│          │  START GAME     │                    │
│          └─────────────────┘                    │
│                                                 │
│      Click on a piece to select it,            │
│    then click on a valid square to move        │
│                                                 │
└─────────────────────────────────────────────────┘
```

## Game Screen (800x700px)

```
┌──────────────────────────────────────────────────────────────┐
│  ┌────────────────────┐  ┌──────────────────┐                │
│  │ BOARD (6x6)        │  │  GAME INFO       │                │
│  │                    │  │ ──────────────── │                │
│  │  ░▓░▓░▓           │  │  Player 1        │                │
│  │  ▓░▓░▓░           │  │  • Red Pieces    │                │
│  │  ░▓░▓░▓           │  │                  │                │
│  │  ▓░▓░▓░           │  │  Player 2        │                │
│  │  ░▓░▓░▓           │  │  • Black Pieces  │                │
│  │  ▓◉▓◉▓◉           │  │ ──────────────── │                │
│  │                    │  │  Current Turn:   │                │
│  │  Legend:           │  │  Player 1        │                │
│  │  ░ = light square  │  │                  │                │
│  │  ▓ = dark square   │  │                  │                │
│  │  ◉ = red piece     │  │  How to Play:    │                │
│  │  ◐ = black piece   │  │  1. Click piece  │                │
│  │  ♔ = king          │  │  2. Click square │                │
│  │                    │  │  3. Jump enemies │                │
│  │  Highlighted:      │  │  4. Reach end to │                │
│  │  🟢 = selected     │  │     become King  │                │
│  │  🔵 = valid move   │  │                  │                │
│  │                    │  │ ┌──────────────┐ │                │
│  │                    │  │ │  NEW GAME    │ │                │
│  │                    │  │ └──────────────┘ │                │
│  │                    │  │ ┌──────────────┐ │                │
│  │                    │  │ │  MAIN MENU   │ │                │
│  │                    │  │ └──────────────┘ │                │
│  └────────────────────┘  └──────────────────┘                │
└──────────────────────────────────────────────────────────────┘
```

## Color Palette

### Background & UI
- Window Background: #FAFAFA (Off-white)
- Text Primary: #464646 (Dark gray)
- Text Secondary: #787878 (Medium gray)
- Dividers: #C8C8C8 (Light gray)

### Game Pieces
- Red Piece Outer: #DC4646
- Red Piece Inner: #B43232
- Black Piece Outer: #323232
- Black Piece Inner: #1E1E1E
- King Crown: #FFD700 (Gold)

### Board
- Light Square: #F0E6DC (Beige)
- Dark Square: #B48C6E (Brown)

### Interactive Elements
- Button Default: #323232
- Button Hover: #505050
- Selected Piece: rgba(100, 200, 100, 0.6) - Translucent green
- Valid Move: rgba(100, 150, 255, 0.4) - Translucent blue

## Typography

- Title (CHECKERS): Arial Bold, 52px
- Subtitle: Arial Regular, 16px
- Section Headers: Arial Bold, 20px
- Player Names: Arial Bold, 16px
- Body Text: Arial Regular, 14px
- Small Text: Arial Regular, 12px
- Button Text: Arial Bold, 16px/13px

## Interactive Elements

### Buttons
- Rounded corners (slight radius)
- Dark background (#323232)
- White text
- Hover effect: Lighter background (#505050)
- Hand cursor on hover
- No border, no focus outline

### Text Fields
- Light background (white)
- Gray border (#C8C8C8, 2px)
- Padding: 5px 15px
- Placeholder text in gray
- Focus: Clear placeholder, dark text

### Board Squares
- 80px x 80px each
- Checkerboard pattern
- Smooth piece rendering with anti-aliasing
- Shadow effect on pieces for depth
- Crown symbol (♔) on kings in gold

### Hover & Selection Effects
- Green overlay on selected square
- Blue dots on valid move destinations
- Smooth color transitions
- Hand cursor for clickable pieces

## Layout Principles

1. **Minimalism**: Clean, uncluttered interface
2. **Whitespace**: Generous padding and margins
3. **Hierarchy**: Clear visual hierarchy with font sizes and weights
4. **Contrast**: Good contrast for readability
5. **Consistency**: Uniform spacing and alignment
6. **Responsiveness**: Fixed size but centered on screen
7. **Feedback**: Visual feedback for all interactions

## User Flow

1. Application Launch → Menu Screen
2. Enter Player Names → Click START GAME
3. Game Screen Loads → Red player's turn
4. Click Piece → Green highlight + blue valid moves
5. Click Destination → Piece moves
6. Turn switches automatically
7. Game ends → Trophy emoji + winner display
8. NEW GAME → Fresh game, same players
9. MAIN MENU → Return to start screen
