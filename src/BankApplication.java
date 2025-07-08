
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApplication {
    private Map<Integer, BankAccount> accounts = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new BankApplication().run();
    }

    public BankApplication() {
        loadAccountsFromDatabase();
    }

    private void run() {
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> credit();
                case 3 -> debit();
                case 4 -> showDetails();
                case 5 -> showTransactions();
                case 6 -> searchAccountByName();
                case 7 -> exportTransactions();
                case 8 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 8);
    }

    private void printMenu() {
        System.out.println("\n1. Create Account");
        System.out.println("2. Credit Amount");
        System.out.println("3. Debit Amount");
        System.out.println("4. Show Account Details");
        System.out.println("5. Show Transaction History");
        System.out.println("6. Search Account by Name");
        System.out.println("7. Export Transactions to CSV");
        System.out.println("8. Exit");
        System.out.print("Choose: ");
    }

    private void loadAccountsFromDatabase() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM Accounts";
            var stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("AccountId");
                String name = rs.getString("HolderName");
                String pass = rs.getString("Password");
                double bal = rs.getDouble("Balance");

                BankAccount account = new BankAccount(id, name, pass, bal);
                accounts.put(id, account);
            }
        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private boolean verifyAccountAccess(int accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }
        System.out.print("Enter account password: ");
        String password = scanner.nextLine();
        if (!account.verifyPassword(password)) {
            System.out.println("Incorrect password.");
            return false;
        }
        return true;
    }

    private void createAccount() {
        System.out.print("Enter holder name: ");
        String holderName = scanner.nextLine();
        System.out.print("Set account password: ");
        String password = scanner.nextLine();
        BankAccount account = new BankAccount(holderName, password);
        account.saveToDatabase();
        accounts.put(account.getAccountId(), account);

        System.out.printf("Account created. ID: %d\n", account.getAccountId());
    }

    private void credit() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        if (verifyAccountAccess(accountId)) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            accounts.get(accountId).credit(amount);
        }
    }

    private void debit() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        if (verifyAccountAccess(accountId)) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            accounts.get(accountId).debit(amount);
        }
    }

    private void showDetails() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        if (verifyAccountAccess(accountId)) {
            accounts.get(accountId).showBalance();
        }
    }

    private void showTransactions() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        if (verifyAccountAccess(accountId)) {
            accounts.get(accountId).showTransactions();
        }
    }

    private void searchAccountByName() {
        System.out.print("Enter holder name to search: ");
        String searchName = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (BankAccount account : accounts.values()) {
            if (account.getHolderName().toLowerCase().contains(searchName)) {
                account.showBalance();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No accounts found with that name.");
        }
    }

    private void exportTransactions() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        if (verifyAccountAccess(accountId)) {
            System.out.print("Enter filename to export (e.g. transactions.csv): ");
            String filename = scanner.nextLine();
            accounts.get(accountId).exportTransactionsToFile(filename);
        }
    }
}
