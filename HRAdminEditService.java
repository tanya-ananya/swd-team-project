import java.util.Scanner;

public class HRAdminEditService {
    public void editEmployeeDetails(Scanner scanner, EmployeeDAO employeeDAO, Employee emp) {
        


        String key;
        boolean editing = true;

        while (editing) {
            System.out.println("Editing details for Employee ID: " + emp.getEmpid());
            System.out.println("----------------------------------------");
            System.out.println("1. Current First Name: " + emp.getFname());
            System.out.println("2. Current Last Name: " + emp.getLname());
            System.out.println("3. Current Job Title and Division: " + emp.getJobTitle() + " in " + emp.getDivision());
            System.out.println("4. Current Address: " + emp.getStreet() + ", " + emp.getCity() + ", " + emp.getState() + " " + emp.getZip());
            System.out.println("----------------------------------------");
            System.out.println("Enter the number of the field you want to edit (or 0 to exit): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: // Edit First Name
                    System.out.print("Enter new First Name: ");
                    String newFname = scanner.nextLine();
                    emp.setFname(newFname);
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "employees", "fname", "'" + newFname + "'");
                    break;

                case 2: // Edit Last Name
                    System.out.print("Enter new Last Name: ");
                    String newLname = scanner.nextLine();
                    emp.setLname(newLname);
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "employees","lname", "'" + newLname + "'");
                    break;

                case 3: // Edit Job Title and Division
                    System.out.print("Enter new Job Title: ");
                    String newJobTitle = scanner.nextLine();
                    emp.setJobTitle(newJobTitle);
                    key = "(SELECT job_title_id FROM job_titles WHERE job_title = '"+ newJobTitle +"')";
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "employee_job_titles", "job_title_id", key);

                    System.out.print("Enter new Division: ");
                    String newDivision = scanner.nextLine();
                    emp.setDivision(newDivision);
                    key = "(SELECT ID FROM division WHERE division_name = '"+ newDivision +"')";
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "employee_division", "div_ID", key);
                    break;

                case 4: // Edit Address
                    System.out.print("Enter new Street Name: ");
                    String newAddress = scanner.nextLine();
                    emp.setAddress(newAddress);
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "address","street", "'" + newAddress + "'");

                    System.out.print("Enter new City: ");
                    String newCity = scanner.nextLine();
                    emp.setCity(newCity);
                    key = "(SELECT city_ID FROM city WHERE city_name = '"+ newCity +"')";
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "address ", "city_id", key);
                    
                    System.out.print("Enter new State: ");
                    String newState = scanner.nextLine();
                    emp.setState(newState);
                    key = "(SELECT state_ID FROM state WHERE state_name = '"+ newState +"')";
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "address", "state_id", key);

                    System.out.print("Enter new Zip: ");
                    String newZip = scanner.nextLine();
                    emp.setZip(newZip);
                    employeeDAO.updateEmployeeDetails(emp.getEmpid(), "address","zip_code", "'" + newZip + "'");
                    break;

                case 0: // Exit
                    System.out.println("Exiting edit mode.");
                    return;

                default: // Invalid choice
                    System.out.println("Invalid choice. Exiting edit mode.");
                    return;
            }
            System.out.println("Edit another field? (Y/N): ");
            String continueEdit = scanner.nextLine();
            if (!continueEdit.equalsIgnoreCase("Y")) {
                editing = false;
                System.out.println("Exiting edit mode.");
            }
        }
    }
}
