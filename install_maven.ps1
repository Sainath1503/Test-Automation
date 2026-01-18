# Maven Installation Helper Script for Windows
# This script helps download and configure Maven

Write-Host "=== Maven Installation Helper ===" -ForegroundColor Green
Write-Host ""

# Check if Maven is already installed
Write-Host "Checking if Maven is already installed..." -ForegroundColor Yellow
$mvnPath = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvnPath) {
    Write-Host "Maven is already installed at: $($mvnPath.Source)" -ForegroundColor Green
    Write-Host "Version:"
    & mvn -version
    exit 0
}

Write-Host "Maven not found in PATH. Let's install it..." -ForegroundColor Yellow
Write-Host ""

# Create installation directory
$installDir = "$env:PROGRAMFILES\Apache\maven"
$downloadDir = "$env:TEMP\maven-download"

# Create directories
if (-not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Path $installDir -Force | Out-Null
    Write-Host "Created directory: $installDir" -ForegroundColor Green
}

if (-not (Test-Path $downloadDir)) {
    New-Item -ItemType Directory -Path $downloadDir -Force | Out-Null
}

Write-Host ""
Write-Host "Manual Installation Steps:" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Download Maven:" -ForegroundColor Yellow
Write-Host "   URL: https://maven.apache.org/download.cgi" -ForegroundColor White
Write-Host "   Download: apache-maven-3.9.5-bin.zip (or latest version)" -ForegroundColor White
Write-Host ""
Write-Host "2. Extract the zip file to:" -ForegroundColor Yellow
Write-Host "   $installDir" -ForegroundColor White
Write-Host ""
Write-Host "3. Add Maven to PATH:" -ForegroundColor Yellow
Write-Host "   Add this path to System PATH:" -ForegroundColor White
Write-Host "   $installDir\apache-maven-3.9.5\bin" -ForegroundColor White
Write-Host ""
Write-Host "   OR run this PowerShell command (as Administrator):" -ForegroundColor Yellow
Write-Host "   [Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'Machine') + ';$installDir\apache-maven-3.9.5\bin', 'Machine')" -ForegroundColor White
Write-Host ""
Write-Host "4. Restart PowerShell/Command Prompt" -ForegroundColor Yellow
Write-Host ""
Write-Host "5. Verify installation:" -ForegroundColor Yellow
Write-Host "   mvn -version" -ForegroundColor White
Write-Host ""

# Offer to open download page
$openBrowser = Read-Host "Would you like to open the Maven download page in your browser? (Y/N)"
if ($openBrowser -eq "Y" -or $openBrowser -eq "y") {
    Start-Process "https://maven.apache.org/download.cgi"
    Write-Host "Download page opened in browser." -ForegroundColor Green
}

Write-Host ""
Write-Host "After installation, restart PowerShell and run: mvn -version" -ForegroundColor Cyan
