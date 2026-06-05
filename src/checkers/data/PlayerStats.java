package checkers.data;

public class PlayerStats {
    public String username;
    public int    passwordHash;
    public int    gamesPlayed;
    public int    wins;
    public int    losses;
    public int    draws;
    public long   totalPlayTime;
    public long   totalTurns;
    public long   totalTurnTime;
    public int    fastestWin;
    public int    longestGame;

    public PlayerStats(String username) {
        this.username = username;
    }

    public double averageTurnTime() {
        return totalTurns == 0 ? 0.0 : (double) totalTurnTime / (double) totalTurns;
    }

    public void recordGame(String result, int matchSeconds, long turns, long turnSeconds) {
        gamesPlayed++;
        if ("WIN".equals(result)) {
            wins++;
            if (fastestWin == 0 || matchSeconds < fastestWin) fastestWin = matchSeconds;
        } else if ("LOSS".equals(result)) {
            losses++;
        } else {
            draws++;
        }
        totalPlayTime += matchSeconds;
        totalTurns    += turns;
        totalTurnTime += turnSeconds;
        if (matchSeconds > longestGame) longestGame = matchSeconds;
    }

    public String serialize() {
        return DataStore.esc(username) + "|" + passwordHash + "|" + gamesPlayed + "|"
             + wins + "|" + losses + "|" + draws + "|" + totalPlayTime + "|"
             + totalTurns + "|" + totalTurnTime + "|" + fastestWin + "|" + longestGame;
    }

    public static PlayerStats deserialize(String line) {
        String[] f = line.split("\\|", -1);
        if (f.length < 11) return null;
        try {
            PlayerStats s = new PlayerStats(DataStore.unesc(f[0]));
            s.passwordHash  = Integer.parseInt(f[1]);
            s.gamesPlayed   = Integer.parseInt(f[2]);
            s.wins          = Integer.parseInt(f[3]);
            s.losses        = Integer.parseInt(f[4]);
            s.draws         = Integer.parseInt(f[5]);
            s.totalPlayTime = Long.parseLong(f[6]);
            s.totalTurns    = Long.parseLong(f[7]);
            s.totalTurnTime = Long.parseLong(f[8]);
            s.fastestWin    = Integer.parseInt(f[9]);
            s.longestGame   = Integer.parseInt(f[10]);
            return s;
        } catch (NumberFormatException e) { return null; }
    }
}
