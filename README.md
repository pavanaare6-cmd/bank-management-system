# 🏦 NexBank — Bank Management System

A full-stack bank management system built with **Spring Boot** (backend) and **HTML/CSS/JS** (frontend), using **MySQL** as the database.

---

## 📁 Project Structure

```
bank-management-system/
├── backend/                          # Spring Boot application
│   ├── pom.xml                       # Maven dependencies
│   └── src/main/
│       ├── java/com/bank/
│       │   ├── BankManagementApplication.java   # Main entry point
│       │   ├── controller/
│       │   │   ├── AuthController.java          # /api/auth/register, /login
│       │   │   ├── AccountController.java       # /api/accounts
│       │   │   └── TransactionController.java   # /api/transactions
│       │   ├── service/
│       │   │   ├── AuthService.java
│       │   │   ├── AccountService.java
│       │   │   └── TransactionService.java
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── AccountRepository.java
│       │   │   └── TransactionRepository.java
│       │   ├── model/
│       │   │   ├── User.java
│       │   │   ├── Account.java
│       │   │   └── Transaction.java
│       │   ├── dto/                             # Request/Response objects
│       │   ├── security/                        # JWT + Spring Security
│       │   ├── config/                          # SecurityConfig
│       │   └── exception/                       # Global error handling
│       └── resources/
│           └── application.properties
│
├── frontend/                         # Static HTML/CSS/JS
│   ├── index.html                    # Login page
│   ├── css/
│   │   └── style.css                 # All styles
│   ├── js/
│   │   └── utils.js                  # Shared JS utilities
│   └── pages/
│       ├── register.html
│       ├── dashboard.html
│       ├── deposit.html
│       ├── withdraw.html
│       ├── transfer.html
│       └── balance.html
│
└── database/
    └── schema.sql                    # MySQL setup script
```

---

## ✅ Prerequisites

Before running this project, make sure you have installed:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17 or higher | https://adoptium.net |
| Maven | 3.8+ (or use `mvnw`) | https://maven.apache.org |
| MySQL | 8.0+ | https://dev.mysql.com/downloads |
| VS Code | Latest | https://code.visualstudio.com |

**Recommended VS Code Extensions:**
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack
- Live Server (Ritwick Dey)

---

## 🚀 Step-by-Step Setup

### Step 1: Set Up MySQL Database

1. Open **MySQL Workbench** or your terminal and connect to MySQL:
   ```bash
   mysql -u root -p
   ```

2. Run the schema file to create the database and tables:
   ```sql
   source /path/to/bank-management-system/database/schema.sql
   ```
   Or paste the contents of `schema.sql` directly into MySQL Workbench and execute.

3. Verify the database was created:
   ```sql
   USE bank_db;
   SHOW TABLES;
   -- Should show: users, accounts, transactions
   ```

---

### Step 2: Configure the Backend

1. Open `backend/src/main/resources/application.properties`

2. Update your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bank_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
   ```

3. (Optional) Change the JWT secret key for better security:
   ```properties
   app.jwt.secret=YourVeryLongAndSecureRandomSecretKeyHere
   ```

---

### Step 3: Run the Backend

**Option A — Using VS Code:**
1. Open the `backend/` folder in VS Code
2. Open `BankManagementApplication.java`
3. Click the **▶ Run** button above the `main` method
4. Or press `F5` to run with the Java debugger

**Option B — Using Terminal (Maven):**
```bash
cd bank-management-system/backend
./mvnw spring-boot:run
```

**Option C — Using Terminal (Windows):**
```cmd
cd bank-management-system\backend
mvnw.cmd spring-boot:run
```

✅ You should see this in the console:
```
====================================
 Bank Management System is running!
 API Base URL: http://localhost:8080/api
====================================
```

> **First run takes ~2 minutes** as Maven downloads all dependencies.

---

### Step 4: Run the Frontend

**Option A — VS Code Live Server (Recommended):**
1. Install the **Live Server** extension in VS Code
2. Open the `frontend/` folder in VS Code
3. Right-click on `index.html` → **"Open with Live Server"**
4. Your browser opens at `http://127.0.0.1:5500`

**Option B — Just open in browser:**
1. Navigate to `bank-management-system/frontend/`
2. Double-click `index.html` to open it directly in your browser

