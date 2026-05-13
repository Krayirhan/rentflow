@echo off
REM RentFlow Desktop Application - Windows Startup Script
REM Usage: run-app.bat [dev|prod]

setlocal
set PROFILE=dev
if not "%~1"=="" set PROFILE=%~1

echo.
echo ====================================
echo  RentFlow Desktop Application v4.0
echo ====================================
echo.
echo Starting in %PROFILE% mode...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher and add it to PATH
    pause
    exit /b 1
)

REM Get the directory where this script is located
set SCRIPT_DIR=%~dp0

REM Set database password if production mode
if "%PROFILE%"=="prod" (
    echo.
    echo ============================================
    echo  Production Mode - Database Configuration
    echo ============================================
    echo Make sure PostgreSQL is running on localhost:5432
    echo with database name: rentflow
    echo username: rentflow
    echo.
    set /p DB_PASSWORD="Enter PostgreSQL password for 'rentflow' user: "
    set DB_PASSWORD_ARG=-DDB_PASSWORD=%DB_PASSWORD%
)

REM Run the application
echo Launching RentFlow...
cd /d "%SCRIPT_DIR%"

if exist "target\rentflow-*-exec.jar" (
    java %DB_PASSWORD_ARG% -jar target\rentflow-*-exec.jar --spring.profiles.active=%PROFILE%
) else (
    echo.
    echo Error: Application JAR not found!
    echo Please run: mvn clean package
    echo.
    pause
    exit /b 1
)

endlocal
