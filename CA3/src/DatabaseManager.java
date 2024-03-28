import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


// Class for making all SQL queries
    // Every report has its own Resulset so it can be saved as a csv file

public class DatabaseManager {
    
    // Course Report method
    public static void generateCourseReport(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "C.course_name AS Module_Name, " +
                       "S.programme AS Programme, " +
                       "COUNT(DISTINCT E.student_id) AS Students_Enrolled, " +
                       "L.lecturer_name AS Lecturer, " +
                       "CASE " +
                       "    WHEN C.online = 1 THEN 'Online' " +
                       "    ELSE C.room " +
                       "END AS Room_Assignment " +
                       "FROM " +
                       "Courses C " +
                       "JOIN " +
                       "Enrollments E ON C.course_id = E.course_id " +
                       "JOIN " +
                       "Students S ON E.student_id = S.student_id " +
                       "JOIN " +
                       "Lecturers L ON C.lecturer_id = L.lecturer_id " +
                       "GROUP BY " +
                       "C.course_id, Programme, Lecturer, Room_Assignment";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Print it in the console
            while (resultSet.next()) {
                System.out.println("Module Name: " + resultSet.getString("Module_Name"));
                System.out.println("Programme: " + resultSet.getString("Programme"));
                System.out.println("Students Enrolled: " + resultSet.getInt("Students_Enrolled"));
                System.out.println("Lecturer: " + resultSet.getString("Lecturer"));
                System.out.println("Room Assignment: " + resultSet.getString("Room_Assignment"));
                System.out.println();
            }
        }
    }

    // Course Report ResultSet
    public static ResultSet getCourseReportResultSet(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "c.course_name AS Module_Name, " +
                       "c.programme AS Programme, " +
                       "COUNT(e.student_id) AS Number_of_Students, " +
                       "l.lecturer_name AS Lecturer, " +
                       "COALESCE(c.room, 'Online') AS Room " +
                       "FROM " +
                       "Courses c " +
                       "JOIN " +
                       "Lecturers l ON c.lecturer_id = l.lecturer_id " +
                       "LEFT JOIN " +
                       "Enrollments e ON c.course_id = e.course_id " +
                       "GROUP BY " +
                       "c.course_id, Module_Name, Programme, Lecturer, Room";
        
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }



    // Student Report method
    public static void generateStudentReport(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "s.student_id AS Student_Number, " +
                       "s.student_name AS Student_Name, " +
                       "s.programme AS Programme, " +
                       "GROUP_CONCAT(DISTINCT c.course_name ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Enrolled, " +
                       "GROUP_CONCAT(DISTINCT CONCAT(c.course_name, ' - Grade: ', g.grade) ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Completed_With_Grades, " +
                       "GROUP_CONCAT(DISTINCT c2.course_name ORDER BY c2.course_name ASC SEPARATOR ', ') AS Modules_To_Repeat " +
                       "FROM " +
                       "Students s " +
                       "LEFT JOIN " +
                       "Enrollments e ON s.student_id = e.student_id " +
                       "LEFT JOIN " +
                       "Courses c ON e.course_id = c.course_id " +
                       "LEFT JOIN " +
                       "Grades g ON e.enrollment_id = g.enrollment_id " +
                       "LEFT JOIN " +
                       "Courses c2 ON s.programme = c2.programme " +
                       "AND c2.course_id NOT IN (SELECT e2.course_id FROM Enrollments e2 WHERE e2.student_id = s.student_id) " +
                       "GROUP BY " +
                       "s.student_id, s.student_name, s.programme";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("Student Number: " + resultSet.getString("Student_Number"));
                System.out.println("Student Name: " + resultSet.getString("Student_Name"));
                System.out.println("Programme: " + resultSet.getString("Programme"));
                System.out.println("Modules Enrolled: " + resultSet.getString("Modules_Enrolled"));
                System.out.println("Modules Completed with Grades: " + resultSet.getString("Modules_Completed_With_Grades"));
                System.out.println("Modules to Repeat: " + resultSet.getString("Modules_To_Repeat"));
                System.out.println();
            }
        }
    }


    // Student Report ResultSet
    public static ResultSet getStudentReportResultSet(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "s.student_id AS Student_Number, " +
                       "s.student_name AS Student_Name, " +
                       "s.programme AS Programme, " +
                       "GROUP_CONCAT(DISTINCT c.course_name ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Enrolled, " +
                       "GROUP_CONCAT(DISTINCT CONCAT(c.course_name, ' - Grade: ', g.grade) ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Completed_With_Grades, " +
                       "GROUP_CONCAT(DISTINCT c2.course_name ORDER BY c2.course_name ASC SEPARATOR ', ') AS Modules_To_Repeat " +
                       "FROM " +
                       "Students s " +
                       "LEFT JOIN " +
                       "Enrollments e ON s.student_id = e.student_id " +
                       "LEFT JOIN " +
                       "Courses c ON e.course_id = c.course_id " +
                       "LEFT JOIN " +
                       "Grades g ON e.enrollment_id = g.enrollment_id " +
                       "LEFT JOIN " +
                       "Courses c2 ON s.programme = c2.programme " +
                       "AND c2.course_id NOT IN (SELECT e2.course_id FROM Enrollments e2 WHERE e2.student_id = s.student_id) " +
                       "GROUP BY " +
                       "s.student_id, s.student_name, s.programme";
        
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }



    // Lecturer Report method
    public static void generateLecturerReport(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "    l.lecturer_name AS Lecturer_Name, " +
                       "    l.role AS Role, " +
                       "    GROUP_CONCAT(DISTINCT c.course_name ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Taught, " +
                       "    COUNT(DISTINCT e.student_id) AS Number_of_Students " +
                       "FROM " +
                       "    Lecturers l " +
                       "JOIN " +
                       "    Courses c ON l.lecturer_id = c.lecturer_id " +
                       "LEFT JOIN " +
                       "    Enrollments e ON c.course_id = e.course_id " +
                       "GROUP BY " +
                       "    l.lecturer_id, l.lecturer_name, l.role";
    
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                System.out.println("Lecturer Name: " + resultSet.getString("Lecturer_Name"));
                System.out.println("Role: " + resultSet.getString("Role"));
                System.out.println("Modules Taught: " + resultSet.getString("Modules_Taught"));
                System.out.println("Number of Students: " + resultSet.getInt("Number_of_Students"));
                System.out.println();
            }
        }
    }
    

    // Lecturer Report ResultSet
    public static ResultSet getLecturerReportResultSet(Connection connection) throws SQLException {
        String query = "SELECT " +
                       "    l.lecturer_name AS Lecturer_Name, " +
                       "    l.role AS Role, " +
                       "    GROUP_CONCAT(DISTINCT c.course_name ORDER BY c.course_name ASC SEPARATOR ', ') AS Modules_Taught, " +
                       "    COUNT(DISTINCT e.student_id) AS Number_of_Students " +
                       "FROM " +
                       "    Lecturers l " +
                       "JOIN " +
                       "    Courses c ON l.lecturer_id = c.lecturer_id " +
                       "LEFT JOIN " +
                       "    Enrollments e ON c.course_id = e.course_id " +
                       "GROUP BY " +
                       "    l.lecturer_id, l.lecturer_name, l.role";
        
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

}
