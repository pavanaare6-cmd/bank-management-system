# Bank Management System - Setup Guide

## Prerequisites

Your system has Java 21 installed ✅
You need Maven for the backend

## Step 1: Install Maven

### Option A: Download Maven (Recommended)
1. Go to https://maven.apache.org/download.cgi
2. Download **Apache Maven 3.9.5** (or latest)
3. Extract to `C:\Program Files\Apache\maven` (or any location)
4. Add to System Environment Variables:
   - **MAVEN_HOME**: `C:\Program Files\Apache\maven`
   - **PATH**: Add `%MAVEN_HOME%\bin`

5. Verify installation:
```powershell
mvn -version
```

### Option B: Use Scoop (If installed)
```powershell
scoop install maven
mvn -version
```

### Option C: Use Chocolatey
```powershell
choco install maven
mvn -version
```

## Step 2: Configure MySQL Database

1. Install MySQL Server (if not already installed)
2. Create database:
```sql
CREATE DATABASE bank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Run schema.sql:
   - Open MySQL Workbench or CLI
   - Run: `source database/schema.sql`

4. Update `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

## Step 3: Build & Run Backend

```powershell
cd backend
mvn clean install
mvn spring-boot:run
```

Server runs at: **http://localhost:8080**

## Step 4: Run Frontend

1. Open `frontend/index.html` in a browser, or
2. Use Live Server (VS Code extension)

## API Documentation

- **Login**: POST /api/auth/login
- **Register**: POST /api/auth/register
- **Accounts**: GET/POST /api/accounts
- **Transactions**: POST/GET /api/transactions/deposit/{id}

## Troubleshooting

**Maven command not found?**
- Restart PowerShell or command prompt
- Verify MAVEN_HOME path is correct

**Database connection error?**
- Check MySQL is running
- Verify credentials in application.properties
- Ensure bank_db database exists

**Port 8080 already in use?**
- Change in application.properties: `server.port=8081`

