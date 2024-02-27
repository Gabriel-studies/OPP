public class Employee {
    private String name;
    private String email;
    private int empNum;
    private static int nextEmpNum = 1;

    // 1st and 2nd Constructors
    public Employee() {
        this.name = "Unknown";
        this.email = "unknown@example.com";
        this.empNum = nextEmpNum++;
    }

    public Employee(String name, String email) {
        this.name = name;
        this.email = email;
        this.empNum = nextEmpNum++;
    }

    // Accessor methods
    public String GetName() {
        return name;
    }

    public String GetEmail() {
        return email;
    }

    public int GetEmpNum() {
        return empNum;
    }

    // Checking email lenght
    public void SetEmail(String email) {
        if (email.length() > 3) {
            this.email = email;
        } else {
            System.out.println("Email must be longer than 3 letters.");
        }
    }

    // Method for getting the nextEmpNum
    public static int GetNextEmpNum() {
        return nextEmpNum;
    }
}
