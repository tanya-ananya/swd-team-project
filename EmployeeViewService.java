public class EmployeeViewService {
    
    public void displayEmployeeDetails(Employee emp) {
        System.out.println("=== Employee Details ===");
        System.out.println("Name: " + emp.getFname() + " " + emp.getLname());
        System.out.println("Employee ID: " + emp.getEmpid());
        System.out.println("Date of Birth: " + emp.getDob());
        System.out.println("SSN: " + emp.getSSN());
        System.out.println("Hire Date: " + emp.getHireDate());
        System.out.println("Job Title: " + emp.getJobTitle());
        System.out.println("Division: " + emp.getDivision());
        System.out.println("Address: " + emp.getAddress() + ", " + emp.getCity() + ", " + emp.getState() + " " + emp.getZip());
    }
}
