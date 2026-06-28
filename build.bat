@echo off
echo Formatting Windows build environment...

:: Create binary output directory
if not exist bin (
    mkdir bin
)

echo Locating Java source files...
:: Find all Java files and write to temporary sources.txt
dir /s /b src\*.java > sources.txt

:: Compile
echo Compiling Java application for Windows...
javac -d bin -cp "libs\flatlaf.jar;libs\svg-salamander.jar;libs\sqlite-jdbc.jar;libs\slf4j-api.jar;libs\slf4j-simple.jar" @sources.txt

:: Delete temporary sources.txt
del sources.txt

echo Compilation successful. Output written to 'bin/'.
