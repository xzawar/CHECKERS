package checkers.data;

import java.io.*;
import java.util.*;

public class DataStore {
    private static DataStore instance;
    private final File profilesFile;
    private final File historyFile;
    private final Map<String, PlayerStats> profiles = new LinkedHashMap<>();
    private final List<MatchRecord> history = new ArrayList<>();

    private DataStore() {
        String home = System.getProperty("user.home");
        File base = (home != null && !home.isEmpty()) ? new File(home, ".checkers_data") : new File(".checkers_data");
        if (!base.exists()) base.mkdirs();
        profilesFile = new File(base, "profiles.txt");
        historyFile  = new File(base, "history.txt");
        load();
    }

    public static synchronized DataStore get() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public synchronized boolean profileExists(String username) {
        return username != null && profiles.containsKey(key(username));
    }

    public synchronized PlayerStats getProfile(String username) {
        return username == null ? null : profiles.get(key(username));
    }

    public synchronized PlayerStats createProfile(String username, String password) {
        if (username == null || username.trim().isEmpty() || profileExists(username)) return null;
        PlayerStats s = new PlayerStats(username.trim());
        s.passwordHash = hash(password);
        profiles.put(key(username), s);
        saveProfiles();
        return s;
    }

    public synchronized PlayerStats authenticate(String username, String password) {
        PlayerStats s = getProfile(username);
        return (s != null && s.passwordHash == hash(password)) ? s : null;
    }

    public synchronized void saveProfile(PlayerStats s) {
        if (s == null) return;
        profiles.put(key(s.username), s);
        saveProfiles();
    }

    public synchronized void addMatch(MatchRecord r) {
        if (r == null) return;
        history.add(r);
        appendHistory(r);
    }

    public synchronized List<MatchRecord> matchesFor(String username, int limit) {
        List<MatchRecord> out = new ArrayList<>();
        for (int i = history.size() - 1; i >= 0 && out.size() < limit; i--) {
            MatchRecord r = history.get(i);
            if (username == null || eq(r.player1, username) || eq(r.player2, username)) out.add(r);
        }
        return out;
    }

    private void load() {
        if (profilesFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(profilesFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        PlayerStats s = PlayerStats.deserialize(line);
                        if (s != null) profiles.put(key(s.username), s);
                    }
                }
            } catch (IOException e) { System.err.println("profiles read error: " + e); }
        }
        if (historyFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        MatchRecord r = MatchRecord.deserialize(line);
                        if (r != null) history.add(r);
                    }
                }
            } catch (IOException e) { System.err.println("history read error: " + e); }
        }
    }

    private void saveProfiles() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(profilesFile, false))) {
            for (PlayerStats s : profiles.values()) { bw.write(s.serialize()); bw.newLine(); }
        } catch (IOException e) { System.err.println("profiles write error: " + e); }
    }

    private void appendHistory(MatchRecord r) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile, true))) {
            bw.write(r.serialize()); bw.newLine();
        } catch (IOException e) { System.err.println("history write error: " + e); }
    }

    private static String key(String u)  { return u == null ? "" : u.trim().toLowerCase(); }
    private static boolean eq(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }

    public static int hash(String p) {
        if (p == null || p.isEmpty()) return 0;
        int h = 7;
        for (int i = 0; i < p.length(); i++) h = h * 31 + p.charAt(i);
        return h;
    }

    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\p").replace("\n", "\\n").replace("\r", "");
    }

    public static String unesc(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                if      (n == 'p')  sb.append('|');
                else if (n == 'n')  sb.append('\n');
                else if (n == '\\') sb.append('\\');
                else                sb.append(n);
            } else { sb.append(c); }
        }
        return sb.toString();
    }
}
