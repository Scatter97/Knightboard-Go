# Knightboard Go Android architecture

## Foundation

The Android app is native Kotlin with Jetpack Compose and CameraX. It does not embed the Python desktop interface.

```text
Compose UI
   │
   ├── Home and navigation
   ├── Camera permission and preview
   ├── Touch calibration
   └── Persistent settings
          │
          ▼
Future game-session state machine
          │
          ├── CameraX ImageAnalysis
          ├── OpenCV board warp
          ├── Local64 Detection V2
          ├── ONNX piece recognition
          ├── Legal move resolver
          └── Multi-move recovery
```

## Current packages

- `ui.home` — feature menu
- `ui.camera` — CameraX preview and board guide
- `ui.calibration` — four-point calibration interaction and grid preview
- `ui.settings` — persistent detection preferences
- `domain.calibration` — platform-independent calibration state and interpolation
- `data.settings` — Preferences DataStore repository

## State rules

Camera frames remain outside Compose state. Compose owns only user-visible state such as permission, calibration points, settings, and errors.

The future vision pipeline will use `ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST`, ensuring that a slow detector drops stale frames instead of blocking the live preview.

## Future modules

The project begins as one app module to keep the first build easy to validate. It will split into `core` and `feature` modules when the first end-to-end recording flow is stable. Premature modularization would slow early camera testing without improving user-visible behaviour.
