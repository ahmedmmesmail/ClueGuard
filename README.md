# 🔐 ClueGuard

**A Kotlin Multiplatform Password Manager for Android & iOS.**

ClueGuard is a password manager project built with Kotlin Multiplatform and Compose Multiplatform, with the goal of providing a simple, secure, and user-friendly way to manage account credentials across mobile platforms.

## ✨ Project Goals

* 🔑 **Credential Management** — Store and manage account credentials in one place.
* 🛡️ **Security First** — Design secure credential storage with encryption and platform-protected key management.
* 📱 **Cross-Platform** — Share application logic and UI across Android and iOS.
* 💾 **Local Persistence** — Use Room for structured local data storage.
* 🎨 **Modern UI** — Build a consistent interface with Compose Multiplatform.

> **Security Notice:** ClueGuard is a work in progress. Do not store real passwords or sensitive credentials until the encryption, key management, and vault-locking mechanisms have been implemented and tested.

## 🛠️ Tech Stack

| Technology               | Purpose                                               |
|--------------------------|-------------------------------------------------------|
| Kotlin Multiplatform     | Shared application logic                              |
| Compose Multiplatform    | Cross-platform user interface                         |
| Room Database            | Local database persistence                            |
| Navigation Compose       | Navigate between screens and manage navigation routes |
| Kotlin Coroutines & Flow | Asynchronous operations and reactive data streams     |
| Android Keystore         | Planned Android encryption-key protection             |
| Apple Keychain           | Planned iOS secret and key protection                 |

*Platform security integrations are planned and should not be considered implemented until verified in the codebase.*

## 📂 Project Structure

```text
ClueGuard/
├── androidApp/                 # Android application entry point
├── iosApp/
│   └── iosApp/                 # iOS application entry point
└── shared/
    └── src/
        ├── commonMain/
        │   ├── composeResources/       # Shared Compose resources
        │   └── kotlin/com/codenytra/amme/clueguard/
        │       ├── App.kt              # Root composable
        │       ├── Platform.kt         # expect declarations
        │       ├── navigation/
        │       │   └── AppNavigation.kt
        │       ├── data/
        │       │   ├── database/       # Database, DatabaseProvider, PasswordDao
        │       │   ├── model/          # PasswordEntity
        │       │   └── repository/     # Repositories
        │       ├── security/           # Encryption (expect/actual)
        │       ├── features/
        │       │   ├── home/           # HomeScreen, HomeViewModel
        │       │   └── ...             # Other features
        │       └── ui/theme/           # Colors, typography, theme
        ├── androidMain/                # Android-specific implementations
        │   └── kotlin/.../clueguard/
        │       ├── Database.android.kt
        │       ├── Platform.android.kt
        │       └── security/           # Android Keystore
        ├── iosMain/                    # iOS-specific implementations
        │   └── kotlin/.../clueguard/
        │       ├── Database.ios.kt
        │       ├── Platform.ios.kt
        │       ├── MainViewController.kt
        │       └── security/           # iOS Keychain
        └── commonTest/                 # Shared tests, if configured
```

### Module Overview

* **`androidApp/`** — Android application module and entry point.
* **`iosApp/iosApp/`** — Native iOS application entry point, with SwiftUI integration where needed.
* **`shared/src/commonMain/`** — Shared UI, application logic, and common data-layer code.
* **`shared/src/androidMain/`** — Android-specific functionality and platform integrations.
* **`shared/src/iosMain/`** — iOS-specific functionality and platform integrations.

## 🚀 Getting Started

### Prerequisites

* Android Studio with Kotlin Multiplatform support.
* A compatible JDK configured for the project.
* Xcode and the required Apple development tools for building and running the iOS application.
* The Android SDK and required project SDK versions.

### Run on Android

From the project root, run:

```bash
./gradlew :androidApp:assembleDebug
```

To install and run the application, open the project in Android Studio, select the Android run configuration, and launch it on an emulator or connected device.

On Windows PowerShell, use:

```powershell
.\gradlew :androidApp:assembleDebug
```

### Run on iOS

1. Open the `iosApp` directory in Xcode.
2. Select the iOS application scheme.
3. Choose an iOS simulator or a connected device.
4. Build and run the application.

Depending on your project configuration, you may need to synchronize Gradle and configure the appropriate Apple development tools first.

## 🔒 Security Roadmap

Security is a core design goal for ClueGuard. Planned security work includes:

* [ ] Encrypt stored credentials before writing them to the database.
* [ ] Protect encryption keys using platform security facilities.
* [ ] Implement secure vault unlocking and automatic locking.
* [ ] Prevent passwords and encryption keys from appearing in application logs.
* [ ] Test encryption, decryption, and tamper handling.
* [ ] Define a secure backup and recovery strategy.
* [ ] Review clipboard handling when copying credentials.

## 🗺️ Roadmap

* [ ] Complete credential creation, viewing, editing, and deletion.
* [ ] Add account search and organization.
* [ ] Implement and test encrypted local storage.
* [ ] Add vault-locking and authentication flows.
* [ ] Build and verify Android and iOS releases.
* [ ] Add automated tests for the data and security layers.

## ⚠️ Disclaimer

ClueGuard is an educational and development project. Its inclusion of security-related technologies or planned features does not, by itself, establish that the application is secure. Do not use it to store real credentials until the relevant security controls have been implemented, reviewed, and tested.

## [📄 License](LICENSE.md)
