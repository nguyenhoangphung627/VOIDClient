#!/usr/bin/env sh
# Portable fallback wrapper for environments where the standard Gradle wrapper JAR is unavailable.
# It downloads a local Gradle distribution on first use, then delegates to it.
set -eu
ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
GRADLE_VERSION=8.2.1
CACHE="$ROOT_DIR/.gradle-dist/gradle-$GRADLE_VERSION"
if [ ! -x "$CACHE/bin/gradle" ]; then
  mkdir -p "$ROOT_DIR/.gradle-dist"
  ZIP="$ROOT_DIR/.gradle-dist/gradle-$GRADLE_VERSION-bin.zip"
  if command -v curl >/dev/null 2>&1; then
    curl -L --fail --retry 2 "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$ZIP"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIP" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  else
    echo "curl or wget is required to bootstrap Gradle $GRADLE_VERSION" >&2
    exit 1
  fi
  if command -v unzip >/dev/null 2>&1; then
    unzip -q -o "$ZIP" -d "$ROOT_DIR/.gradle-dist"
  else
    echo "unzip is required to bootstrap Gradle $GRADLE_VERSION" >&2
    exit 1
  fi
fi
exec "$CACHE/bin/gradle" "$@"
