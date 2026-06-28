#!/bin/bash
set -e

# Detect OS and set classpath separator
OS_NAME=$(uname -s 2>/dev/null || echo "Windows")
if [[ "$OS_NAME" == *"NT"* || "$OS_NAME" == *"MINGW"* || "$OS_NAME" == *"CYGWIN"* || "$OS_NAME" == "Windows" ]]; then
    CP_SEP=";"
    echo "Detected Windows-based environment (OS: $OS_NAME). Classpath separator set to '$CP_SEP'."
else
    CP_SEP=":"
    echo "Detected Unix-based environment (OS: $OS_NAME). Classpath separator set to '$CP_SEP'."
fi

# Detect architecture for informative logs
ARCH_NAME=$(uname -m 2>/dev/null || echo "unknown")
echo "System architecture: $ARCH_NAME"

# Create output bin directory
mkdir -p bin

# Find all Java source files
echo "Locating Java source files..."
find src -name "*.java" > sources.txt

# Classpath libraries
CLASSPATH="libs/flatlaf.jar${CP_SEP}libs/svg-salamander.jar${CP_SEP}libs/sqlite-jdbc.jar${CP_SEP}libs/slf4j-api.jar${CP_SEP}libs/slf4j-simple.jar"

# Compile source files
echo "Compiling Java application..."
javac -d bin -cp "$CLASSPATH" @sources.txt

# Clean up temporary sources list
rm sources.txt

echo "Compilation successful. Output written to 'bin/'."
