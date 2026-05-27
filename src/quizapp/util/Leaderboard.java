package quizapp.util;

import quizapp.model.Player;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {
    private static final String FILE_PATH = "leaderboard.dat";
    private List<Player> entries;

    public Leaderboard() {
        entries = loadFromFile();
    }

    public void addEntry(Player player) {
        entries.add(player);
        Collections.sort(entries);
        if (entries.size() > 10) entries = entries.subList(0, 10);
        saveToFile();
    }

    public List<Player> getTopEntries() {
        return new ArrayList<>(entries);
    }

    public void clear() {
        entries.clear();
        saveToFile();
    }

    @SuppressWarnings("unchecked")
    private List<Player> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Player>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(entries);
        } catch (IOException e) {
            System.err.println("Could not save leaderboard: " + e.getMessage());
        }
    }
}
