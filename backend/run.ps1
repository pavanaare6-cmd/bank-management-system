#!/usr/bin/env powershell

# Bank Management System - PowerShell Build Script

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "Bank Management System - Build Tool" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mavenExists = $false
try {
    $mvnVersion = mvn -version 2>$null
    $mavenExists = $?
} catch {
    $mavenExists = $false
}

if (-not $mavenExists) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Exit 1
}

Write-Host "Maven version:" -ForegroundColor Green
mvn -version
Write-Host ""

# Check if pom.xml exists
if (-not (Test-Path "pom.xml")) {
    Write-Host "ERROR: pom.xml not found!" -ForegroundColor Red
    Write-Host "Please run this script from the backend directory" -ForegroundColor Yellow
    Exit 1
}

# Menu
Write-Host "Choose an option:" -ForegroundColor Cyan
Write-Host "1. Clean and build (download dependencies)"
Write-Host "2. Run the application"
Write-Host "3. Build and run"
Write-Host "4. Clean only"
Write-Host ""
$choice = Read-Host "Enter your choice (1-4)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "Cleaning and building..." -ForegroundColor Yellow
        mvn clean install
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Build failed!" -ForegroundColor Red
            Exit 1
        }
        Write-Host "Build completed successfully!" -ForegroundColor Green
    }
    "2" {
        Write-Host ""
        Write-Host "Running Spring Boot application..." -ForegroundColor Yellow
        Write-Host "Server will be available at: http://localhost:8080" -ForegroundColor Green
        Write-Host ""
        mvn spring-boot:run
    }
    "3" {
        Write-Host ""
        Write-Host "Cleaning, building and running..." -ForegroundColor Yellow
        mvn clean install
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Build failed!" -ForegroundColor Red
            Exit 1
        }
        Write-Host ""
        Write-Host "Starting Spring Boot application..." -ForegroundColor Yellow
        Write-Host "Server will be available at: http://localhost:8080" -ForegroundColor Green
        Write-Host ""
        mvn spring-boot:run
    }
    "4" {
        Write-Host ""
        Write-Host "Cleaning build artifacts..." -ForegroundColor Yellow
        mvn clean
        Write-Host "Clean completed!" -ForegroundColor Green
    }
    default {
        Write-Host "Invalid choice!" -ForegroundColor Red
        Exit 1
    }
}
