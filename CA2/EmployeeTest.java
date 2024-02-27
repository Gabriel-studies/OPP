public class EmployeeTest {
    public static void main(String[] args) {

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
    }
}