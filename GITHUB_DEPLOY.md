# 📤 GitHub Deployment Guide — QuizMaster Pro

Follow these steps exactly to upload your project to GitHub.

---

## ✅ Step 1 — Install Prerequisites

- **Java JDK 11+**: https://adoptium.net
- **Git**: https://git-scm.com/downloads
- **GitHub account**: https://github.com

Verify installations:
```bash
java -version
git --version
```

---

## ✅ Step 2 — Create a GitHub Repository

1. Go to https://github.com/new
2. Fill in:
   - **Repository name**: `QuizMaster-Pro`
   - **Description**: `Java Quiz Application with GUI and Console mode`
   - **Visibility**: Public (or Private)
   - ✅ **Add a README** — UNCHECK this (we already have one)
3. Click **Create repository**
4. Copy the repository URL (e.g. `https://github.com/YOUR_USERNAME/QuizMaster-Pro.git`)

---

## ✅ Step 3 — Set Up Git Locally

Open a terminal/command prompt in your `QuizApp/` folder.

```bash
# Initialize git
git init

# Set your identity (first time only)
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```

---

## ✅ Step 4 — Add .gitignore

Create a file named `.gitignore` in the `QuizApp/` folder with this content:

```
out/
*.class
*.dat
*.iml
.idea/
.vscode/
*.DS_Store
sources.txt
```

---

## ✅ Step 5 — Stage and Commit Files

```bash
git add .
git commit -m "Initial commit: QuizMaster Pro with GUI and Console mode"
```

---

## ✅ Step 6 — Push to GitHub

```bash
# Replace the URL with your actual repository URL
git remote add origin https://github.com/YOUR_USERNAME/QuizMaster-Pro.git

git branch -M main
git push -u origin main
```

You'll be prompted for your GitHub credentials:
- **Username**: your GitHub username
- **Password**: use a **Personal Access Token** (NOT your account password)

### Getting a Personal Access Token:
1. GitHub → Settings → Developer Settings → Personal Access Tokens → Tokens (classic)
2. Click **Generate new token**
3. Select scope: `repo`
4. Copy the token and use it as your password

---

## ✅ Step 7 — Verify on GitHub

Visit `https://github.com/YOUR_USERNAME/QuizMaster-Pro`

You should see all your files listed! ✅

---

## 🔄 Future Updates (after making changes)

```bash
git add .
git commit -m "Describe what you changed"
git push
```

---

## 🌐 Optional: Enable GitHub Pages

If you later build a web version:
1. Go to repo → **Settings** → **Pages**
2. Source: `main` branch, `/ (root)` folder
3. Click **Save** → your site is live at `https://YOUR_USERNAME.github.io/QuizMaster-Pro`

---

## 📌 Quick Reference

| Command | What it does |
|---|---|
| `git init` | Start tracking a folder |
| `git add .` | Stage all changed files |
| `git commit -m "msg"` | Save a snapshot |
| `git push` | Upload to GitHub |
| `git pull` | Download latest changes |
| `git status` | See what's changed |
| `git log` | View commit history |
