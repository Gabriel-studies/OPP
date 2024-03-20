import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class main {

    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        // Connection to the database
        Connection connection = App.getConnection();

        String role = authenticateUser();
        
        // Menu on users role
        switch (role) {
            case "admin":
                adminMenu(connection);
                break;
            case "office":
                officeMenu(connection);
                break;
            case "lecturer":
                lecturerMenu(connection);
                break;
            default:
                System.out.println("Invalid role. Exiting program.");
                break;
            }
    }
    private static String authenticateUser() {
        System.out.println("Welcome! Please select your role:");
        System.out.println("1. Admin");
        System.out.println("2. Office");
        System.out.println("3. Lecturer");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                return login("admin");
            case 2:
                return login("office");
            case 3:
                return login("lecturer");
            default:
                return null;
        }
    }

    private static String login(String role) {
    
    String username, password;
    System.out.print("Enter username: ");
    username = scanner.nextLine();
    System.out.print("Enter password: ");
    password = scanner.nextLine();
    
    try (Connection connection = App.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT role FROM Users WHERE username = ? AND password = ?")) {
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String userRole = resultSet.getString("role");
            if (userRole.equals(role)) {
                return userRole;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Invalid credentials for " + role + ". Exiting program.");
    return null;
}

private static void adminMenu(Connection connection) {
   System.out.println("Wait till programed");
        }

    

    private static void officeMenu(Connection connection) {
        System.out.println("Wait till programed");
            }
       

    private static void lecturerMenu(Connection connection) {
        System.out.println("Wait till programed");
            
    }

} 