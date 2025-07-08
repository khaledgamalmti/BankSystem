
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private int accountId;
    private String holderName;
    private double balance;
    private List<Transaction> transactions;
    private String password;

    public BankAccount(String holderName, String password) {

        this.holderName = holderName;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.password = password;
    }

    public BankAccount(int accountId, String holderName, String password, double balance) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.password = password;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public int getAccountId() { return accountId; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public boolean verifyPassword(String inputPassword) { return this.password.equals(inputPassword); }

    public void credit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            transactions.add(new Transaction("CREDIT", amount));
            saveTransactionToDatabase("CREDIT", amount);
            updateBalanceInDatabase();
            System.out.println("Amount credited.");
        }
    }

    public void debit(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            transactions.add(new Transaction("DEBIT", amount));
            saveTransactionToDatabase("DEBIT", amount);
            updateBalanceInDatabase();
            System.out.println("Amount debited.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void showBalance() {
        System.out.printf("ID: %d\nHolder: %s\nBalance: %.2f\n", accountId, holderName, balance);
    }

    public void showTransactions() {
        loadTransactionsFromDatabase();
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    public void exportTransactionsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Type,Amount,Timestamp");
            for (Transaction t : transactions) {
                writer.printf("%s,%.2f,%s\n", t.getType(), t.getAmount(), t.getTimestamp());
            }
            System.out.println("Transactions exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting transactions: " + e.getMessage());
        }
    }

    public void saveToDatabase() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO Accounts (HolderName, Password, Balance) VALUES (?, ?, ?)";
            var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, holderName);
            stmt.setString(2, password);
            stmt.setDouble(3, balance);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.accountId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Database error (save account): " + e.getMessage());
        }
    }


    public void saveTransactionToDatabase(String type, double amount) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO Transactions (AccountId, Type, Amount, Timestamp) VALUES (?, ?, ?, ?)";
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error (save transaction): " + e.getMessage());
        }
    }

    public void updateBalanceInDatabase() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "UPDATE Accounts SET Balance = ? WHERE AccountId = ?";
            var stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, balance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error (update balance): " + e.getMessage());
        }
    }

    public void loadTransactionsFromDatabase() {
        transactions.clear();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM Transactions WHERE AccountId = ?";
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                double amount = rs.getDouble("Amount");
                LocalDateTime timestamp = rs.getTimestamp("Timestamp").toLocalDateTime();
                transactions.add(new Transaction(type, amount, timestamp));
            }
        } catch (SQLException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
}
