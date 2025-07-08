import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader("dbconfig.txt"))) {
            String ip = reader.readLine();              // Line 1: IP
            String port = reader.readLine();           // Line 2: Port
            String dbName = reader.readLine();         // Line 3: DB Name
            user = reader.readLine();                  // Line 4: Username
            password = reader.readLine();              // Line 5: Password

            url = String.format(
                    "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                    ip, port, dbName
            );

        } catch (IOException e) {
            System.out.println("Error reading DB config: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
