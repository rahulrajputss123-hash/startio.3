# Start.io Video Player - Start.io (StartApp) SDK Android Native Integration

This repository contains a ready-to-build Android Native Kotlin project with Start.io SDK integration and automatic GitHub Actions APK CI/CD pipeline.

## Key Features Included
1. **Start.io Launch Interstitial Ad**: Triggers on app cold start and app reopen.
2. **Custom Video Player Rewarded Video Ad**: Preloads and shows Start.io Rewarded Video ads to unlock premium video content.
3. **Gradle KTS Setup**: Configured with official Start.io Maven repository and `com.startapp:inapp-sdk:4.11.0`.
4. **Automated GitHub Actions Workflow**: `.github/workflows/android.yml` automatically compiles and uploads `app-debug.apk` artifact on every commit.

## Building Debug APK Without Android Studio (GitHub Actions)
1. Push this repository to **GitHub.com**.
2. Go to the **Actions** tab in your repository.
3. The workflow will automatically run `./gradlew assembleDebug` using Java 17.
4. Download the generated `app-debug` artifact from the workflow run details and install the APK directly on your Android device!

## How to Run in Android Studio
1. Open **Android Studio** (Hedgehog / Jellyfish / Koala or newer).
2. Choose **Open an Existing Project** and select this directory.
3. Ensure your **Start.io App ID** (`207768706`) is set in `AndroidManifest.xml` and `MyApplication.kt`.
4. Connect an Android Device or Emulator (API level 24+).
5. Click **Run 'app'** (`Shift + F10`).

## Start.io Documentation & Support
- Portal: https://portal.start.io
- Android Integration Docs: https://support.start.io/hc/en-us
