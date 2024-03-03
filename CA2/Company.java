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
                    String name = "";

                    // I had to create a validation point where employee name is asked first and when filled
                    // the program continues with asking for the email. Otherwise they would jump off at the same time in the console like this:

                    // New employee name:
                    // New employee email:
                    
                    // Maybe because I am denting in a while loop, or that is how switch works. I have no idea

                    while (name.isEmpty()) {
                        name = scanner.nextLine(); 
                        if (name.isEmpty()) {
                            System.out.println("New employee name:");
                        }
                    }

                    // Now continues the program

                    System.out.println("New employee email:");
                    String email = scanner.next();
                    
                        
                        // New Employee object
                        Employee newEmp = new Employee(name, email);
                        
                        // for AddNewStaff method I needed to create a new 
                        // Company company = new Company(); instance
                        // which takes another part of memmory which then I didnt know how to link with the main one
                        // that is why I didnt use AddNewStaff method

                        // Adding the new employee directly to the staff
                        staff.add(newEmp);
                        // Also add empNum
                        staffSet.add(newEmp.GetEmpNum());
                        
                        System.out.println("New employee added successfully.");
                        break;
                    
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
