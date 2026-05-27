package quizapp.model;

public class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private String category;

    public Question(String questionText, String[] options, int correctAnswerIndex, String category) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.category = category;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getCorrectAnswer() { return options[correctAnswerIndex]; }
    public String getCategory() { return category; }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }
}
