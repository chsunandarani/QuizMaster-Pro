package quizapp.ui;

import quizapp.data.QuestionBank;
import quizapp.model.Player;
import quizapp.model.Question;
import quizapp.util.Leaderboard;

import java.util.*;
import java.util.concurrent.*;

public class QuizConsole {

    private static final int SECONDS_PER_QUESTION = 20;
    private static final int TOTAL_QUESTIONS      = 10;
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";

    private final Scanner scanner = new Scanner(System.in);
    private final Leaderboard leaderboard = new Leaderboard();

    public void run() {
        printBanner();
        while (true) {
            System.out.println(BOLD + CYAN + "\n  MAIN MENU" + RESET);
            System.out.println("  1. Start Quiz");
            System.out.println("  2. View Leaderboard");
            System.out.println("  3. Exit");
            System.out.print(YELLOW + "  Choice: " + RESET);

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> startQuiz();
                case "2" -> showLeaderboard();
                case "3" -> { System.out.println(GREEN + "\n  Goodbye! 👋" + RESET); return; }
                default  -> System.out.println(RED + "  Invalid choice." + RESET);
            }
        }
    }

    private void startQuiz() {
        System.out.print(YELLOW + "\n  Enter your name: " + RESET);
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) { System.out.println(RED + "  Name cannot be empty." + RESET); return; }

        List<Question> questions = QuestionBank.getShuffled(TOTAL_QUESTIONS);
        int score = 0;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("\n" + BOLD + CYAN + "─────────────────────────────────────────" + RESET);
            System.out.printf(BOLD + "  Question %d/%d  [%s]%n" + RESET, i + 1, questions.size(), q.getCategory());
            System.out.println(BOLD + "\n  " + q.getQuestionText() + RESET);
            String[] opts = q.getOptions();
            String[] labels = {"A", "B", "C", "D"};
            for (int j = 0; j < 4; j++)
                System.out.printf("  %s. %s%n", labels[j], opts[j]);

            int answer = timedInput("  Your answer (A/B/C/D): ", SECONDS_PER_QUESTION);

            if (answer == -1) {
                System.out.println(YELLOW + "  ⏰ Time's up! Correct: " + labels[q.getCorrectAnswerIndex()] + RESET);
            } else if (q.isCorrect(answer)) {
                score++;
                System.out.println(GREEN + "  ✅ Correct! +" + 1 + "  (Score: " + score + ")" + RESET);
            } else {
                System.out.println(RED + "  ❌ Wrong. Correct: " + labels[q.getCorrectAnswerIndex()] + RESET);
            }
        }

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        Player player = new Player(name, score, questions.size(), elapsed);
        leaderboard.addEntry(player);

        double pct = player.getPercentage();
        String grade = pct >= 80 ? "🌟 Excellent!" : pct >= 60 ? "👍 Good job!" : pct >= 40 ? "🙂 Keep practicing!" : "📚 Study more!";

        System.out.println("\n" + BOLD + PURPLE + "═══════════════════════════════════════════" + RESET);
        System.out.println(BOLD + PURPLE + "                 QUIZ COMPLETE                " + RESET);
        System.out.println(BOLD + PURPLE + "═══════════════════════════════════════════" + RESET);
        System.out.printf(BOLD + "  Score:    %d / %d  (%.0f%%)%n" + RESET, score, questions.size(), pct);
        System.out.printf("  Time:     %d seconds%n", elapsed);
        System.out.println("  Rating:   " + grade);
        System.out.println(BOLD + PURPLE + "═══════════════════════════════════════════" + RESET);
    }

    private int timedInput(String prompt, int seconds) {
        System.out.print(YELLOW + prompt + RESET);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<String> future = exec.submit(() -> scanner.nextLine().trim());
        try {
            String input = future.get(seconds, TimeUnit.SECONDS);
            switch (input.toUpperCase()) {
                case "A": return 0; case "B": return 1; case "C": return 2; case "D": return 3;
                default: return -1;
            }
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println();
            return -1;
        } catch (Exception e) {
            return -1;
        } finally {
            exec.shutdownNow();
        }
    }

    private void showLeaderboard() {
        List<Player> entries = leaderboard.getTopEntries();
        System.out.println("\n" + BOLD + YELLOW + "════════════════════════════════════════════" + RESET);
        System.out.println(BOLD + YELLOW + "               🏆 LEADERBOARD                " + RESET);
        System.out.println(BOLD + YELLOW + "════════════════════════════════════════════" + RESET);
        System.out.printf(BOLD + "  %-4s %-20s %-10s %-8s %s%n" + RESET, "Rank", "Name", "Score", "Pct", "Time");
        System.out.println("  ─────────────────────────────────────────");

        if (entries.isEmpty()) {
            System.out.println("  No entries yet!");
        }
        String[] medals = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < entries.size(); i++) {
            Player p = entries.get(i);
            String rank = (i < 3) ? medals[i] : " #" + (i + 1);
            System.out.printf("  %-5s %-20s %d/%-5d  %5.0f%%  %ds%n",
                    rank, p.getName(), p.getScore(), p.getTotalQuestions(),
                    p.getPercentage(), p.getTimeTakenSeconds());
        }
        System.out.println(BOLD + YELLOW + "════════════════════════════════════════════" + RESET);
    }

    private void printBanner() {
        System.out.println(BOLD + CYAN);
        System.out.println("  ╔═══════════════════════════════════════╗");
        System.out.println("  ║        ⚡  QuizMaster Pro  ⚡          ║");
        System.out.println("  ║    Java & CS Knowledge Challenge       ║");
        System.out.println("  ╚═══════════════════════════════════════╝");
        System.out.println(RESET);
    }

    public static void main(String[] args) {
        new QuizConsole().run();
    }
}
