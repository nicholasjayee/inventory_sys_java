#!/bin/bash
set -e

# Run build script first
./build.sh

# Detect OS and set classpath separator
OS_NAME=$(uname -s 2>/dev/null || echo "Windows")
if [[ "$OS_NAME" == *"NT"* || "$OS_NAME" == *"MINGW"* || "$OS_NAME" == *"CYGWIN"* || "$OS_NAME" == "Windows" ]]; then
    CP_SEP=";"
else
    CP_SEP=":"
fi

CLASSPATH="bin${CP_SEP}libs/flatlaf.jar${CP_SEP}libs/svg-salamander.jar${CP_SEP}libs/sqlite-jdbc.jar${CP_SEP}libs/slf4j-api.jar${CP_SEP}libs/slf4j-simple.jar"

echo "Executing Database Migrations..."
java -cp "$CLASSPATH" com.inventory.db.DatabaseManager
