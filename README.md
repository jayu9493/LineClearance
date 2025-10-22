# Line Clearance App

An Android application designed to streamline the process of requesting line clearance permits via SMS. Authorized users can select a substation and feeder to automatically generate a pre-formatted permit request.

## Features

*   **User Authentication:** A basic login screen for authorized personnel.
*   **Substation & Feeder Selection:** A nested list view to first select a substation, then a specific feeder.
*   **Automated SMS Generation:** Creates an SMS with a pre-filled message and recipient phone number for the line clearance request.

## How to Run

1.  Open the project in Android Studio.
2.  Let Gradle sync the project dependencies.
3.  Run the app on a physical Android device. An emulator may not have an SMS app, which is required for the final step.

## Testing

To bypass the login screen for development, the `AndroidManifest.xml` has been modified to launch `SubstationActivity` directly. To re-enable the login flow, move the `<intent-filter>` back to the `MainActivity` declaration.

*   **Login Email:** `user@example.com`
*   **Login Password:** `password`
