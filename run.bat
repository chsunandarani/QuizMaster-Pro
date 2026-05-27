@echo off
REM ─────────────────────────────────────────────────────
REM  QuizMaster Pro — Build & Run Script (Windows)
REM ─────────────────────────────────────────────────────

set SRC_DIR=src
set OUT_DIR=out
set MAIN_CLASS=quizapp.Main

echo Compiling...
if not exist %OUT_DIR% mkdir %OUT_DIR%
dir /s /b %SRC_DIR%\*.java > sources.txt
javac -d %OUT_DIR% @sources.txt
del sources.txt

if %ERRORLEVEL%==0 (
    echo Build successful!
    echo.
    echo Choose mode:
    echo   1^) GUI  ^(default^)
    echo   2^) Console
    set /p mode="  Choice [1]: "
    if "%mode%"=="2" (
        java -cp %OUT_DIR% %MAIN_CLASS% --console
    ) else (
        java -cp %OUT_DIR% %MAIN_CLASS%
    )
) else (
    echo Build FAILED.
    pause
)
