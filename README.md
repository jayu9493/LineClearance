# LineClearance Android App

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


A native Android application designed to streamline the process of requesting line clearance permits via SMS.

## 🚀 Features

- **User Authentication**: A basic login screen for authorized personnel.
- **Substation & Feeder Selection**: A nested list view to first select a substation, then a specific feeder from real-world data.
- **Automated SMS Generation**: Creates an SMS with a pre-filled message and recipient phone number for the line clearance request.
- **Development-Friendly**: Easily bypass the login screen for testing by modifying `AndroidManifest.xml`.

## 📦 Installation & Setup

### Prerequisites
- Android Studio (latest stable version recommended)
- An Android device or emulator (A physical device with an SMS app is required to test the SMS functionality).

### Quick Start

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/jayu9493/LineClearance.git
    ```
2.  **Open in Android Studio:**
    Open the cloned directory in Android Studio.
3.  **Sync Gradle:**
    Allow Android Studio to download and sync the project's dependencies (Gradle sync).
4.  **Run the app:**
    Select a target device and click the 'Run' button.

## 📖 Usage

### Running the App
- By default, the app starts at the substation selection screen to allow for quick testing.
- To re-enable the login screen, modify `app/src/main/AndroidManifest.xml` and move the `<intent-filter>` for the `LAUNCHER` action from `SubstationActivity` back to `MainActivity`.

### Test Credentials
- **Email**: `user@example.com`
- **Password**: `password`

## 🛠️ Development

### Code Style
This project follows standard Kotlin conventions for Android.

### Key Files
- `MainActivity.kt`: Login screen logic.
- `SubstationActivity.kt` / `FeederActivity.kt`: Main screens for selection.
- `app/src/main/res/layout/`: XML layout files for all activities.
- `app/build.gradle.kts`: Project dependencies.

### Build & Test (CLI)

If you prefer the command line, you can build and run tests with the Gradle wrapper:

```bash
./gradlew assembleDebug
./gradlew test
```

## 🤝 Contributing

We welcome contributions! Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to get started.

## 📄 License

This project is licensed under the MIT License.
