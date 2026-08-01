# Knightboard Go

Knightboard Go is the native Android companion for recording and reviewing over-the-board chess.

## First playable foundation: 0.2.0-alpha01

The first milestone provides a real Android application foundation rather than a desktop-code wrapper:

- Kotlin and Jetpack Compose application
- Knightboard Go branding and Material 3 navigation
- Android camera permission flow
- Live rear-camera preview using CameraX
- 8×8 alignment guide
- Touch-based four-corner board calibration
- Perspective-style grid preview across the selected quadrilateral
- Persistent Local64 V2, diagnostics, and detection-mode settings using DataStore
- Playable virtual chessboard with turn-aware manual moves
- Move list and reset control for manual position correction
- Unit tests for calibration state and grid interpolation
- GitHub Actions lint, tests, and debug APK build

The app does **not** detect chess moves from the camera yet. Local64 V2, ONNX piece recognition, PGN recording, clocks, correction, and multi-move recovery are subsequent milestones. The virtual board is included now so the same game flow can later accept manual corrections and detected moves.

## Build locally

Requirements:

- Android Studio with Android SDK 36
- JDK 17
- Gradle 8.13 when building outside Android Studio

Open the repository in Android Studio and run the `app` configuration, or use:

```bash
./gradlew assembleDebug
```

The APK will be created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## First device test

1. Install the debug APK on an Android phone.
2. Open **Record OTB Game**.
3. Grant camera permission.
4. Place the board inside the guide.
5. Choose **Calibrate board**.
6. Tap top-left, top-right, bottom-right, then bottom-left.
7. Confirm the generated 8×8 grid follows the playing squares.

## Branding

`Knightboard-Go` is the permanent repository and code name. The Android display name is currently **Knightboard Go** and can be changed later without changing the application ID or affecting existing installations.

## Privacy

The foundation build displays the camera preview but does not upload or save camera frames.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Testing checklist](docs/DEVICE_TESTING.md)

Copyright © 2026 Joshua Wang. All rights reserved.
