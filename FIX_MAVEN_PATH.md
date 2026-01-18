# Fix Maven PATH - Quick Solution

## ✅ Good News: Maven IS Installed!

Maven is installed at:
- **Location**: `C:\Program Files\Apache\apache-maven-3.9.12`
- **Version**: 3.9.12

## ❌ Problem: Maven is NOT in PATH

That's why `mvn` command is not recognized. Let's fix it!

## Solution: Add Maven to PATH

### Method 1: Using PowerShell (Quick - Recommended)

**Run PowerShell as Administrator:**

1. Right-click PowerShell → **Run as Administrator**
2. Run this command:
   ```powershell
   $mavenBinPath = "C:\Program Files\Apache\apache-maven-3.9.12\bin"
   $machinePath = [Environment]::GetEnvironmentVariable("Path", "Machine")
   $newPath = $machinePath + ";" + $mavenBinPath
   [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
   Write-Host "✅ Maven added to PATH! Please restart PowerShell." -ForegroundColor Green
   ```

3. **Close ALL PowerShell windows**
4. **Open a NEW PowerShell window**
5. Test: `mvn -version`

### Method 2: Using GUI (Easier if you prefer)

1. Press **Windows Key + R**
2. Type: `sysdm.cpl` and press Enter
3. Click **"Environment Variables"** button
4. Under **"System variables"** section:
   - Find **"Path"** → Click **"Edit"**
   - Click **"New"**
   - Add this path:
     ```
     C:\Program Files\Apache\apache-maven-3.9.12\bin
     ```
   - Click **"OK"** on all dialogs
5. **Close ALL PowerShell/Command Prompt windows**
6. Open a NEW PowerShell window
7. Test: `mvn -version`

### Method 3: Using the Script (Easiest)

1. Run PowerShell as Administrator
2. Navigate to project folder:
   ```powershell
   cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
   ```
3. Run the script:
   ```powershell
   .\add_maven_to_path.ps1
   ```
4. Restart PowerShell and test: `mvn -version`

## Verify Maven Works

After adding to PATH and restarting PowerShell:

```powershell
mvn -version
```

**Expected Output:**
```
Apache Maven 3.9.12
Maven home: C:\Program Files\Apache\apache-maven-3.9.12
Java version: 24.0.2
Java home: C:\Program Files\Java\jdk-24
...
```

## Quick Test Right Now

Even before adding to PATH, you can test Maven works:

```powershell
& "C:\Program Files\Apache\apache-maven-3.9.12\bin\mvn.cmd" -version
```

This should show Maven version (proving Maven is installed correctly).

## Troubleshooting

### Still Not Working After Adding to PATH?

1. **Did you restart PowerShell?**
   - Close ALL PowerShell windows
   - Open a BRAND NEW PowerShell window

2. **Check if PATH was added:**
   ```powershell
   $env:PATH -split ';' | Select-String maven
   ```
   Should show: `C:\Program Files\Apache\apache-maven-3.9.12\bin`

3. **Try refreshing environment:**
   ```powershell
   $env:PATH = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
   mvn -version
   ```

4. **Verify Maven files exist:**
   ```powershell
   Test-Path "C:\Program Files\Apache\apache-maven-3.9.12\bin\mvn.cmd"
   ```
   Should return: `True`

### If You Get "Access Denied"

- Make sure you're running PowerShell as **Administrator**
- Or use Method 2 (GUI) which doesn't require admin for User PATH

## After Maven Works

Once `mvn -version` works, you can:

1. **Navigate to project:**
   ```powershell
   cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
   ```

2. **Start Docker services:**
   ```powershell
   docker-compose up -d
   ```

3. **Run tests:**
   ```powershell
   mvn clean test
   ```

## Summary

- ✅ Maven IS installed at: `C:\Program Files\Apache\apache-maven-3.9.12`
- ❌ Maven is NOT in PATH
- ✅ Solution: Add `C:\Program Files\Apache\apache-maven-3.9.12\bin` to PATH
- ✅ Then restart PowerShell and test with `mvn -version`
