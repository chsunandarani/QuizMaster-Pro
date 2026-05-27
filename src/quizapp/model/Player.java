package quizapp.model;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int score;
    private int totalQuestions;
    private long timeTakenSeconds;

    public Player(String name, int score, int totalQuestions, long timeTakenSeconds) {
        this.name = name;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public long getTimeTakenSeconds() { return timeTakenSeconds; }
    public double getPercentage() { return (double) score / totalQuestions * 100; }

    @Override
    public int compareTo(Player other) {
        if (other.score != this.score) return other.score - this.score;
        return Long.compare(this.timeTakenSeconds, other.timeTakenSeconds);
    }

    @Override
    public String toString() {
        return String.format("%-20s %3d/%-3d  %5.1f%%  %ds",
                name, score, totalQuestions, getPercentage(), timeTakenSeconds);
    }
}
