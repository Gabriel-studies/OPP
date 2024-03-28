import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Class for connecting to my database in MySQL

public class App {
    private static final String URL = "jdbc:mysql://localhost:3306/CA3";
    private static final String USERNAME = "newuser";
    private static final String PASSWORD = "newuser";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        return connection;
    }
}
