
#  Bank System Java Console App

A simple bank account management system written in Java, connected to a SQL Server database using JDBC.

## Features

-  Create new bank accounts with password protection
-  Deposit and withdraw money securely
-  View account balance and transaction history
-  Search accounts by holder name
-  Export transactions to CSV file
-  Uses in-memory map for quick access + database for persistence

---

##  Technologies Used

- Java (JDK 11+ or JDK 17/21/24)
- SQL Server
- JDBC (SQL Server JDBC Driver)
- IntelliJ IDEA
- Basic File I/O for exporting CSV

---

##  How to Run the Project

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/Gemii111/BankSystem.git
cd BankSystem
```

### 2️⃣ Import the Project in IntelliJ or any IDE

Make sure you have JDK and JDBC driver configured.

### 3️⃣ Add SQL Server JDBC Driver to Classpath

Use version compatible with your JDK (like `mssql-jdbc-12.10.1.jre11.jar`), and place it in a `lib/` folder or reference from your system.

### 4️⃣ Set Up the Database

Create the SQL Server database using the script below.

You can find this SQL in the included `database.sql` file.

### 5️⃣ Configure DB Connection in `DatabaseUtil.java`

Make sure to replace the IP address, username, and password with the correct values for your SQL Server instance.


---

## 🧪 Usage

Run `BankApplication.java` and follow the menu:

```
1. Create Account
2. Credit Amount
3. Debit Amount
4. Show Account Details
5. Show Transaction History
6. Search Account by Name
7. Export Transactions to CSV
8. Exit
```

---

## 🗃️ Project Structure

```
BankSystem/
├── BankApplication.java         # Main app logic
├── BankAccount.java             # Account class with methods
├── Transaction.java             # Transaction class
├── DatabaseUtil.java            # DB connection utility
├── database.sql                 # DB creation script
├── Main.java                    # Run App
├── README.md                    # Description file
└── dbconfig.txt                 # Read DB connection from text file
```

---

## ⚠️ Notes

- This project is for **educational purposes**.
- Passwords are stored as plain text (not secure).
- No input validation or encryption is implemented.
- Not production ready — purely for learning JDBC and Java-DB integration.

---

## 📬 Author

Built by [Khaled Gamal].
