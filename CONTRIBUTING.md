# Contributing to LineClearance Android App

Thank you for your interest in contributing! We welcome contributions from everyone.

## 🎯 How to Contribute

### Reporting Bugs

1.  **Search Existing Issues**: Check if the bug has already been reported.
2.  **Create a New Issue**: Use the [bug report template](.github/ISSUE_TEMPLATE/bug_report.md).
3.  **Provide Details**: Include steps to reproduce, what you expected to happen, and what actually happened. Screenshots and logs are very helpful.

### Suggesting Features

1.  **Check for Existing Requests**: Look through open and closed issues to see if the feature has already been suggested.
2.  **Use the Feature Template**: Use the [feature request template](.github/ISSUE_TEMPLATE/feature_request.md) to create a new request.
3.  **Explain the Value**: Describe the problem the feature solves and who would benefit from it.

### Code Contributions

#### Development Workflow

1.  **Fork the Repository** and clone it to your local machine.
2.  **Create a Feature Branch**:
    ```bash
    git checkout -b feature/your-feature-name
    # or for bug fixes:
    git checkout -b fix/issue-you-are-fixing
    ```
3.  **Make Your Changes** in Android Studio.
    - Follow the existing code style.
    - Update documentation or comments where necessary.
4.  **Test Your Changes** by running the app on an emulator or physical device.
5.  **Commit Your Changes** with a clear and descriptive message:
    ```bash
    git add .
    git commit -m "feat: Describe your new feature"
    ```
6.  **Push and Create a Pull Request** against the `main` branch of the original repository.

## 🏗️ Development Setup

### Prerequisites
- Android Studio (latest stable version)
- Kotlin Plugin
- A physical Android device to test SMS functionality.

### Local Development

1.  Clone the repository and open it in Android Studio.
2.  Let Gradle sync the project and download all dependencies.
3.  Run the app using the standard 'Run' configuration.

## 📏 Code Standards

- Follow the official [Kotlin style guide](https://kotlinlang.org/docs/coding-conventions.html).
- Use meaningful names for variables, functions, and classes.
- Add comments for complex or non-obvious logic.

## ❓ Questions?

- Open a discussion in GitHub Discussions.
- Comment on a relevant issue.

Thank you for contributing! 🎉
