# Checkers Game — Enhanced Edition

Built on top of the original Beautiful GUI Version.
All new features described below have been added **without changing the original
minimalistic theme, color palette, or layout.**

---

## What's New

### Game Features
| Feature | Detail |
|---|---|
| Per-turn countdown | 10 s per turn; turns red at ≤3 s, orange at ≤6 s; auto-move on timeout |
| Per-player total timer | Each player's accumulated thinking time shown in the Info Panel |
| Current player piece color | Animated piece icon updates to the active color every turn |
| Info Panel enriched | Shows: player names, piece counts, both timers, match status, and four action buttons |
| Match status | Displays "In Progress" during play; shows 🏆 winner or 🤝 Draw at end |
| Offer Draw button | Either player can agree to a draw; a 40-ply no-capture rule also triggers a draw |
| Dual Screen | "DUAL SCREEN" button opens a second window with the board **flipped 180°** for the opponent; auto-placed on a second monitor if one is connected; both boards are fully interactive and always in sync |

### Account System
| Feature | Detail |
|---|---|
| Login screen | First screen: Log In / Sign Up / Play as Guest |
| Login | Enter username + password (stored as a hash locally) |
| Sign Up | Creates a new profile; error if name taken |
| Guest mode | Skips login; no profile stats saved, but match history is recorded |
| Logged-in menu | Player 1 field pre-filled with account username; subtitle shows "Logged in as X" |
| MY STATS button | Opens the Stats screen (only when logged in) |
| LOG OUT button | Returns to the login screen |

### Statistics & Data Storage
All data is saved to `~/.checkers_data/` automatically.

**Profile stats (per account):**
- Games played
- Wins / Losses / Draws
- Total play time
- Average turn time (seconds per turn)
- Fastest win (shortest winning match)
- Longest game

**Match history (always saved):**
- Date & time
- Player 1 and Player 2 names
- Winner (or "Draw")
- Match duration

The **Stats screen** (`MY STATS` button) shows all of the above for the
logged-in player plus a table of their 5 most recent matches.

---

## How to Build & Run

### Requirements
- JDK 8 or later (needs `javac` on your PATH).

### Quick build (Linux / macOS)
```bash
cd CHECKERS
bash build.sh
```

### Quick build (Windows)
```cmd
cd CHECKERS\src
dir /s /B *.java > sources.txt
javac -d ..\bin @sources.txt
java -cp ..\bin checkers.ui.gui.CheckersGUI
```

### Run the pre-built JAR (if present)
```bash
java -jar dist/CHECKERS.jar
```

---

## Project Structure

```
CHECKERS/
├── src/checkers/
│   ├── Main.java                  # Entry point (console OR GUI)
│   ├── data/
│   │   ├── DataStore.java            # File-based persistence (profiles + history)
│   │   ├── PlayerStats.java          # Per-profile stat fields + serialization
│   │   └── MatchRecord.java           # Single match history record
│   ├── logic/
│   │   ├── GameEngine.java            # Applies moves, draw detection
│   │   ├── GameState.java             # ONGOING / RED_WINS / BLACK_WINS / DRAW
│   │   ├── CheckersGame.java
│   │   └── MoveValidator.java
│   ├── model/
│   │   ├── Board.java / Piece.java / Player.java / Move.java
│   └── ui/gui/
│       ├── CheckersGUI.java           # JFrame, CardLayout (LOGIN→MENU→GAME→STATS)
│       ├── LoginPanel.java            # NEW – Log In / Sign Up / Guest
│       ├── MenuPanel.java             # Updated – account context, MY STATS, LOG OUT
│       ├── GamePanel.java             # Updated – dual-screen, game-over recording
│       ├── InfoPanel.java             # Updated – all timers, piece counts, buttons
│       ├── BoardPanel.java            # Updated – flip support for second screen
│       ├── StatsPanel.java            # NEW – Stats & match history display
│       └── SecondScreenFrame.java     # NEW – Dual-screen mirror window
└── build.sh                       # NEW – One-command build + run script
```

---

## Credits
Original: Ahmad Zawar, Ahmad Iqbal  
Enhanced: Notion AI (2026)  
Game: 6×6 Checkers — Minimalistic Design
