# Script to Add Maven to PATH Environment Variable
# Run this script as Administrator

Write-Host "=== Adding Maven to PATH ===" -ForegroundColor Cyan
Write-Host ""

$mavenBinPath = "C:\Program Files\Apache\apache-maven-3.9.12\bin"

# Verify Maven exists
if (-not (Test-Path "$mavenBinPath\mvn.cmd")) {
    Write-Host "ERROR: Maven not found at: $mavenBinPath" -ForegroundColor Red
    Write-Host "Please verify Maven installation path" -ForegroundColor Yellow
    exit 1
}

Write-Host "Maven found at: $mavenBinPath" -ForegroundColor Green
Write-Host ""

# Check if already in PATH
$machinePath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
$currentSessionPath = $env:PATH

if ($machinePath -like "*$mavenBinPath*" -or $userPath -like "*$mavenBinPath*" -or $currentSessionPath -like "*$mavenBinPath*") {
    Write-Host "Maven path is already in PATH!" -ForegroundColor Yellow
    Write-Host "You may need to restart PowerShell for changes to take effect." -ForegroundColor Yellow
} else {
    Write-Host "Adding Maven to System PATH..." -ForegroundColor Yellow
    
    # Add to Machine PATH (requires admin)
    try {
        $newMachinePath = $machinePath + ";" + $mavenBinPath
        [Environment]::SetEnvironmentVariable("Path", $newMachinePath, "Machine")
        Write-Host "✅ Added Maven to System PATH (Machine level)" -ForegroundColor Green
        Write-Host ""
        Write-Host "IMPORTANT: Please close and reopen PowerShell for changes to take effect!" -ForegroundColor Cyan
    } catch {
        Write-Host "Failed to add to Machine PATH. Trying User PATH..." -ForegroundColor Yellow
        try {
            $newUserPath = $userPath + ";" + $mavenBinPath
            [Environment]::SetEnvironmentVariable("Path", $newUserPath, "User")
            Write-Host "✅ Added Maven to User PATH" -ForegroundColor Green
            Write-Host ""
            Write-Host "IMPORTANT: Please close and reopen PowerShell for changes to take effect!" -ForegroundColor Cyan
        } catch {
            Write-Host "ERROR: Could not add to PATH automatically." -ForegroundColor Red
            Write-Host "Please add manually:" -ForegroundColor Yellow
            Write-Host "  $mavenBinPath" -ForegroundColor White
        }
    }
}

Write-Host ""
Write-Host "After restarting PowerShell, verify with: mvn -version" -ForegroundColor Cyan
