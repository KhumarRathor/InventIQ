# GitHub Upload Guide for InventIQ 🚀

Follow these steps to upload your InventIQ project to GitHub.

---

## Step 1: Create GitHub Repository

1. Go to https://github.com/KhumarRathor
2. Click the **"+"** button (top right) → **"New repository"**
3. Fill in:
   - **Repository name:** `InventIQ`
   - **Description:** `AI-Powered Autonomous Procurement System - B.Tech CSE Major Project`
   - **Public** (so recruiters can see it)
   - ✅ Check "Add a README file" - NO! We'll upload our own
   - **DO NOT** add .gitignore or license yet
4. Click **"Create repository"**

---

## Step 2: Prepare Your Local Files

### Create this folder structure:

```
InventIQ/
├── README.md                          (Download from outputs)
├── .gitignore                         (Download from outputs)
├── backend/                           (Copy from procurement-agent-phase4)
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── ai-service/                        (Copy from ai_service/files)
│   ├── ai_service.py
│   ├── requirements.txt
│   └── README.md
├── dashboard/                         (Copy from Downloads/files)
│   ├── dashboard.py
│   ├── dashboard_requirements.txt
│   └── README.md
├── database/                          (Create new folder)
│   └── schema.sql                     (Copy from outputs)
├── screenshots/                       (Create new - we'll add later)
└── docs/                             (Create new)
    └── synopsis.docx                  (Copy your synopsis)
```

### Commands to organize files:

```powershell
# Create main InventIQ folder
cd C:\Users\khuma\Downloads
mkdir InventIQ
cd InventIQ

# Copy backend
xcopy C:\Users\khuma\Downloads\procurement-agent-phase4\procurement-agent backend\ /E /I

# Copy AI service
mkdir ai-service
copy C:\Users\khuma\Downloads\ai_service\files\ai_service.py ai-service\
copy C:\Users\khuma\Downloads\ai_service\files\test_ai_service.py ai-service\
echo fastapi==0.115.12 > ai-service\requirements.txt
echo uvicorn==0.34.0 >> ai-service\requirements.txt
echo pydantic==2.10.6 >> ai-service\requirements.txt

# Copy dashboard
mkdir dashboard
copy C:\Users\khuma\Downloads\files\dashboard.py dashboard\
echo streamlit==1.31.1 > dashboard\requirements.txt
echo requests==2.31.0 >> dashboard\requirements.txt
echo pandas==2.2.0 >> dashboard\requirements.txt
echo plotly==5.18.0 >> dashboard\requirements.txt

# Create database folder
mkdir database
copy C:\Users\khuma\Downloads\outputs\inventory_schema_mysql.sql database\schema.sql

# Create docs folder
mkdir docs
copy C:\Users\khuma\Downloads\outputs\InventIQ_Synopsis.docx docs\

# Create screenshots folder (empty for now)
mkdir screenshots

# Copy README and .gitignore from outputs folder
copy C:\path\to\README.md .
copy C:\path\to\.gitignore .
```

---

## Step 3: Remove Sensitive Data

**IMPORTANT:** Before uploading, remove passwords from `application.properties`

```powershell
# Edit this file:
notepad backend\src\main\resources\application.properties
```

Change:
```properties
spring.datasource.password=your_actual_password
```

To:
```properties
spring.datasource.password=${DB_PASSWORD}
```

Add a note in README about setting environment variables.

---

## Step 4: Initialize Git & Upload

```powershell
# Navigate to InventIQ folder
cd C:\Users\khuma\Downloads\InventIQ

# Initialize Git
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit: InventIQ - AI-Powered Autonomous Procurement System"

# Add remote (replace with your actual repository URL)
git remote add origin https://github.com/KhumarRathor/InventIQ.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### If prompted for credentials:
- Username: `KhumarRathor`
- Password: Use **Personal Access Token** (not your account password)

### To create Personal Access Token:
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token → Check "repo" → Generate token
3. Copy token and use as password

---

## Step 5: Verify Upload

1. Go to https://github.com/KhumarRathor/InventIQ
2. Check that all folders are there:
   - ✅ backend/
   - ✅ ai-service/
   - ✅ dashboard/
   - ✅ database/
   - ✅ docs/
   - ✅ README.md
   - ✅ .gitignore

---

## Step 6: Add Screenshots (Later)

Once you have screenshots:
```powershell
# Add screenshots
copy screenshot1.png screenshots\dashboard-inventory.png
copy screenshot2.png screenshots\ai-predictions.png
copy screenshot3.png screenshots\purchase-orders.png
copy screenshot4.png screenshots\alerts.png

# Update repository
git add screenshots/
git commit -m "Add dashboard screenshots"
git push
```

---

## Common Issues & Solutions

### Issue: "git is not recognized"
**Solution:** Install Git from https://git-scm.com/downloads

### Issue: Authentication failed
**Solution:** Use Personal Access Token instead of password

### Issue: Files too large
**Solution:** Already handled by .gitignore (removes target/, __pycache__, etc.)

### Issue: Cannot push
**Solution:** 
```powershell
git pull origin main --allow-unrelated-histories
git push -u origin main
```

---

## Next Steps After Upload

1. ✅ Add GitHub repository link to your resume
2. ✅ Update LinkedIn with project link
3. ✅ Take screenshots of running dashboard
4. ✅ Create demo video (optional)
5. ✅ Deploy dashboard to Streamlit Cloud (next phase)

---

**Congratulations! Your code is now on GitHub! 🎉**

Repository: https://github.com/KhumarRathor/InventIQ
