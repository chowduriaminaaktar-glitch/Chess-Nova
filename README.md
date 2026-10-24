# ChessNova — Learn • Practice • Master

ChessNova is a modern, offline-first Android chess application built with Kotlin and Jetpack Compose. It includes a 12-module structured learning curriculum, daily tactical puzzles, an offline local Minimax chess engine across 3 difficulty levels, local 2-player passless play, AI coaching (with offline fallback), and bilingual English & Bengali support.

---

## Automated APK Builds & Downloads

Every push to `main` and every version release tag automatically triggers a GitHub Actions workflow that builds an installable Android APK (`Chess-Nova.apk`).

### 1. How to Open the Repository's Actions Tab
1. Open your ChessNova repository on [GitHub](https://github.com).
2. Click the **Actions** tab in the top navigation bar (located between *Pull requests* and *Projects*).
3. In the left sidebar, click **Build ChessNova APK**.

---

### 2. How to Run the APK Build Manually
You can trigger a fresh build at any time without pushing code:
1. Go to the **Actions** tab.
2. Under **Workflows** in the left sidebar, select **Build ChessNova APK**.
3. On the right side above the workflow runs list, click the **Run workflow** dropdown button.
4. Select the `main` branch and click the green **Run workflow** button.
5. Wait for the workflow run to finish (usually 2–3 minutes). A green checkmark appears when the build succeeds.

---

### 3. How to Download the "Chess-Nova-APK" Artifact
Once any workflow run finishes:
1. Click on the completed workflow run (marked with a green checkmark).
2. Scroll down to the bottom of the summary page to the **Artifacts** section.
3. Click on **Chess-Nova-APK**.
4. GitHub will download a ZIP archive containing `Chess-Nova.apk`.
5. Extract the ZIP and install `Chess-Nova.apk` on your Android device (ensure "Install unknown apps" is enabled in Android Settings).

---

### 4. How to Download the APK from GitHub Releases
Official releases provide direct APK downloads without needing to unzip:
1. On the main page of your repository, look at the right sidebar and click **Releases** (or append `/releases` to your repository URL).
2. Find the latest release version (e.g., `v1.0.0`).
3. Under **Assets**, click **Chess-Nova.apk** to download the installable APK directly to your phone or computer.

---

### 5. How to Create a New Versioned Release
To publish an official release with attached APK:

#### Option A: Using Git (Command Line / Terminal)
```bash
# Ensure you are on the main branch with latest commits
git checkout main
git pull origin main

# Create a semver tag (e.g., v1.0.0)
git tag v1.0.0

# Push the tag to GitHub
git push origin v1.0.0
```
*The GitHub Actions workflow will automatically detect the `v*` tag, build the APK, create the GitHub Release, and attach `Chess-Nova.apk` automatically!*

#### Option B: Via the GitHub Web Interface
1. Go to your repository on GitHub.
2. In the right sidebar, click **Releases** -> **Draft a new release**.
3. Click **Choose a tag**, type your new version tag (e.g., `v1.0.0`), and select **Create new tag: v1.0.0 on publish**.
4. Enter a Release title (e.g., `ChessNova v1.0.0`).
5. Click **Publish release**.
6. The workflow will run and attach the compiled APK to the release.

---

## App Features

- **Home Screen**: Learning level, streaks, XP progress, Continue Learning, daily puzzle challenge, and quick navigation.
- **Learn Chess (12 Modules)**: Step-by-step curriculum with interactive demonstration boards, guided moves, quizzes, hints, and local persistence.
- **Play Chess**:
  - **Vs Computer**: Easy (~900 Elo), Medium (~1450 Elo), and Hard (~1850 Elo) powered by a pure-Kotlin local Minimax engine with Alpha-Beta pruning, PST, and Quiescence search.
  - **Local Two Player**: Smooth, uninterrupted two-player chess on a single device with optional board auto-flip and instant moves.
  - **Practice Mode**: Free-play sandbox with real-time evaluation and move hints.
  - **Full Move Undo**: One-tap undo anytime, rolling back turns cleanly.
- **Offline-First**: Built with Room database. All lessons, engine, puzzles, and rules run 100% offline without remote servers.
- **Language Support**: Seamless English and Bengali toggle.

---

## Technical Specifications

- **Language**: Kotlin 2.2.10
- **UI Framework**: Jetpack Compose with Material Design 3 (M3)
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 15 (API 36)
- **Local Persistence**: Android Room Database
- **Build System**: Gradle 9.3.1 with Android Gradle Plugin 9.1.1
