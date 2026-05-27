package quizapp.ui;

import quizapp.data.QuestionBank;
import quizapp.model.Player;
import quizapp.model.Question;
import quizapp.util.Leaderboard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuizGUI extends JFrame {

    // Colors
    private static final Color BG_DARK      = new Color(15, 20, 40);
    private static final Color BG_CARD      = new Color(25, 35, 65);
    private static final Color ACCENT       = new Color(99, 179, 237);
    private static final Color ACCENT_GREEN = new Color(72, 199, 142);
    private static final Color ACCENT_RED   = new Color(252, 100, 100);
    private static final Color TEXT_PRIMARY = new Color(235, 240, 255);
    private static final Color TEXT_MUTED   = new Color(130, 150, 190);
    private static final Color OPTION_HOVER = new Color(40, 55, 100);
    private static final Color OPTION_BG    = new Color(30, 45, 80);

    // State
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String playerName = "";
    private long startTime;
    private Leaderboard leaderboard = new Leaderboard();

    // Timer
    private Timer countdownTimer;
    private int secondsLeft;
    private static final int SECONDS_PER_QUESTION = 20;

    // Panels
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Quiz components
    private JLabel questionNumLabel, questionLabel, timerLabel, scoreLabel;
    private JButton[] optionButtons;
    private JProgressBar timerBar;
    private JPanel optionsPanel;

    public QuizGUI() {
        setTitle("QuizMaster Pro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG_DARK);

        mainPanel.add(buildWelcomePanel(), "WELCOME");
        mainPanel.add(buildQuizPanel(),    "QUIZ");
        mainPanel.add(buildResultPanel(),  "RESULT");
        mainPanel.add(buildLeaderboard(),  "LEADERBOARD");

        add(mainPanel);
        cardLayout.show(mainPanel, "WELCOME");
        setVisible(true);
    }

    // ──────────────────────────────────────────────────────────────
    //  WELCOME SCREEN
    // ──────────────────────────────────────────────────────────────
    private JPanel buildWelcomePanel() {
        JPanel p = darkPanel(new BorderLayout());

        // Top brand bar
        JPanel brand = darkPanel(new FlowLayout(FlowLayout.CENTER));
        brand.setBorder(new EmptyBorder(40, 0, 10, 0));
        JLabel logo = new JLabel("⚡ QuizMaster Pro");
        logo.setFont(new Font("SansSerif", Font.BOLD, 36));
        logo.setForeground(ACCENT);
        brand.add(logo);

        JLabel sub = new JLabel("Test your Java & CS knowledge");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(TEXT_MUTED);
        JPanel subRow = darkPanel(new FlowLayout(FlowLayout.CENTER));
        subRow.add(sub);

        JPanel top = darkPanel(new BorderLayout());
        top.add(brand, BorderLayout.NORTH);
        top.add(subRow, BorderLayout.CENTER);

        // Center form
        JPanel form = darkPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 80, 20, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel nameLabel = styledLabel("Enter your name:", 14, TEXT_MUTED);
        JTextField nameField = new JTextField();
        nameField.setBackground(OPTION_BG);
        nameField.setForeground(TEXT_PRIMARY);
        nameField.setCaretColor(ACCENT);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        nameField.setPreferredSize(new Dimension(300, 48));

        JButton startBtn = accentButton("▶  Start Quiz", ACCENT);
        JButton lbBtn    = accentButton("🏆  Leaderboard", new Color(139, 92, 246));

        startBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!", "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }
            playerName = name;
            startQuiz();
        });
        lbBtn.addActionListener(e -> showLeaderboard());
        nameField.addActionListener(e -> startBtn.doClick());

        gbc.gridy = 0; form.add(nameLabel, gbc);
        gbc.gridy = 1; form.add(nameField, gbc);
        gbc.gridy = 2; form.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 3; form.add(startBtn, gbc);
        gbc.gridy = 4; form.add(lbBtn, gbc);

        // Stats row
        JPanel stats = darkPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        stats.add(statBox("20", "Questions"));
        stats.add(statBox("20s", "Per Question"));
        stats.add(statBox("Top 10", "Leaderboard"));

        p.add(top,   BorderLayout.NORTH);
        p.add(form,  BorderLayout.CENTER);
        p.add(stats, BorderLayout.SOUTH);
        return p;
    }

    private JPanel statBox(String value, String label) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(BG_CARD);
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(50, 70, 120), 1, true),
                new EmptyBorder(12, 20, 12, 20)));
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("SansSerif", Font.BOLD, 20));
        v.setForeground(ACCENT);
        JLabel l = new JLabel(label, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(TEXT_MUTED);
        box.add(v, BorderLayout.CENTER);
        box.add(l, BorderLayout.SOUTH);
        return box;
    }

    // ──────────────────────────────────────────────────────────────
    //  QUIZ SCREEN
    // ──────────────────────────────────────────────────────────────
    private JPanel buildQuizPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 0));

        // Header
        JPanel header = darkPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        questionNumLabel = styledLabel("Question 1 / 20", 13, TEXT_MUTED);
        timerLabel       = styledLabel("⏱ 20s", 16, ACCENT_GREEN);
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreLabel       = styledLabel("Score: 0", 14, ACCENT);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(questionNumLabel, BorderLayout.WEST);
        header.add(scoreLabel,       BorderLayout.CENTER);
        header.add(timerLabel,       BorderLayout.EAST);

        // Timer progress bar
        timerBar = new JProgressBar(0, SECONDS_PER_QUESTION);
        timerBar.setValue(SECONDS_PER_QUESTION);
        timerBar.setForeground(ACCENT_GREEN);
        timerBar.setBackground(BG_DARK);
        timerBar.setBorderPainted(false);
        timerBar.setPreferredSize(new Dimension(0, 5));

        JPanel headerWrap = darkPanel(new BorderLayout());
        headerWrap.add(header,   BorderLayout.CENTER);
        headerWrap.add(timerBar, BorderLayout.SOUTH);

        // Question
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 19));
        questionLabel.setForeground(TEXT_PRIMARY);
        questionLabel.setBorder(new EmptyBorder(30, 40, 20, 40));
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Options
        optionsPanel = darkPanel(new GridLayout(2, 2, 16, 16));
        optionsPanel.setBorder(new EmptyBorder(0, 40, 40, 40));
        optionButtons = new JButton[4];
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            JButton btn = new JButton();
            btn.setBackground(OPTION_BG);
            btn.setForeground(TEXT_PRIMARY);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(50, 70, 120), 1, true),
                    new EmptyBorder(14, 18, 14, 18)));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addActionListener(e -> handleAnswer(idx));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { if (btn.isEnabled()) btn.setBackground(OPTION_HOVER); }
                public void mouseExited(MouseEvent e)  { if (btn.isEnabled()) btn.setBackground(OPTION_BG); }
            });
            optionButtons[i] = btn;
            optionsPanel.add(btn);
        }

        p.add(headerWrap,    BorderLayout.NORTH);
        p.add(questionLabel, BorderLayout.CENTER);
        p.add(optionsPanel,  BorderLayout.SOUTH);
        return p;
    }

    // ──────────────────────────────────────────────────────────────
    //  RESULT SCREEN
    // ──────────────────────────────────────────────────────────────
    private JLabel resultScoreLabel, resultMsgLabel, resultTimeLabel;
    private JPanel buildResultPanel() {
        JPanel p = darkPanel(new BorderLayout());

        JPanel center = darkPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 60, 6, 60);

        JLabel title = styledLabel("Quiz Complete!", 28, ACCENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        resultScoreLabel = styledLabel("", 48, ACCENT_GREEN);
        resultScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        resultMsgLabel = styledLabel("", 16, TEXT_MUTED);
        resultMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        resultTimeLabel = styledLabel("", 14, TEXT_MUTED);
        resultTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton playAgain = accentButton("▶  Play Again", ACCENT);
        JButton viewLB    = accentButton("🏆  View Leaderboard", new Color(139, 92, 246));
        JButton home      = accentButton("⌂  Home", new Color(100, 120, 160));

        playAgain.addActionListener(e -> startQuiz());
        viewLB.addActionListener(e -> showLeaderboard());
        home.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));

        gbc.gridy = 0; center.add(title,            gbc);
        gbc.gridy = 1; center.add(resultScoreLabel, gbc);
        gbc.gridy = 2; center.add(resultMsgLabel,   gbc);
        gbc.gridy = 3; center.add(resultTimeLabel,  gbc);
        gbc.gridy = 4; center.add(Box.createVerticalStrut(16), gbc);
        gbc.gridy = 5; center.add(playAgain,        gbc);
        gbc.gridy = 6; center.add(viewLB,           gbc);
        gbc.gridy = 7; center.add(home,             gbc);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ──────────────────────────────────────────────────────────────
    //  LEADERBOARD SCREEN
    // ──────────────────────────────────────────────────────────────
    private JPanel lbListPanel;
    private JPanel buildLeaderboard() {
        JPanel p = darkPanel(new BorderLayout());

        JPanel header = darkPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(24, 30, 10, 30));
        JLabel title = styledLabel("🏆  Leaderboard", 26, ACCENT);
        JButton back = accentButton("← Back", new Color(100, 120, 160));
        back.setPreferredSize(new Dimension(110, 36));
        back.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        header.add(title, BorderLayout.WEST);
        header.add(back,  BorderLayout.EAST);

        lbListPanel = darkPanel(new GridLayout(0, 1, 0, 6));
        lbListPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        JScrollPane scroll = new JScrollPane(lbListPanel);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(null);

        p.add(header, BorderLayout.NORTH);
        p.add(scroll,  BorderLayout.CENTER);
        return p;
    }

    // ──────────────────────────────────────────────────────────────
    //  LOGIC
    // ──────────────────────────────────────────────────────────────
    private void startQuiz() {
        questions = QuestionBank.getShuffled(10);
        currentIndex = 0;
        score = 0;
        startTime = System.currentTimeMillis();
        cardLayout.show(mainPanel, "QUIZ");
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) { endQuiz(); return; }
        Question q = questions.get(currentIndex);

        questionNumLabel.setText("Question " + (currentIndex + 1) + " / " + questions.size());
        scoreLabel.setText("Score: " + score);
        questionLabel.setText("<html><div style='text-align:center;width:600px'>" + q.getQuestionText() + "</div></html>");

        String[] opts = q.getOptions();
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(labels[i] + ".  " + opts[i]);
            optionButtons[i].setBackground(OPTION_BG);
            optionButtons[i].setForeground(TEXT_PRIMARY);
            optionButtons[i].setEnabled(true);
        }

        startTimer();
    }

    private void startTimer() {
        if (countdownTimer != null) countdownTimer.stop();
        secondsLeft = SECONDS_PER_QUESTION;
        timerBar.setValue(secondsLeft);
        updateTimerUI();

        countdownTimer = new Timer(1000, e -> {
            secondsLeft--;
            timerBar.setValue(secondsLeft);
            updateTimerUI();
            if (secondsLeft <= 0) {
                countdownTimer.stop();
                flashTimeout();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerUI() {
        timerLabel.setText("⏱ " + secondsLeft + "s");
        if (secondsLeft > 10) {
            timerLabel.setForeground(ACCENT_GREEN);
            timerBar.setForeground(ACCENT_GREEN);
        } else if (secondsLeft > 5) {
            timerLabel.setForeground(new Color(255, 200, 50));
            timerBar.setForeground(new Color(255, 200, 50));
        } else {
            timerLabel.setForeground(ACCENT_RED);
            timerBar.setForeground(ACCENT_RED);
        }
    }

    private void flashTimeout() {
        for (JButton b : optionButtons) b.setEnabled(false);
        int correct = questions.get(currentIndex).getCorrectAnswerIndex();
        optionButtons[correct].setBackground(ACCENT_GREEN);
        Timer t = new Timer(1200, e -> nextQuestion());
        t.setRepeats(false);
        t.start();
    }

    private void handleAnswer(int selectedIdx) {
        if (countdownTimer != null) countdownTimer.stop();
        Question q = questions.get(currentIndex);
        for (JButton b : optionButtons) b.setEnabled(false);

        if (q.isCorrect(selectedIdx)) {
            score++;
            optionButtons[selectedIdx].setBackground(ACCENT_GREEN);
        } else {
            optionButtons[selectedIdx].setBackground(ACCENT_RED);
            optionButtons[q.getCorrectAnswerIndex()].setBackground(ACCENT_GREEN);
        }

        scoreLabel.setText("Score: " + score);
        Timer t = new Timer(1000, e -> nextQuestion());
        t.setRepeats(false);
        t.start();
    }

    private void nextQuestion() {
        currentIndex++;
        loadQuestion();
    }

    private void endQuiz() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        Player player = new Player(playerName, score, questions.size(), elapsed);
        leaderboard.addEntry(player);

        double pct = player.getPercentage();
        String msg = pct >= 80 ? "🌟 Excellent!" : pct >= 60 ? "👍 Good job!" : pct >= 40 ? "🙂 Keep practicing!" : "📚 Study more!";

        resultScoreLabel.setText(score + " / " + questions.size());
        resultMsgLabel.setText(msg);
        resultTimeLabel.setText("Time taken: " + elapsed + " seconds  •  " + String.format("%.0f%%", pct));
        cardLayout.show(mainPanel, "RESULT");
    }

    private void showLeaderboard() {
        lbListPanel.removeAll();
        List<Player> entries = leaderboard.getTopEntries();
        String[] medals = {"🥇", "🥈", "🥉"};

        if (entries.isEmpty()) {
            JLabel empty = styledLabel("No entries yet. Play a game!", 16, TEXT_MUTED);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            lbListPanel.add(empty);
        }

        for (int i = 0; i < entries.size(); i++) {
            Player pl = entries.get(i);
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(BG_CARD);
            row.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(50, 70, 120), 1, true),
                    new EmptyBorder(12, 20, 12, 20)));

            String rank = (i < 3) ? medals[i] : "#" + (i + 1);
            JLabel rankL = styledLabel(rank + "  " + pl.getName(), 15, i == 0 ? new Color(255, 215, 0) : TEXT_PRIMARY);
            JLabel scoreL = styledLabel(pl.getScore() + "/" + pl.getTotalQuestions()
                    + "  (" + String.format("%.0f%%", pl.getPercentage()) + ")  "
                    + pl.getTimeTakenSeconds() + "s", 14, TEXT_MUTED);
            scoreL.setHorizontalAlignment(SwingConstants.RIGHT);

            row.add(rankL,  BorderLayout.WEST);
            row.add(scoreL, BorderLayout.EAST);
            lbListPanel.add(row);
        }

        lbListPanel.revalidate();
        lbListPanel.repaint();
        cardLayout.show(mainPanel, "LEADERBOARD");
    }

    // ──────────────────────────────────────────────────────────────
    //  HELPERS
    // ──────────────────────────────────────────────────────────────
    private JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_DARK);
        return p;
    }

    private JLabel styledLabel(String text, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, size));
        l.setForeground(color);
        return l;
    }

    private JButton accentButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setBorder(new EmptyBorder(13, 24, 13, 24));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(QuizGUI::new);
    }
}
