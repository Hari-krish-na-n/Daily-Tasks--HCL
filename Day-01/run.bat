@echo off
echo ===================================================
echo   Day 01 - Java Platform Basics Demo
echo ===================================================
echo.
echo Compiling PlatformInfo.java...
javac PlatformInfo.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation successful! Running PlatformInfo...
echo.
java PlatformInfo

echo.
echo ===================================================
echo   Inspecting Bytecode (javap -c)
echo ===================================================
javap -c PlatformInfo

echo.
pause
