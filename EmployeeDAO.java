import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDAO implements IEmployeeDAO {
    String url = "jdbc:mysql://localhost:3306/company_database";
    String user = "root";
    String password = "password";
    String sqlTable = "SELECT e.empid, e.fname, e.lname, e.hire_date, e.ssn, e.dob, "
            + "jt.job_title, d.division_name, a.street, a.zip_code, a.phone_number, ac.city_name, sc.state_name " + 
            "FROM employees e " +
             "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
             "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
             "JOIN address a ON e.empid = a.empid " +
             "JOIN city ac ON a.city_id = ac.city_id " +
             "JOIN state sc ON a.state_id = sc.state_id " +
             "JOIN employee_division ed ON e.empid = ed.empid " +
             "JOIN division d ON ed.div_ID = d.ID ";

    @Override
    public List<Employee> searchByName(String firstName, String lastName) {
        List<Employee> employees = new ArrayList<>();

        String sqlcmd = sqlTable +
             "WHERE Fname = '" + firstName + "' AND Lname = '" + lastName + "' ORDER BY e.empid";

        MapRowToEmployees(employees, sqlcmd);

        return employees;
    }

    @Override
    public List<Employee> searchByDOB(String dob) {
        List<Employee> employees = new ArrayList<>();

        String sqlcmd = sqlTable + "WHERE e.dob = '" + dob.toString() + "' ORDER BY e.empid";
        
        MapRowToEmployees(employees, sqlcmd);

        return employees;
    }

    @Override
    public List<Employee> searchBySSN(String ssn) {
        List<Employee> employees = new ArrayList<>();

        String sqlcmd = sqlTable + "WHERE ssn = '" + ssn + "' ORDER BY e.empid";

        MapRowToEmployees(employees, sqlcmd);

        return employees;
    }

    @Override
    public List<Employee> searchByEmpID(int empID) {
        List<Employee> employees = new ArrayList<>();

        String sqlcmd = sqlTable + "WHERE e.empid = '" + empID + "' ORDER BY e.empid";

        MapRowToEmployees(employees, sqlcmd);

        return employees;
    }

    @Override
    public void displaySearchResults(List<Employee> results) {
        List<Employee> employees = results;
        int employeeCount = 1;
        for (Employee emp : employees) {
            System.out.println("[" + employeeCount + "] \tEmpID: " + emp.getEmpid() + " | " + emp.getFname() + " " + emp.getLname());
            System.out.println("\tDivision: " + emp.getDivision() + " | Job Title: " + emp.getJobTitle() + "\n");
            employeeCount++;
        }
    }

    @Override
    public void updateEmployeeDetails(int empid, String table, String field, String newValue) {
        String sqlUpdate = "UPDATE " + table + " SET " + field + " = " + newValue + " WHERE empid = " + empid;
        try (Connection myConn = DriverManager.getConnection(url, user, password);
                java.sql.Statement myStmt = myConn.createStatement()) {
            myStmt.executeUpdate(sqlUpdate);
            System.out.println("Employee detail updated successfully.");
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }
    private void MapRowToEmployees(List<Employee> employees, String sqcmdl) {
        try (Connection myConn = DriverManager.getConnection(url, user, password);
                java.sql.Statement myStmt = myConn.createStatement();
                ResultSet myRS = myStmt.executeQuery(sqcmdl)) {
            while (myRS.next()) {
                Employee emp = new Employee();
                emp.setEmpid(myRS.getInt("e.empid"));
                emp.setFname(myRS.getString("fname"));
                emp.setLname(myRS.getString("lname"));
                emp.setHireDate(myRS.getDate("hire_date").toString());
                emp.setSSN(myRS.getString("SSN"));
                emp.setAddress(myRS.getString("street"));
                emp.setCity(myRS.getString("city_name"));
                emp.setState(myRS.getString("state_name"));
                emp.setZip(myRS.getString("zip_code"));
                emp.setDob(myRS.getDate("dob").toString());
                emp.setJobTitle(myRS.getString("job_title"));
                emp.setDivision(myRS.getString("division_name"));
                emp.setPhoneNumber(myRS.getString("phone_number"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }
}
