@echo off
setlocal
set ROOT_DIR=%~dp0
set GRADLE_VERSION=8.2.1
set CACHE=%ROOT_DIR%.gradle-dist\gradle-%GRADLE_VERSION%
if not exist "%CACHE%\bin\gradle.bat" (
  echo Bootstrapping Gradle %GRADLE_VERSION%...
  if not exist "%ROOT_DIR%.gradle-dist" mkdir "%ROOT_DIR%.gradle-dist"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%ROOT_DIR%.gradle-dist\gradle.zip'; Expand-Archive -Force '%ROOT_DIR%.gradle-dist\gradle.zip' '%ROOT_DIR%.gradle-dist'"
)
call "%CACHE%\bin\gradle.bat" %*
endlocal
