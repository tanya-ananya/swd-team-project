import java.util.List;
import java.util.Scanner;

public class HRAdminSearchService {
    public void initiateEmployeeSearch() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> results = null;
        
        System.out.println("=== HR ADMIN — EMPLOYEE SEARCH ===");
        System.out.println("----------------------------------------");
        System.out.println("Search employee by:");
        System.out.println("1. First & Last Name");
        System.out.println("2. Date of Birth");
        System.out.println("3. Social Security Number");
        System.out.println("4. Employee ID");
        System.out.println("----------------------------------------");
        System.out.print("Enter your choice (1-4): ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Enter First Name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter Last Name: ");
                String lastName = scanner.nextLine();
                results = employeeDAO.searchByName(firstName, lastName);
                break;
            case 2:
                System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                String dob = scanner.nextLine();
                results = employeeDAO.searchByDOB(dob);
                break;
            case 3:
                System.out.print("Enter Social Security Number (XXX-XX-XXXX): ");
                String ssn = scanner.nextLine();
                results = employeeDAO.searchBySSN(ssn);
                break;
            case 4:
                System.out.print("Enter Employee ID: ");
                String empId = scanner.nextLine();
                int empIdInt = Integer.parseInt(empId);
                results = employeeDAO.searchByEmpID(empIdInt);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                return;
        }
        
        System.out.println("\n=== Search Results ===");
        int selectedEmployee = -1;

        if (results == null || results.isEmpty()) { // no results
            System.out.println("No employees found matching the search criteria.");
            return;
        } else if (results.size() == 1) { // single result
            System.out.println("1 employee found:");
            System.out.println();
            selectedEmployee = 0;
        } else { // multiple results
            System.out.println(results.size() + " employees found:");
            System.out.println("Total Employees Found: " + results.size());
            employeeDAO.displaySearchResults(results);
            System.out.println("----------------------------------------");
            System.out.println("Enter the number of the employee to view more details, or 0 to exit ");
            int empChoice = scanner.nextInt();
            if (empChoice == 0) {
                System.out.println("Exiting search.");
                return;
            } else if (empChoice < 1 || empChoice > results.size()) {
                System.out.println("Invalid selection. Exiting search.");
                return;
            } else {
                selectedEmployee = empChoice - 1;
            }
        }
        EmployeeViewService viewService = new EmployeeViewService();
        viewService.displayEmployeeDetails(results.get(selectedEmployee));
        System.out.println("----------------------------------------");

        System.out.println("Would you like to edit this employee's details? (Y/N): ");
        String editChoice = scanner.next();
        if (editChoice.equalsIgnoreCase("Y")) {

            scanner.nextLine();

            HRAdminEditService editService = new HRAdminEditService();
            editService.editEmployeeDetails(scanner, employeeDAO, results.get(selectedEmployee));
        } else {
            System.out.println("Exiting.");
}
}
}
