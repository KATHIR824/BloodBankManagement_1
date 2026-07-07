@echo off
echo ========================================================
echo   Blood Bank Management System - JAR Packager
echo ========================================================
echo.

:: Detect Java Bin path
set "JAVA_BIN="
for /d %%i in ("C:\Program Files\Java\jdk-*") do (
    set "JAVA_BIN=%%i\bin"
)

if not defined JAVA_BIN (
    :: Fallback if not found in Program Files
    echo [WARNING] Could not find JDK in C:\Program Files\Java\jdk-*
    echo Trying to use system PATH...
    set "JAVAC_CMD=javac"
    set "JAR_CMD=jar"
) else (
    echo [INFO] Found JDK at: %JAVA_BIN%
    set "JAVAC_CMD=%JAVA_BIN%\javac.exe"
    set "JAR_CMD=%JAVA_BIN%\jar.exe"
)

set JAR_PATH=BloodBankManagement\lib\mysql-connector-j-9.7.0\mysql-connector-j-9.7.0\mysql-connector-j-9.7.0.jar

:: Create temp directory
rd /s /q temp_build 2>nul
mkdir temp_build
mkdir temp_build\classes

echo [INFO] Compiling Java classes...
"%JAVAC_CMD%" -d temp_build\classes -sourcepath BloodBankManagement\src -cp "%JAR_PATH%" BloodBankManagement\src\Main.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    rd /s /q temp_build 2>nul
    pause
    exit /b %errorlevel%
)

echo [INFO] Extracting JDBC driver classes...
cd temp_build\classes
"%JAR_CMD%" xf "..\..\%JAR_PATH%"

:: Remove signature files to avoid SecurityException
del /f /q META-INF\*.SF 2>nul
del /f /q META-INF\*.DSA 2>nul
del /f /q META-INF\*.RSA 2>nul
cd ..\..

echo [INFO] Creating Manifest file...
echo Manifest-Version: 1.0 > temp_build\MANIFEST.MF
echo Main-Class: Main >> temp_build\MANIFEST.MF

echo [INFO] Bundling into runnable JAR (BloodBankManagement.jar)...
"%JAR_CMD%" cfm BloodBankManagement.jar temp_build\MANIFEST.MF -C temp_build\classes .

echo [INFO] Cleaning up temporary build files...
rd /s /q temp_build 2>nul

echo.
echo ========================================================
echo [SUCCESS] Created runnable JAR: BloodBankManagement.jar
echo ========================================================
pause
