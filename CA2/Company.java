import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

// Class Company and components
public class Company {
    public String companyName;
    public ArrayList<Employee> staff;
    public HashSet<Integer> staffSet;

    // Default constructor
    public Company() {
        this.companyName = "Business Gnómes Ltd";
        this.staff = new ArrayList<>();
        this.staffSet = new HashSet<>();
    }

    // Overloaded constructor
    public Company(String companyName) {
        this.companyName = companyName;
        this.staff = new ArrayList<>();
        this.staffSet = new HashSet<>();
    }

    // Method to add new staff plus empNum
    public void AddNewStaff(Employee employee) {
        staff.add(employee);
        staffSet.add(employee.GetEmpNum());
    }

    // Method to remove staff
    public void RemoveStaff(int empNum) {
        Iterator<Employee> iterator = staff.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            if (employee.GetEmpNum() == empNum) {
                iterator.remove();
                // Also remove empNum
                staffSet.remove(empNum);
                System.out.println("Employee removed successfully.");
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    // Method to get the number of staff
    public int GetStaffNumber() {
        return staff.size();
    }

    // Method to list employees above a certain employee number
        // We pass the numer in main method
    public void ListEmployees(int empNum) {
        Iterator<Employee> iterator = staff.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            if (employee.GetEmpNum() > empNum) {
                System.out.println(employee.GetName());
            }
        }
    }

    // Menu system for manager
    public void ManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        // Validating credentials and creating menu in a while loop
        if (username.equals("Gnomeo") && password.equals("smurf")) {
            System.out.println("Login successful.");

            while (true) {
                System.out.println("1. View current staff");
                System.out.println("2. Add new staff");
                System.out.println("3. Remove staff");
                System.out.println("4. Exit");

                // Reading and implementing the choice
                int choice = scanner.nextInt();
                // Lets use switch instead of many dented ifs
                switch (choice) {
                    case 1:
                        System.out.println("Current staff:");
                        for (Employee i : staff) {
                            System.out.println(i.GetName());
                        }
                        break;
                    case 2:
                        System.out.println("Wait till programmed");
                    case 3:
                        // Removimg staff by empNum
                        System.out.println("Enter employee number to remove:");
                        int empNum = scanner.nextInt();
                        RemoveStaff(empNum);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }
}
