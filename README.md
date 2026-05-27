# ⚡ QuizMaster Pro

A feature-rich Quiz Application built in **Java** with both a **Swing GUI** and a **Console** mode.

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=flat-square&logo=java)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## 📸 Features

| Feature | GUI | Console |
|---|---|---|
| MCQ Questions | ✅ | ✅ |
| Score Tracking | ✅ | ✅ |
| Countdown Timer (20s/q) | ✅ | ✅ |
| Leaderboard (Top 10) | ✅ | ✅ |
| Persistent Leaderboard | ✅ | ✅ |
| Randomized Questions | ✅ | ✅ |
| Category Labels | ✅ | ✅ |

---

## 📁 Project Structure

```
QuizApp/
├── src/
│   └── quizapp/
│       ├── Main.java                  ← Entry point
│       ├── model/
│       │   ├── Question.java          ← MCQ question model
│       │   └── Player.java            ← Player/score model
│       ├── data/
│       │   └── QuestionBank.java      ← 20 Java/CS questions
│       ├── util/
│       │   └── Leaderboard.java       ← Persistent top-10 leaderboard
│       └── ui/
│           ├── QuizGUI.java           ← Java Swing GUI
│           └── QuizConsole.java       ← Console (terminal) mode
├── run.sh                             ← Build & run (Linux/macOS)
├── run.bat                            ← Build & run (Windows)
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK 11 or higher installed
- Verify: `java -version`

### Option 1 — Shell Script (Linux/macOS)
```bash
chmod +x run.sh
./run.sh
```

### Option 2 — Batch Script (Windows)
```
run.bat
```

### Option 3 — Manual Compile & Run
```bash
# Compile
mkdir out
find src -name "*.java" | xargs javac -d out

# Run GUI (default)
java -cp out quizapp.Main

# Run Console mode
java -cp out quizapp.Main --console
```

---

## 📊 Leaderboard
Scores are saved to `leaderboard.dat` in the working directory.  
Top 10 players are ranked by **score** (highest first), then by **time** (fastest wins ties).

---

## 🧠 Question Categories
- **Java** — keywords, types, JVM, collections
- **OOP** — polymorphism, encapsulation, abstract classes
- **DSA** — sorting, searching, data structures
- **CS General** — binary, protocols, SQL

---

## 📤 Deploying to GitHub

See the full step-by-step guide in **`GITHUB_DEPLOY.md`**.

---

## 📄 License
MIT License — free to use and modify.
