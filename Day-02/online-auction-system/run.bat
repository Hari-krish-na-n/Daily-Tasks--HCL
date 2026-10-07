@echo off
echo ========================================================
echo   Day 02 - Online Auction System Fundamentals Demo
echo ========================================================
echo.
echo Compiling project with Maven...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================================
echo   Running AuctionDataDemo...
echo ========================================================
echo.
call mvn exec:java -Dexec.mainClass="com.auction.AuctionDataDemo"

echo.
pause
