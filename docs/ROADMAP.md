# Mobile roadmap

The repository and internal code name are **Knightboard-Go**. The Android package ID remains stable so future public-name changes do not interrupt updates for existing users.

## Phase 1 — foundation

- [x] Compose app shell
- [x] Navigation
- [x] Camera permission
- [x] CameraX rear-camera preview
- [x] Touch corner calibration
- [x] 8×8 calibration grid preview
- [x] Persistent detection settings
- [x] Android CI and debug APK
- [x] Knightboard Go branding

## Phase 2 — image pipeline

- [ ] CameraX ImageAnalysis with latest-frame backpressure
- [ ] YUV-to-RGB conversion
- [ ] Perspective warp into a canonical board image
- [ ] Rotation and camera-side mapping
- [ ] Exposure and focus controls
- [ ] Saved board profiles

## Phase 3 — chess recording

- [x] Turn-aware manual move model
- [x] Virtual board
- [ ] Player and event setup
- [ ] Built-in clock
- [ ] PGN and FEN generation
- [ ] Game history

## Phase 4 — detection

- [ ] 64 individual square scores
- [ ] Sixteen independent 2×2 movement zones
- [ ] Clear, moving, blocked, and recovering states
- [ ] Ten-second temporal buffer
- [ ] ONNX piece recognition
- [ ] Legal candidate ranking
- [ ] Manual and automatic confirmation
- [ ] Accuracy Boost

## Phase 5 — correction and recovery

- [ ] Detection Wrong
- [ ] Illegal-position correction
- [ ] Manual virtual-board synchronization
- [ ] Two- and three-half-move recovery
- [ ] Temporal candidate confirmation

## Phase 6 — review and libraries

- [ ] Stockfish review
- [ ] Accuracy and average centipawn loss
- [ ] Opening Explorer
- [ ] Syzygy tablebases
- [ ] Training library
