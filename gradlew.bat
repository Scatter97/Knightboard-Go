@echo off
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle 8.13 is required. Open the project in Android Studio or install Gradle 8.13.
  exit /b 1
)
gradle %*
