import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Company Z Login System ===");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String role = Authorization.login(username, password);

        if (role == null) {
            System.out.println("Invalid login credentials. Exiting.");
            sc.close();
            return;
        }

        if (role.equalsIgnoreCase("hr_admin")) {
            System.out.println("\nWelcome HR Admin! You have full system access.");
            runHrAdminMenu(sc);
        } else if (role.equalsIgnoreCase("employee")) {
            System.out.println("\nWelcome Employee! You have view-only access.");
            runEmployeeMenu(sc);
        } else {
            System.out.println("Unknown role: " + role);
        }

        sc.close();
    }

    private static void runHrAdminMenu(Scanner sc) {
        HRAdminSearchService searchService = new HRAdminSearchService();
        HRAdminSalaryManager salaryManager = new HRAdminSalaryManager();
        Reports reports = new Reports();

        int choice;
        do {
            System.out.println("\n=== HR ADMIN MENU ===");
            System.out.println("1. Search/View/Edit Employee");
            System.out.println("2. Add New Employee");
            System.out.println("3. Salary Updates");
            System.out.println("4. Reports");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchService.initiateEmployeeSearch(sc);
                    break;

                case 2:
                    addEmployeeUI(sc);
                    break;
                case 3:
                    showSalaryMenu(sc, salaryManager);
                    break;
                case 4:
                    showReportsMenu(sc, reports);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void showReportsMenu(Scanner sc, Reports reports) {
        int choice;
        do {
            System.out.println("\n=== REPORTS MENU (HR Admin) ===");
            System.out.println("1. Total pay for month by Division");
            System.out.println("2. Total pay for month by Job Title");
            System.out.println("3. Employees hired within a date range");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter month (1-12): ");
                    int monthDiv = sc.nextInt();
                    System.out.print("Enter year: ");
                    int yearDiv = sc.nextInt();
                    System.out.print("Enter Division ID: ");
                    int divisionId = sc.nextInt();
                    sc.nextLine();
                    reports.generateDivisionReport(monthDiv, yearDiv, divisionId);
                    break;

                case 2:
                    System.out.print("Enter month (1-12): ");
                    int monthJob = sc.nextInt();
                    System.out.print("Enter year: ");
                    int yearJob = sc.nextInt();
                    System.out.print("Enter Job Title ID: ");
                    int jobTitleId = sc.nextInt();
                    sc.nextLine();
                    reports.generateJobTitleReport(monthJob, yearJob, jobTitleId);
                    break;

                case 3:
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    String endDate = sc.nextLine();
                    reports.generateHireDateReport(startDate, endDate);
                    break;

                case 0:
                    System.out.println("Returning to HR Admin menu.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void runEmployeeMenu(Scanner sc) {
        Reports reports = new Reports();
        EmployeeDAO dao = new EmployeeDAO();
        EmployeeViewService viewService = new EmployeeViewService();

        System.out.print("Enter your Employee ID to view your information: ");
        int empid = sc.nextInt();
        sc.nextLine();

        List<Employee> employees = dao.searchByEmpID(empid);
        if (employees.isEmpty()) {
            System.out.println("No employee found with ID " + empid);
            return;
        }

        Employee emp = employees.get(0);
        System.out.println("\nYour Profile:");
        viewService.displayEmployeeDetails(emp);

        System.out.println("\nYour Pay History:");
        reports.generatePayHistory(empid);
    }

    private static void addEmployeeUI(Scanner sc) {

        EmployeeDAO dao = new EmployeeDAO();
        Employee emp = new Employee();

        System.out.print("Enter Employee ID: ");
        emp.setEmpid(sc.nextInt());
        sc.nextLine();

        System.out.print("First Name: ");
        emp.setFname(sc.nextLine());

        System.out.print("Last Name: ");
        emp.setLname(sc.nextLine());

        System.out.print("DOB (YYYY-MM-DD): ");
        emp.setDob(sc.nextLine());

        System.out.print("SSN (9 digits): ");
        emp.setSSN(sc.nextLine());

        System.out.print("Hire Date (YYYY-MM-DD): ");
        emp.setHireDate(sc.nextLine());

        System.out.print("Street Address: ");
        emp.setStreet(sc.nextLine());


        System.out.print("Zip Code: ");
        emp.setZip(sc.nextLine());

        System.out.print("Enter City Name: ");
        String cityName = sc.nextLine();

        System.out.print("Enter State Name: ");
        String stateName = sc.nextLine();

        int cityId = dao.getCityIdByName(cityName);
        int stateId = dao.getStateIdByName(stateName);

        if (cityId == -1 || stateId == -1) {
            System.out.println("Invalid city or state. Employee not added.");
            return;
        }

        emp.setCity(cityName);
        emp.setCityId(cityId);

        emp.setState(stateName);
        emp.setStateId(stateId);

        System.out.println("\nJob Titles:");
        System.out.println("1 - Software Engineer");
        System.out.println("2 - Accountant");
        System.out.println("3 - Product Manager");
        System.out.println("4 - Marketing Specialist");
        System.out.println("5 - Business Analyst");
        System.out.print("Enter Job Title ID: ");
        int jobId = sc.nextInt();

        System.out.println("\nDivisions:");
        System.out.println("1 - IT");
        System.out.println("2 - Marketing");
        System.out.println("3 - Business");
        System.out.println("4 - Finance");
        System.out.println("5 - HR");
        System.out.print("Enter Division ID: ");
        int divId = sc.nextInt();
        sc.nextLine();

        dao.addEmployee(emp, jobId, divId, cityId, stateId);
    }
    
    private static void showSalaryMenu(Scanner sc, HRAdminSalaryManager salaryService) {

        System.out.println("\n=== SALARY UPDATE MENU ===");
        System.out.println("1. Update salary for ONE employee");
        System.out.println("2. Update salary for ALL employees below a max salary");
        System.out.println("0. Back");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Employee ID: ");
                int empID = sc.nextInt();
                System.out.print("Enter percent increase: ");
                double percent = sc.nextDouble();
                sc.nextLine();
                salaryService.increaseSalaryByEmpID(empID, percent);
                break;

            case 2:
                System.out.print("Enter maximum salary: ");
                double maxSalary = sc.nextDouble();
                System.out.print("Enter percent increase: ");
                double percentRange = sc.nextDouble();
                sc.nextLine();
                salaryService.increaseSalaryForRange(maxSalary, percentRange);
                break;

            case 0:
                return;

            default:
                System.out.println("Invalid choice.");
        }
    }


}
