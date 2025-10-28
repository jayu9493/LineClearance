# LineClearance Android App

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A native Android application designed to streamline the process of requesting line clearance permits via SMS.

## 🚀 Features

- **Role-Based Authentication**: A database-driven login system for staff members (DE, JE, LI, etc.).
- **Substation & Feeder Selection**: A nested list view to first select a substation, then a specific feeder from real-world data.
- **Automated SMS Generation**: Creates an SMS with a pre-filled message and recipient phone number for the line clearance request.
- **Development-Friendly**: Easily bypass the login screen for testing by modifying `AndroidManifest.xml`.

## 📦 Installation & Setup

### Prerequisites
- Android Studio (latest stable version recommended)
- An Android device or emulator.

### Quick Start

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/jayu9493/LineClearance.git
    ```
2.  **Open in Android Studio & Sync Gradle**
3.  **Run the app**

## 📖 Usage

### Login Credentials
- **Username**: Use the 10-digit mobile number of any staff member from the pre-populated list.
- **Password**: The default password for all users is `password`.

The initial user list is located in `app/src/main/java/com/example/lineclearance/InitialData.kt`.

## 🛠️ Development

### Code Style
This project follows standard Kotlin conventions for Android.

### Key Files
- `MainActivity.kt`: Login screen logic.
- `PersonalDashboardActivity.kt`: The main dashboard screen after login.
- `SubstationActivity.kt` / `FeederActivity.kt`: Screens for LC requests.
- `AppDatabase.kt`: The Room database definition, including `User` and `LineClearancePermit` tables.

## 🤝 Contributing

We welcome contributions! Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to get started.

## 📄 License

This project is licensed under the MIT License.
