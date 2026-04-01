# ⚡ QUICK START - Bank Management System

Get the full-stack banking app running in **5 minutes**!

## 🎯 Prerequisites Checklist

```powershell
# Check Java
java -version
# Expected: Java 17 or higher ✓

# Check MySQL
mysql --version
# Expected: MySQL 8.0+ ✓

# Check Maven
mvn -version
# If NOT found → See "Install Maven" below
```

---

## 🚀 Quick Setup (5 Minutes)

### 1️⃣ Database Setup (2 minutes)
```sql
-- Open MySQL and run:
CREATE DATABASE bank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bank_db;
-- Paste contents of database/schema.sql and execute
```

### 2️⃣ Update Credentials (1 minute)
File: `backend/src/main/resources/application.properties`
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 3️⃣ Run Backend (1 minute)
```powershell
cd backend
mvn spring-boot:run
```
✅ See message: *"Bank Management System is running!"*

### 4️⃣ Run Frontend (1 minute)
- Right-click `frontend/index.html` → Open with Live Server
- OR: Double-click to open in browser

### ✅ Done! 
- Frontend: http://localhost:5500 or file://...
- Backend: http://localhost:8080
- API: http://localhost:8080/api

---

## 🎪 First Time Use

1. **Register**
   - Click "Create one free"
   - Enter Name, Email, Password → Submit

2. **Create Account**
   - Dashboard → "New Account" → Choose type → Create

3. **Make Deposit**
   - Click "Deposit" → Select account → Enter amount → Done!

4. **Make Withdrawal**
   - Click "Withdraw" → Select account → Enter amount → Done!

5. **Transfer Money**
   - Need 2 accounts? Create another!
   - Click "Transfer" → Enter target account → Enter amount → Done!

---

## 📦 Install Maven (If Needed)

### Windows
1. Download: https://maven.apache.org/download.cgi → **apache-maven-3.9.5-bin.zip**
2. Extract to: `C:\Program Files\Apache\maven`
3. Add Environment Variable:
   - Settings → System → Environment Variables
   - New Variable: `MAVEN_HOME` = `C:\Program Files\Apache\maven\`
   - Edit PATH: Add `%MAVEN_HOME%\bin`
4. Restart PowerShell
5. Verify: `mvn -version`

### Mac
```bash
brew install maven
mvn -version
```

### Linux
```bash
sudo apt-get install maven
mvn -version
```

---

## 🆘 Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| mysql command not found | Install MySQL from https://dev.mysql.com/downloads |
| mvn command not found | Restart PowerShell after Maven install, check PATH |
| Cannot connect to database | Verify MySQL is running, check password in `application.properties` |
| Port 8080 in use | Change port: `server.port=8081` in `application.properties` |
| CORS error | Use Live Server instead of opening file directly |
| Build fails | Run: `mvn clean install -U` |

---

## 📂 Important Files

| File | Purpose |
|------|---------|
| `backend/src/main/resources/application.properties` | Database & server config |
| `frontend/index.html` | Login page |
| `database/schema.sql` | Create database tables |
| `README.md` | Full documentation |

---

## 🔗 Useful Links

- Java Installation: https://adoptium.net
- Maven: https://maven.apache.org
- MySQL: https://dev.mysql.com
- Spring Boot: https://spring.io/projects/spring-boot
- JWT: https://jwt.io

---

## 💡 Tips

- **First build is slow** (1-2 min) — Maven downloads dependencies
- **Keep MySQL running** — backend won't work without it
- **Frontend reloads automatically** with Live Server
- **Clear localStorage** (F12 → Application) if login issues
- **Check F12 Console** for JavaScript errors

---

## 🎉 Success!

If you see:
- ✅ Backend: *"Bank Management System is running!"*
- ✅ Frontend: Login page loads
- ✅ Can register & login
- ✅ Can perform banking operations

**You're all set!** 🚀

---

Need help? Check [README.md](README.md) for detailed documentation.
