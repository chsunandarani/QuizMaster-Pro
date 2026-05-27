package quizapp.data;

import quizapp.model.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        // --- Java ---
        questions.add(new Question(
            "Which keyword is used to inherit a class in Java?",
            new String[]{"implements", "extends", "inherits", "super"},
            1, "Java"));

        questions.add(new Question(
            "What is the size of an int in Java?",
            new String[]{"2 bytes", "4 bytes", "8 bytes", "Platform-dependent"},
            1, "Java"));

        questions.add(new Question(
            "Which of these is NOT a valid access modifier in Java?",
            new String[]{"public", "private", "protected", "friend"},
            3, "Java"));

        questions.add(new Question(
            "What does JVM stand for?",
            new String[]{"Java Virtual Machine", "Java Variable Method", "Java Verified Module", "Java Visual Manager"},
            0, "Java"));

        questions.add(new Question(
            "Which collection does NOT allow duplicate values?",
            new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"},
            2, "Java"));

        questions.add(new Question(
            "What is the default value of a boolean in Java?",
            new String[]{"true", "false", "null", "0"},
            1, "Java"));

        questions.add(new Question(
            "Which method must every Java thread implement?",
            new String[]{"start()", "run()", "execute()", "init()"},
            1, "Java"));

        questions.add(new Question(
            "What is autoboxing in Java?",
            new String[]{"Converting primitive to Object wrapper", "Importing packages automatically", "Auto memory allocation", "Compiling code automatically"},
            0, "Java"));

        // --- OOP ---
        questions.add(new Question(
            "Which OOP concept allows a class to have multiple methods with the same name?",
            new String[]{"Inheritance", "Encapsulation", "Polymorphism", "Abstraction"},
            2, "OOP"));

        questions.add(new Question(
            "What is encapsulation?",
            new String[]{"Hiding implementation details", "Reusing code via inheritance", "Creating multiple objects", "Defining abstract methods"},
            0, "OOP"));

        questions.add(new Question(
            "An abstract class in Java can have:",
            new String[]{"Only abstract methods", "Only concrete methods", "Both abstract and concrete methods", "Neither abstract nor concrete methods"},
            2, "OOP"));

        questions.add(new Question(
            "Which keyword is used to prevent a class from being subclassed?",
            new String[]{"static", "abstract", "sealed", "final"},
            3, "OOP"));

        // --- Data Structures ---
        questions.add(new Question(
            "Which data structure uses LIFO order?",
            new String[]{"Queue", "Stack", "LinkedList", "Tree"},
            1, "DSA"));

        questions.add(new Question(
            "The time complexity of binary search is:",
            new String[]{"O(n)", "O(n²)", "O(log n)", "O(1)"},
            2, "DSA"));

        questions.add(new Question(
            "Which sorting algorithm has the best average-case complexity?",
            new String[]{"Bubble Sort", "Insertion Sort", "Merge Sort", "Selection Sort"},
            2, "DSA"));

        questions.add(new Question(
            "A HashMap in Java is based on which data structure?",
            new String[]{"Tree", "Linked List", "Hash Table", "Stack"},
            2, "DSA"));

        // --- General CS ---
        questions.add(new Question(
            "What does SQL stand for?",
            new String[]{"Structured Query Language", "Sequential Query List", "Simple Question Logic", "System Query Layer"},
            0, "CS General"));

        questions.add(new Question(
            "What is the binary representation of decimal 10?",
            new String[]{"1001", "1010", "1100", "0110"},
            1, "CS General"));

        questions.add(new Question(
            "Which protocol is used to send emails?",
            new String[]{"FTP", "HTTP", "SMTP", "SSH"},
            2, "CS General"));

        questions.add(new Question(
            "What does CPU stand for?",
            new String[]{"Central Processing Unit", "Core Programming Utility", "Computer Power Unit", "Control Processing Unit"},
            0, "CS General"));

        return questions;
    }

    public static List<Question> getShuffled(int count) {
        List<Question> all = getAllQuestions();
        Collections.shuffle(all);
        return all.subList(0, Math.min(count, all.size()));
    }
}
