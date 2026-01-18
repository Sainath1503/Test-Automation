# Install Maven Now - Step by Step Guide

## ❌ Current Status: Maven is NOT installed

## Quick Installation (5 Minutes)

### Step 1: Download Maven ⬇️

1. **Open your web browser**
2. **Go to**: https://maven.apache.org/download.cgi
3. **Download**: `apache-maven-3.9.5-bin.zip`
   - Direct link: https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip

### Step 2: Extract the ZIP File 📦

1. **Right-click** the downloaded `apache-maven-3.9.5-bin.zip`
2. **Select**: "Extract All..." or use 7-Zip/WinRAR
3. **Extract to**: `C:\Program Files\Apache\maven`
4. You should have: `C:\Program Files\Apache\maven\apache-maven-3.9.5`

**Important**: Make sure the folder structure is:
```
C:\Program Files\Apache\maven\apache-maven-3.9.5\
  ├── bin\
  ├── boot\
  ├── conf\
  └── ...
```

### Step 3: Add Maven to PATH 🔧

**Method 1: Using GUI (Easier)**

1. Press **Windows Key + R**
2. Type: `sysdm.cpl` and press Enter
3. Click **"Environment Variables"** button
4. Under **"System variables"** section, find **"Path"** and click **"Edit"**
5. Click **"New"** button
6. Add this path:
   ```
   C:\Program Files\Apache\maven\apache-maven-3.9.5\bin
   ```
7. Click **"OK"** on all dialogs
8. **Close ALL PowerShell/Command Prompt windows**

**Method 2: Using PowerShell (Run as Administrator)**

1. Right-click PowerShell → **Run as Administrator**
2. Run this command (adjust path if you installed to different location):
   ```powershell
   $mavenPath = "C:\Program Files\Apache\maven\apache-maven-3.9.5\bin"
   $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
   [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenPath", "Machine")
   ```

### Step 4: Verify Installation ✅

**IMPORTANT**: Close and reopen PowerShell (or open a NEW PowerShell window)

Then run:
```powershell
mvn -version
```

**Expected Output:**
```
Apache Maven 3.9.5
Maven home: C:\Program Files\Apache\maven\apache-maven-3.9.5
Java version: 24.0.2, vendor: Oracle Corporation
Java home: C:\Program Files\Java\jdk-24
...
```

If you see version information, **Maven is installed successfully!** ✅

## Troubleshooting

### Still Getting "mvn is not recognized"?

1. **Did you restart PowerShell?** 
   - Close ALL PowerShell windows and open a NEW one

2. **Check if Maven folder exists:**
   ```powershell
   Test-Path "C:\Program Files\Apache\maven\apache-maven-3.9.5\bin\mvn.cmd"
   ```
   Should return: `True`

3. **Verify PATH was added:**
   ```powershell
   $env:PATH -split ';' | Select-String maven
   ```
   Should show Maven path

4. **Try adding to User PATH instead:**
   - In Environment Variables, edit **"User variables"** → **"Path"**
   - Add: `C:\Program Files\Apache\maven\apache-maven-3.9.5\bin`
   - Restart PowerShell

### Alternative Installation Locations

If you don't have admin rights, install to user folder:

1. Extract to: `C:\Users\Ashish\Apache\maven\apache-maven-3.9.5`
2. Add to User PATH: `C:\Users\Ashish\Apache\maven\apache-maven-3.9.5\bin`

## After Installation

Once Maven is installed, you can:

1. **Verify:**
   ```powershell
   mvn -version
   ```

2. **Navigate to project:**
   ```powershell
   cd "C:\Users\Ashish\OneDrive\Desktop\AutoMention Project"
   ```

3. **Start Docker:**
   ```powershell
   docker-compose up -d
   ```

4. **Run tests:**
   ```powershell
   mvn clean test
   ```

## Quick Checklist

- [ ] Downloaded apache-maven-3.9.5-bin.zip
- [ ] Extracted to C:\Program Files\Apache\maven
- [ ] Added to System PATH: C:\Program Files\Apache\maven\apache-maven-3.9.5\bin
- [ ] Closed and reopened PowerShell
- [ ] Verified with: mvn -version

## Need Help?

If you encounter issues:
1. Check that Java is installed: `java -version` (should work - you have Java 24)
2. Verify Maven folder structure is correct
3. Make sure you restarted PowerShell after adding to PATH
4. Try installing to a user folder if you don't have admin rights
