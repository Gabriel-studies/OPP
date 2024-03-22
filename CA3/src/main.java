import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

// ------------------------------------------------------------  ADMIN MENU ---------------------------------------------------------------------------------//


    private static void adminMenu(Connection connection) {
        while (true) {
            System.out.println("Admin Menu");
            System.out.println("1. Add User");
            System.out.println("2. Modify User");
            System.out.println("3. Delete User");
            System.out.println("4. Change Own Password");

            // I add this because when changing or addig users, the console would not show them, but them admin
            System.out.println("5. View real Users in database");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    modifyUser(connection);
                    break;
                case 3:
                    deleteUser(connection);
                    break;
                case 4:
                    // Inizializing currentUsername string that we need later
                    System.out.print("Enter your current username: ");
                    String currentUsername = scanner.nextLine();
                    changeOwnUsernameAndPassword(connection, currentUsername);
                    break;
                case 5:
                    viewUsers(connection);
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ViewUsers
    private static void viewUsers(Connection connection) {
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users")) {
            System.out.println("Users in the database:");

            //Printing the result
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                System.out.println("Username: " + username + ", Role: " + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // AddUser
    private static void addUser() {

        // We need these three
        String username, password, role;

        System.out.print("Enter new username: ");
        username = scanner.nextLine();
        System.out.print("Enter new password: ");
        password = scanner.nextLine();
        System.out.print("Enter role for the new user: ");
        role = scanner.nextLine();
        
        try (Connection connection = App.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (username, password, role) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
            
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); 
            }
            
    }
    

    // ModifyUser
    public static void modifyUser(Connection connection) {
        System.out.print("Enter the username of the user you want to modify: ");
        String username = scanner.nextLine();

        // Check if the user exists
        if (!userExists(connection, username)) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Enter the new details:");

        System.out.print("New username: ");
        String newUsername = scanner.nextLine();

        System.out.print("New password: ");
        String newPassword = scanner.nextLine();

        System.out.print("New role: ");
        String newRole = scanner.nextLine();

        // Update the user calling our method
        updateUser(connection, username, newUsername, newPassword, newRole);

        System.out.println("User updated successfully.");
    }


        private static boolean userExists(Connection connection, String username) {
            String sql = "SELECT COUNT(*) AS count FROM Users WHERE username = ?";
        
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    // Checking if we have that username
                    return count > 0;
                }
            } catch (SQLException e) {
                System.out.println("Error checking user existence: " + e.getMessage());
            }
            return false;
        }
        

        private static void updateUser(Connection connection, String username, String newUsername, String newPassword, String newRole) {
            String sql = "UPDATE Users SET username = ?, password = ?, role = ? WHERE username = ?";
            
            // We pass the query and assing everything to our variables
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newUsername);
                statement.setString(2, newPassword);
                statement.setString(3, newRole);
                statement.setString(4, username);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("User updated successfully.");
                } else {
                    System.out.println("Failed to update user.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        }
    


    // DeleteUser
    private static void deleteUser(Connection connection) {
        try {
            System.out.print("Enter the username of the user to delete: ");
            String username = scanner.nextLine();

            // Get the query
            String sql = "DELETE FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Setting the username parameter
            statement.setString(1, username);

            // Running it
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
    
    // changeOwnUsernameAndPassword
    private static void changeOwnUsernameAndPassword(Connection connection, String currentUsername) {
        try {
            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
    
            // SQL statement
            String sql = "UPDATE users SET username = ?, password = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
    
            statement.setString(1, newUsername);
            statement.setString(2, newPassword);
            statement.setString(3, currentUsername);
    
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Username and password updated successfully.");
            } else {
                System.out.println("Failed to update username and password. User not found or values unchanged.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing username and password: " + e.getMessage());
        }
    }
    
    

    private static void officeMenu(Connection connection) {
        System.out.println("Wait till programed");
            }
       

    private static void lecturerMenu(Connection connection) {
        System.out.println("Wait till programed");
            
    }

} 
