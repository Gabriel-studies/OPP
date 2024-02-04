// Gabriel Villasmil "CA1": Github repo: https://github.com/Gabriel-studies/OPP/tree/main/CA1

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CA1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display menu: choosing operations
        System.out.println("Menu:");
        System.out.println("1. Write files automatically from students.txt");
        System.out.println("2. Add data manually to status.txt");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        // Standard Operation
        if (choice == 1) {
            ProcessStudentData();
            System.out.println("Valid data successfully written!");
        // Add Data manually to status.txt
        }
        else if (choice == 2) {
            AddDataManually();
        // Error    
        }
        else {
            System.out.println("Invalid choice. Exiting program.");
        }

        scanner.close();
    }

    // METHODS

    // Reading data and writting data
    public static void ProcessStudentData() {
        //Reading students.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
            // Writting in status.txt
             FileWriter writer = new FileWriter("status.txt")) {

            // Starting to read the file in a while loop making sure there is no null values
            // Creating a String so I can call it to split the first line right afterwards

            String line;
            while ((line = reader.readLine()) != null) {
                //Split the first line characters to get them whenever I want, saving them into an empty list
                String[] Names = line.split(" ");
                // Validating the split (Must be first and second name)
                if (Names.length < 2) {
                    System.out.println("Invalid data: first and second name must be in the same line separated by space");
                    continue;
                }
                // Selecting first and second name out my list
                String FirstName = Names[0];
                String SecondName = Names[1];

                // Continue validating the .txt file
                String NumberOfClasses = reader.readLine();
                
                // Parsing the class workload String to get the int
                int NumberOfClassesParsed = Integer.parseInt(NumberOfClasses);

                // Call DetermineWorkload method to determine workload and store the result in a variable
                String workloadResult = DetermineWorkload(NumberOfClassesParsed);

                String StudentNumber = reader.readLine();

                // Calling the function for validation and if right, writting everything to status.txt
                    
                if (ValidStudentData(FirstName, SecondName, NumberOfClassesParsed, StudentNumber)) {
                    //if right...
                        // Remeber we validate NumberOfClassesParsed but we write workloadResult
                    WriteToStatusFile(writer, FirstName, SecondName, workloadResult, StudentNumber);
                } else {
                    // This helps us to see which entry was thrown an error
                    System.out.println("Invalid data for student:");
                    System.out.println("First Name: " + FirstName);
                    System.out.println("Second Name: " + SecondName);
                    System.out.println(workloadResult);
                    System.out.println("Student Number: " + StudentNumber);
                }
            }
        } catch (IOException e) {
            System.out.println("Error processing student data; check data in students.txt");
        }
    }

    // Validating data
    public static boolean ValidStudentData(String FirstName, String SecondName, int NumberOfClassesParsed, String StudentNumber) {
        // Boolean state is valid till one of the next conditionals are not
        boolean isValid = true;
    
        // First name must be letters
        if (!FirstName.matches("[a-zA-Z]+")) {
            System.err.println("Please give a valid first name");
            // Change boolean state
            isValid = false;
        }
        // Second name must be letters
        if (!SecondName.matches("[a-zA-Z]+")) {
            System.err.println("Please give a valid second name");
            isValid = false;
        }
        // Must be above 1 and below 8
        if (NumberOfClassesParsed < 1 || NumberOfClassesParsed >= 8) {
            System.err.println("Please enter a number of classes above 1 and below 8");
            isValid = false;
        }  
        // Validating student number 
        if (!StudentNumber.matches("2\\d{1}[A-Z]{3}\\d{4}")) { // Two numbers (MUST start with 2) + Three letters + Four numbers
            System.err.println("Please give a valid student number with the format: 20AAA0000");
            isValid = false;
        }  
        // Return state of the boolean
        return isValid;
    }
    

    // Writting data
    public static void WriteToStatusFile(FileWriter writer, String FirstName, String SecondName, String workloadResult, String StudentNumber) 

    // Error if this is not working out
    throws IOException {
        // Writting data to status.txt in different lines
        writer.write(StudentNumber + " - " + SecondName + "\n");
        writer.write(workloadResult + "\n\n"); // Leaving extra line for next data entry
    }


    // Assigning the workload value
    // Passing the already parsed "numberOfClasses" variable 
    public static String DetermineWorkload(int NumberOfClassesParsed) {
        // Determining workload based on the number of classes
        if (NumberOfClassesParsed == 1) {
            return "Very Light";
        } else if (NumberOfClassesParsed == 2) {
            return "Light";
        // Between 3 and 5 included: Part Time
        } else if (NumberOfClassesParsed >= 3 && NumberOfClassesParsed <= 5) {
            return "Part Time";
        } else {
            // Above 5
            return "Full Time";
        }    
    }

    // Adding data manually method
    public static void AddDataManually() {
        // Openning new scanner in a try catch
        try (Scanner scanner = new Scanner(System.in);

            // Remember appending it to avoid overwritting
            FileWriter writer = new FileWriter("status.txt", true)) {

            System.out.println("Enter First Name and Last Name separated by space:");
            String fullName = scanner.nextLine();
            
            // Creating an empty list to get both first and second name whenever we need
            String[] names = fullName.split(" ");
            if (names.length < 2) {
                System.out.println("Invalid format. Please enter both first and last name separated by space in the same line.");
                return;
            }
            // Picking each name out of our list
            String FirstName = names[0];
            String SecondName = names[1];

            // Continue adding the rest of the data
            System.out.println("Enter Number of Classes:");
            String NumberOfClasses = scanner.nextLine();
            
            // Parsing the class workload String to get the int
            int NumberOfClassesParsed = Integer.parseInt(NumberOfClasses);

            // Call DetermineWorkload method to determine workload and store the result in a variable
            String workloadResult = DetermineWorkload(NumberOfClassesParsed);

            System.out.println("Enter Student Number:");
            String StudentNumber = scanner.nextLine();

            // Validating data by calling these two methods
                // Remeber we validate NumberOfClassesParsed but we write workloadResult
            if (ValidStudentData(FirstName, SecondName, NumberOfClassesParsed, StudentNumber)) {
                WriteToStatusFile(writer, FirstName, SecondName, workloadResult, StudentNumber);
                System.out.println("Data added successfully to status.txt");
            } else {
                System.out.println("Something went wrong, check manual added data");
            }
        } catch (IOException e) {
            System.out.println("Error adding manual data to status.txt");
        }    
    }
}
