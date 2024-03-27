import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;


// Main Class
public class main {

    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        // Connection to my database
        Connection connection = App.getConnection();

        // Authenticate the user
        String role = authenticateUser();
        
        // Running the choose
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

    // First menu
    private static String authenticateUser() {
        System.out.println("Welcome! Please login:");
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

    // Prompt this after the program gets the role
    private static String login(String role) {
    String username, password;
    System.out.print("Enter username: ");
    username = scanner.nextLine();
    System.out.print("Enter password: ");
    password = scanner.nextLine();
    
    try (Connection connection = App.getConnection();

        // Run the query
         PreparedStatement statement = connection.prepareStatement("SELECT role FROM Users WHERE username = ? AND password = ?")) {

        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        // If the query equals the role, continue the program
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
    
    

 // ---------------------------------------------------------------------- OfficeMenu -----------------------------------------------------------------//   


    private static void officeMenu(Connection connection) {
        while (true) {
            System.out.println("Office Menu");
            System.out.println("1. Generate Course Report");
            System.out.println("2. Generate Student Report");
            System.out.println("3. Generate Lecturer Report");
            System.out.println("4. Change Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    selectCourseReportFormat();
                    break;
                case 2:
                    selectStudentReportFormat();
                    break;
                case 3:
                    selectLecturerReportFormat();
                    break;
                case 4:
                    // inizializing currentUsername string
                    System.out.print("Enter your current username: ");
                    String currentUsername = scanner.nextLine();
                    changeOwnUsernameAndPassword(connection, currentUsername);
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // -------------------------------------------------------------------------- LecturerMenu ------------------------------------------------------------------ //

    private static void lecturerMenu(Connection connection) {
        while (true) {
            System.out.println("Lecuter Menu");
            System.out.println("1. Generate Lecturer Report");
            System.out.println("2. Change Password");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    selectLecturerReportFormat();
                    break;
                case 2:
                    // inizializing currentUsername string
                    System.out.print("Enter your current username: ");
                    String currentUsername = scanner.nextLine();
                    changeOwnUsernameAndPassword(connection, currentUsername);
                    break;
                case 3:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    // ------------------------------------------------------------------------- Course report ---------------------------------------------------------------------//


    private static void selectCourseReportFormat() {
        System.out.println("Select Course Report Format:");
        System.out.println("1. Text");
        System.out.println("2. CSV");
        System.out.println("3. Console");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                generateCourseReport("txt");
                break;
            case 2:
                generateCourseReport("csv");
                break;
            case 3:
                generateCourseReport("console");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                // Bring it back
                selectCourseReportFormat();
                break;
        }
    }


    private static void generateCourseReport(String format) {
    Connection connection = null;
    try {
        connection = App.getConnection();
        switch (format.toLowerCase()) {
            case "txt":
                generateCourseReportAsText(connection);
                break;
            case "csv":
                generateCourseReportAsCSV(connection);
                break;
            case "console":
                DatabaseManager.generateCourseReport(connection); // Direct console output
                break;
            default:
                System.out.println("Invalid report format choice.");
                break;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // REPORT GETS GENERATED BUT PROGRAM FREEZES AND I DONT NOT WHY
    // Course report as txt
    private static void generateCourseReportAsText(Connection connection) throws SQLException {
        try (PrintWriter writer = new PrintWriter("course_report.txt")) {

            // Java class that allows me hold the data otherwise would be printed in the console
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Saving it
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps); 

            // Generate the report
            DatabaseManager.generateCourseReport(connection); 

            // Writting the variable
            writer.println(baos.toString());

            System.setOut(System.out); 
            System.out.println("Course Report generated as 'course_report.txt'");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to generate Course Report as a CSV file
    private static void generateCourseReportAsCSV(Connection connection) {
        // Calling the writer
        try (PrintWriter writer = new PrintWriter("course_report.csv")) {

            // Pass the resulset
            ResultSet resultSet = DatabaseManager.getCourseReportResultSet(connection);

            // Getting metadata so we getting the counts, so we pass a loop to write
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Writting the headers by counting the columns
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i));
                if (i < columnCount) {
                    // Separating by every comma
                    writer.print(",");
                } else {
                    writer.println();
                }
            }
            // Rows
            while (resultSet.next()) {

                // Columns checked
                for (int i = 1; i <= columnCount; i++) {
                    writer.print(resultSet.getString(i));
                    // Get the data
                    if (i < columnCount) {
                        writer.print(",");
                    } else {
                        writer.println();
                    }
                }
            }
            System.out.println("Course Report generated as 'course_report.csv'");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Calling the methods in databasemanager
    private static void generateCourseReport() {

        // Calling the method in Databasemanager to generate the report
        try {
            Connection connection = App.getConnection();
            DatabaseManager.generateCourseReport(connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // ---------------------------------------------------------------------------- Student Report --------------------------------------------------------------------- // 


    private static void selectStudentReportFormat() {
        System.out.println("Select Student Report Format:");
        System.out.println("1. Text");
        System.out.println("2. CSV");
        System.out.println("3. Console");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                generateStudentReport("txt");
                break;
            case 2:
                generateStudentReport("csv");
                break;
            case 3:
                generateStudentReport("console");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                selectStudentReportFormat(); 
                break;
        }
    }

    // REPORT GETS GENERATED BUT PROGRAM FREEZES AND I DONT NOT WHY
    // Method to generate Course Report as a text file
    private static void generateStudentReportAsText(Connection connection) throws SQLException {
        try (PrintWriter writer = new PrintWriter("student_report.txt")) {

            // Java class that allows me hold the data otherwise would be printed in the console
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Saving it
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps); 

            // Generate the report
            DatabaseManager.generateStudentReport(connection); 

            // Writting the variable
            writer.println(baos.toString());

            System.setOut(System.out); 
            System.out.println("Course Report generated as 'student_report.txt'");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to generate Course Report as a CSV file
    private static void generateStudentReportAsCSV(Connection connection) {
        try (PrintWriter writer = new PrintWriter("student_report.csv")) {
            ResultSet resultSet = DatabaseManager.getCourseReportResultSet(connection);

            // Getting metadata so we getting the counts, so we pass a loop to write
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Headers
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.print(",");
                } else {
                    writer.println();
                }
            }

            // Rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.print(resultSet.getString(i));
                    if (i < columnCount) {
                        writer.print(",");
                    } else {
                        writer.println();
                    }
                }
            }
            System.out.println("Student Report generated as 'Student_report.csv'");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void generateStudentReport(String format) {
        Connection connection = null;
        try {
            connection = App.getConnection();
            switch (format.toLowerCase()) {
                case "txt":
                    generateStudentReportAsText(connection);
                    break;
                case "csv":
                    generateStudentReportAsCSV(connection);
                    break;
                case "console":
                    DatabaseManager.generateStudentReport(connection);
                    break;
                default:
                    System.out.println("Invalid report format choice.");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateStudentReport() {

        // Calling the method in Databasemanager to generate the report
        try {
            Connection connection = App.getConnection();
            DatabaseManager.generateStudentReport(connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------- Lecturer Report --------------------------------------------------------------------------//


    private static void selectLecturerReportFormat() {
        System.out.println("Select Lecturer Report Format:");
        System.out.println("1. Text");
        System.out.println("2. CSV");
        System.out.println("3. Console");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                generateLecturerReport("txt");
                break;
            case 2:
                generateLecturerReport("csv");
                break;
            case 3:
                generateLecturerReport("console");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                selectLecturerReportFormat();
                break;
        }
    }

    // REPORT GETS GENERATED BUT PROGRAM FREEZES AND I DONT NOT WHY
    // Method to generate Course Report as a text file
    private static void generateLecturerReportAsText(Connection connection) throws SQLException {
        try (PrintWriter writer = new PrintWriter("lecturer_report.txt")) {
            // Java class that allows me hold the data otherwise would be printed in the console
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Saving it
            PrintStream ps = new PrintStream(baos);
            System.setOut(ps); 

            // Generate the report
            DatabaseManager.generateLecturerReport(connection); 

            // Writting the variable
            writer.println(baos.toString());

            System.setOut(System.out); 
            System.out.println("Lecturer Report generated as 'lecturer_report.txt'");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    // Method to generate Lecturer Report as a CSV file
    private static void generateLecturerReportAsCSV(Connection connection) {
        try (PrintWriter writer = new PrintWriter("lecturer_report.csv")) {

            // Calling the Resulset from Databasemanager
            ResultSet resultSet = DatabaseManager.getLecturerReportResultSet(connection); 

            // Getting metadata so we getting the counts, so we pass a loop to write
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Headers
            for (int i = 1; i <= columnCount; i++) {
                writer.print(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.print(",");
                } else {
                    writer.println();
                }
            }
            
            // Rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.print(resultSet.getString(i));
                    if (i < columnCount) {
                        writer.print(",");
                    } else {
                        writer.println();
                    }
                }
            }
            System.out.println("Lecturer Report generated as 'lecturer_report.csv'");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void generateLecturerReport(String format) {
        Connection connection = null;
        try {
            connection = App.getConnection();
            switch (format.toLowerCase()) {
                case "txt":
                    generateLecturerReportAsText(connection);
                    break;
                case "csv":
                    generateLecturerReportAsCSV(connection);
                    break;
                case "console":
                    DatabaseManager.generateLecturerReport(connection); 
                    break;
                default:
                    System.out.println("Invalid report format choice.");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void generateLecturerReport() {

        // Calling the method in Databasemanager to generate the report
        try {
            Connection connection = App.getConnection();
            DatabaseManager.generateLecturerReport(connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

