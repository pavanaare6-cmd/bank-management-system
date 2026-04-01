@echo off
REM Bank Management System - Build and Run Script
REM This script builds the backend and starts the server

setlocal enabledelayedexpansion

echo.
echo ====================================
echo Bank Management System - Build Tool
echo ====================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your system PATH
    echo Visit: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if we're in the right directory
if not exist "pom.xml" (
    echo ERROR: pom.xml not found!
    echo Please run this script from the backend directory
    pause
    exit /b 1
)

echo Maven version:
mvn -version
echo.

REM Menu
echo Choose an option:
echo 1. Clean and build (download dependencies)
echo 2. Run the application
echo 3. Build and run
echo 4. Clean only
echo.
set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" (
    echo.
    echo Cleaning and building...
    mvn clean install
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed!
        pause
        exit /b 1
    )
    echo Build completed successfully!
    pause
) else if "%choice%"=="2" (
    echo.
    echo Running Spring Boot application...
    mvn spring-boot:run
) else if "%choice%"=="3" (
    echo.
    echo Cleaning, building and running...
    mvn clean install
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed!
        pause
        exit /b 1
    )
    echo.
    echo Starting Spring Boot application...
    mvn spring-boot:run
) else if "%choice%"=="4" (
    echo.
    echo Cleaning build artifacts...
    mvn clean
    echo Clean completed!
    pause
) else (
    echo Invalid choice!
    pause
    exit /b 1
)

pause
