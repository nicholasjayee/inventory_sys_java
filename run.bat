@echo off
echo Building project...
call build.bat

if %ERRORLEVEL% neq 0 (
    echo Build failed. Exiting...
    pause
    exit /b %ERRORLEVEL%
)

echo Starting Botanical Logistics Application on Windows...
java -cp "bin;libs\flatlaf.jar;libs\svg-salamander.jar;libs\sqlite-jdbc.jar;libs\slf4j-api.jar;libs\slf4j-simple.jar" com.inventory.app.Main
