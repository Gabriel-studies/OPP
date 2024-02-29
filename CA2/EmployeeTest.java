// Jorge Gabriel Villasmil Mesa CA2: https://github.com/Gabriel-studies/OPP/tree/main/CA2

public class EmployeeTest {
    public static void main(String[] args) {

    // 1ST PART

        // Creating test
        Employee emp1 = new Employee("Joe Bloggs", "jb@gmail.com");
        Employee emp2 = new Employee("Ann Banana", "ab@gmail.com");
        Employee emp3 = new Employee("Tom Thumb", "tt@gmail.com");

        // Loading projectGroup array 
        Employee[] projectGroup = {emp1, emp2, emp3};

        // Printing next Employee number to be asigned
        System.out.println("Next employee number: " + Employee.GetNextEmpNum());

        // Searching for employees with empNum > m by iterating through projectGroup
        int m = 0; // Example value of m
        System.out.println("Employees with empNum > " + m + ":");
        for (Employee i : projectGroup) {
            if (i.GetEmpNum() > m) {
                System.out.println(i.GetName());
            }   
        }
        
        // We can use this too
        // List employees with empNum > 2
        //System.out.println("Employees with empNum > 2:");
        //company.ListEmployees(2);
        
    // 2ND PART

        // Create a Company instance
        Company company = new Company();

        // Add employees to the company
        company.AddNewStaff(emp1);
        company.AddNewStaff(emp2);
        company.AddNewStaff(emp3);
        

        // Manager login and menu
        company.ManagerMenu();
        
    }
}
