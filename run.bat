@echo off
echo ========================================================
echo   Blood Bank Management System - Compiler and Runner
echo ========================================================
echo.

:: Check Java compiler
where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] JDK (javac) is not installed or not in your PATH.
    echo Please install JDK 21 or later and add it to your environment variables.
    pause
    exit /b 1
)

:: Create out directory if not exists
if not exist out mkdir out

echo [INFO] Compiling Java source files...
javac -d out -sourcepath BloodBankManagement/src -cp "BloodBankManagement/lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar" BloodBankManagement/src/Main.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [INFO] Compilation successful.
echo [INFO] Starting application...
echo.

java -cp "out;BloodBankManagement/lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar" Main

if %errorlevel% neq 0 (
    echo.
    echo [WARNING] Application exited with non-zero code or failed to launch.
    echo Make sure your database connection details in DBConnection.java are correct
    echo and that MySQL server is running.
)
pause
