package checkers.data;

public class MatchRecord {
    public String dateTime;
    public String player1;
    public String player2;
    public String winner;
    public int    duration;

    public MatchRecord(String dateTime, String player1, String player2,
                       String winner, int duration) {
        this.dateTime = dateTime;
        this.player1  = player1;
        this.player2  = player2;
        this.winner   = winner;
        this.duration = duration;
    }

    public String durationString() {
        int m = duration / 60;
        int s = duration % 60;
        return m + ":" + String.format("%02d", s);
    }

    public String serialize() {
        return DataStore.esc(dateTime) + "|" + DataStore.esc(player1) + "|"
             + DataStore.esc(player2) + "|" + DataStore.esc(winner) + "|" + duration;
    }

    public static MatchRecord deserialize(String line) {
        String[] f = line.split("\\|", -1);
        if (f.length < 5) return null;
        try {
            return new MatchRecord(DataStore.unesc(f[0]), DataStore.unesc(f[1]),
                                   DataStore.unesc(f[2]), DataStore.unesc(f[3]),
                                   Integer.parseInt(f[4]));
        } catch (NumberFormatException e) { return null; }
    }
}
