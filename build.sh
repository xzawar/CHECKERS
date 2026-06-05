#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# build.sh  –  Compile & run the Checkers game from source
# Requires:  JDK 8 or later (must have javac on PATH)
# ─────────────────────────────────────────────────────────────────────────────
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src"
BIN="$ROOT/bin"
JAR="$ROOT/dist/CHECKERS.jar"

echo "=== Compiling Checkers ==="
mkdir -p "$BIN"
find "$SRC" -name '*.java' > /tmp/sources.txt
javac -d "$BIN" @/tmp/sources.txt
echo "Compiled OK"

echo "=== Packaging JAR ==="
mkdir -p "$ROOT/dist"
jar cfe "$JAR" checkers.ui.gui.CheckersGUI -C "$BIN" .
echo "JAR created: $JAR"

echo "=== Launching ==="
java -jar "$JAR"
