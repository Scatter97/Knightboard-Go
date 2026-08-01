#!/usr/bin/env sh
set -eu
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
echo "Gradle 8.13 is required. Open the project in Android Studio or install Gradle 8.13." >&2
exit 1
