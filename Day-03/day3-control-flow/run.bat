@echo off
REM ============================================================
REM Day 3: Online Auction System - Control Flow + Maven Runner
REM ============================================================

echo ============================================================
echo   Building and Running Day 3 - Online Auction System
echo ============================================================

REM Step 1: Build the Maven project
echo [1/2] Compiling and packaging with Maven...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven build failed!
    exit /b %ERRORLEVEL%
)

REM Step 2: Execute the console application
echo.
echo [2/2] Launching AuctionConsoleApp...
echo ============================================================
java -jar target\day3-control-flow-1.0-SNAPSHOT.jar

pause
