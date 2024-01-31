// Gabriel Villasmil "CA1": Github repo:

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
        System.out.println("1. Writting files automatically");
        System.out.println("2. Add Data to status.txt manually");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        // Standard Operation
        if (choice == 1) {
            ProcessStudentData();
            System.out.println("Successfully written!");
        //Add Data manually to status.txt
        } else if (choice == 2) {
            System.out.println("Wait till programmed");
            //AddDataManually();
        // Error    
        } else {
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
                    System.out.println("Invalid data.");
                    continue;
                }
                // Selecting first and second name out my list
                String FirstName = Names[0];
                String SecondName = Names[1];
                // Continue validating the .txt file
                String NumberOfClasses = reader.readLine();
                String StudentNumber = reader.readLine();

                System.out.println("Invalid data for student:");
                System.out.println("First Name: " + FirstName);
                System.out.println("Second Name: " + SecondName);
                System.out.println("Number of Classes: " + NumberOfClasses);
                System.out.println("Student Number: " + StudentNumber);
                System.out.println();
            }
            
        } catch (IOException e) {
            System.out.println("Error processing student data");
        }
    }

    // Validating data
    //public static boolean ValidStudentData

    // Writting data
    //public static void WriteToStatusFile

    // Assigning the workload value
    //public static String DetermineWorkload
}
