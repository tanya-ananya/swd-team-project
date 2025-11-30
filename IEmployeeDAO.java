import java.util.List;

public interface IEmployeeDAO {
    
    List<Employee> searchByName(String firstName, String lastName);

    List<Employee> searchByDOB(String dob);

    List<Employee> searchBySSN(String ssn);

    List<Employee> searchByEmpID(int empID);

    public void displaySearchResults(List<Employee> results);

    public void updateEmployeeDetails(int empid, String table, String field, String newValue);
}
