#!/bin/bash
# ─────────────────────────────────────────────────────
#  QuizMaster Pro — Build & Run Script (Linux / macOS)
# ─────────────────────────────────────────────────────

SRC_DIR="src"
OUT_DIR="out"
MAIN_CLASS="quizapp.Main"

echo "🔨 Compiling..."
mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" @sources.txt
rm sources.txt

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "Choose mode:"
    echo "  1) GUI  (default)"
    echo "  2) Console"
    read -p "  Choice [1]: " mode
    if [ "$mode" = "2" ]; then
        java -cp "$OUT_DIR" "$MAIN_CLASS" --console
    else
        java -cp "$OUT_DIR" "$MAIN_CLASS"
    fi
else
    echo "❌ Build failed."
fi
