Got it! Here's your updated `README.md` with the **Installation** section removed, and the rest adjusted accordingly:

---

# ReText - Recover Deleted Notifications and Messages

[![GitHub Stars](https://img.shields.io/github/stars/ilokeshmeena/ReText?style=social)](https://github.com/ilokeshmeena/ReText)
[![GitHub Forks](https://img.shields.io/github/forks/ilokeshmeena/ReText?style=social)](https://github.com/ilokeshmeena/ReText)
[![GitHub Issues](https://img.shields.io/github/issues/ilokeshmeena/ReText)](https://github.com/ilokeshmeena/ReText/issues)
[![License](https://img.shields.io/github/license/ilokeshmeena/ReText)](https://github.com/ilokeshmeena/ReText/blob/main/LICENSE)

**ReText** is a user-friendly Android application designed to help you recover and view deleted notifications and messages from your favorite apps. Never miss important information again!

---

## Features

- **Comprehensive Recovery:** ReText monitors and stores incoming notifications, allowing you to retrieve them even after they’ve been dismissed or deleted.
- **App-Specific Tracking:** View deleted notifications grouped by the originating app for easy access.
- **Intuitive Interface:** Clean, futuristic, and user-friendly design for seamless navigation.
- **Lightweight and Efficient:** Minimal performance and battery impact.
- **Customizable Monitoring:** Select which apps ReText should monitor.

---

## Build Steps (For Developers)

If you'd like to build the app locally or contribute:

### Prerequisites

- Android Studio Flamingo or higher
- JDK 17 or later
- Gradle 8+
- Git

### Clone the Repository

```bash
git clone https://github.com/ilokeshmeena/ReText.git
cd ReText
```

### Open in Android Studio

1. Launch Android Studio.
2. Select "Open an existing project."
3. Navigate to the cloned directory and open it.

### Build the Project

1. Allow Gradle to sync and download dependencies.
2. Connect an Android device or start an emulator.
3. Click **Run > Run ‘app’** or use `Shift + F10`.

> The app uses **Room Database** for storing notifications and **JXML Layout with View Binding** for UI.

---

## How to Use

1. **Grant Notification Access:** On first launch, grant ReText access to your notifications.
2. **Choose Apps to Monitor:** Select the apps whose deleted notifications you want to save.
3. **View Recovered Notifications:** Access them categorized by app from the home screen.
4. **Adjust Settings:** Modify preferences as needed.

---

## Permissions

- **Notification Access:** Required to capture and store deleted notifications.  
  ReText **does not** send or store any data externally — everything is kept securely on the device.

---

## Contributing

Want to improve ReText? We welcome contributions!

- ⭐ Star the repo to show support.
- 🐛 Report bugs via [Issues](https://github.com/ilokeshmeena/ReText/issues).
- 🔧 Submit pull requests via [Pull Requests](https://github.com/ilokeshmeena/ReText/pulls).

Before submitting, please:

- Follow the existing code style.
- Test your changes.
- Provide a clear description of what you’ve done.

---

## License

ReText is licensed under the [MIT License](https://github.com/ilokeshmeena/ReText/blob/main/LICENSE).

---

## Support

Need help or found a bug? [Open an issue](https://github.com/ilokeshmeena/ReText/issues).

---

**Thanks for using ReText!** 🚀

---

Let me know if you want me to draft a `CONTRIBUTING.md` or `PR` template as well!
