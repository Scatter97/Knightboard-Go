# Foundation device testing

Primary target: Samsung Galaxy S25 Ultra.

## Camera

- Rear camera opens after permission is granted.
- Preview fills the screen without freezing.
- Returning to Home releases the camera.
- Rotating the phone keeps the app usable.
- Denying permission shows a recoverable permission screen.

## Calibration

- Four taps are accepted in the documented order.
- A fifth tap is ignored.
- Undo removes the newest point.
- Reset removes all points.
- The completed polygon closes correctly.
- Internal grid lines follow a rectangular board and a perspective board.
- Save is disabled until four points exist.

## Settings

- Local64 V2 preference survives app restart.
- Diagnostics preference survives app restart.
- Detection mode survives app restart.

## Privacy

- No camera images appear in app storage.
- No network permission is requested.