> Note: The frontend connects to the backend at `http://localhost:8080`. Make sure the backend is running first.

---

## 🧪 How to Test

### 1. Register a New User
- Open the frontend in your browser
- Click **"Create one free"** to go to the Register page
- Fill in: Name, Email, Password → Submit
- You'll be automatically redirected to the Dashboard

### 2. Create a Bank Account
- On the Dashboard, click **"New Account"**
- Choose **SAVINGS** or **CURRENT** → Click Create
- Your new account appears on the Dashboard

### 3. Make a Deposit
- Click **Deposit** in the navbar
- Select your account → Enter amount → Click Deposit
- Your balance updates immediately

### 4. Make a Withdrawal
- Click **Withdraw** → Select account → Enter amount → Submit
- If balance is insufficient, you'll see an error

### 5. Transfer Money
- You need **two accounts** (create another or use a friend's account number)
- Click **Transfer** → Select your account → Enter recipient's account number → Amount → Submit

### 6. View Statement
- Click **Balance** → Click on any account card
- See full transaction history with filters

---

## 📡 API Reference

All protected endpoints require the header:
```
Authorization: Bearer <your-jwt-token>
```

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get token |

### Accounts (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/accounts` | Create bank account |
| GET | `/api/accounts` | Get all your accounts |
| GET | `/api/accounts/{id}` | Get specific account |

### Transactions (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions/deposit/{accountId}` | Deposit money |
| POST | `/api/transactions/withdraw/{accountId}` | Withdraw money |
| POST | `/api/transactions/transfer/{fromAccountId}` | Transfer money |
| GET | `/api/transactions/history/{accountId}` | Transaction history |

### Example: Login Request
```json
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "password123"
}
```

### Example: Deposit Request
```json
POST http://localhost:8080/api/transactions/deposit/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "amount": 5000.00,
  "description": "Salary"
}
```

---

## 🔧 Troubleshooting

### ❌ "Cannot connect to server"
- Make sure the Spring Boot backend is running on port 8080
- Check the terminal for any startup errors
- Verify MySQL is running

### ❌ "Access denied for user 'root'@'localhost'"
- Check `application.properties` — the password must match your MySQL password
- Try connecting to MySQL manually: `mysql -u root -p`

### ❌ "Communications link failure" (MySQL)
- MySQL is not running — start it:
  - Windows: Open Services → Start MySQL80
  - Mac: `brew services start mysql`
  - Linux: `sudo systemctl start mysql`

### ❌ Port 8080 already in use
- Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```
- Also update the `API_BASE` in `frontend/js/utils.js`:
  ```javascript
  const API_BASE = 'http://localhost:8081/api';
  ```

### ❌ CORS error in browser console
- This is normal if you open HTML files directly (file://)
- Use VS Code Live Server or any local HTTP server instead

### ❌ Maven build fails
- Make sure Java 17 is installed: `java -version`
- Make sure JAVA_HOME is set correctly
- Try: `./mvnw clean install -U` to force re-download dependencies

---

## 🛡️ Security Features

- **BCrypt password hashing** — passwords are never stored in plain text
- **JWT authentication** — stateless, token-based auth (24hr expiry)
- **Account ownership verification** — users can only access their own accounts
- **Input validation** — all inputs validated on both client and server
- **Global exception handling** — clean error responses, no stack traces exposed

---

## 📝 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2 |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8.0 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Password | BCrypt |
| Build Tool | Maven |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Fonts | Google Fonts (DM Sans, DM Serif Display) |

---

## 🎓 Learning Notes

This project follows the standard **Layered Architecture** pattern:

```
HTTP Request
    ↓
Controller      (receives request, validates input, calls service)
    ↓
Service         (business logic: checks balance, applies rules)
    ↓
Repository      (database queries via JPA)
    ↓
MySQL Database
```

Key concepts used:
- `@RestController` — marks a class as a REST API controller
- `@Service` — marks a class as a business logic service
- `@Repository` — marks an interface as a data access layer
- `@Entity` — marks a class as a database table (JPA)
- `@Transactional` — wraps multiple DB operations in one atomic unit
- `@Valid` — triggers input validation on request bodies
- `OncePerRequestFilter` — runs custom code for every HTTP request (JWT check)

---

*Built with ❤️ — NexBank v1.0.0*
