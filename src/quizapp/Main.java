package quizapp;

import quizapp.ui.QuizConsole;
import quizapp.ui.QuizGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--console")) {
            new QuizConsole().run();
        } else {
            // Default: GUI mode
            try {
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            SwingUtilities.invokeLater(QuizGUI::new);
        }
    }
}
