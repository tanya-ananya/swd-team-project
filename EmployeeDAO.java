import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IEmployeeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/company_database";
    private static final String USER = "root";
    private static final String PASSWORD = "pALOmo1130";
    private final String baseSelect =
        "SELECT e.empid, e.fname, e.lname, e.dob, e.ssn, e.hire_date, " +
        "jt.job_title_id, jt.job_title, " +
        "d.division_name, " +
        "a.street AS street, " +
        "a.zip_code AS zip_code, " +
        "c.city_name AS city_name, " +
        "s.state_name AS state_name " +
        "FROM employees e " +
        "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
        "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
        "JOIN employee_division ed ON e.empid = ed.empid " +
        "JOIN division d ON ed.div_ID = d.ID " +
        "JOIN address a ON e.empid = a.empid " +
        "JOIN city c ON a.city_id = c.city_id " +
        "JOIN state s ON a.state_id = s.state_id ";


    @Override
    public List<Employee> searchByEmpID(int empID) {
        List<Employee> list = new ArrayList<>();
        mapResults(list, baseSelect + " WHERE e.empid = " + empID);
        return list;
    }

    @Override
    public List<Employee> searchByName(String firstName, String lastName) {
        List<Employee> list = new ArrayList<>();
        mapResults(list, baseSelect +
            " WHERE e.fname = '" + firstName + "' AND e.lname = '" + lastName + "'");
        return list;
    }

    @Override
    public List<Employee> searchByDOB(String dob) {
        List<Employee> list = new ArrayList<>();
        mapResults(list, baseSelect + " WHERE e.dob = '" + dob + "'");
        return list;
    }

    @Override
    public List<Employee> searchBySSN(String ssn) {
        List<Employee> list = new ArrayList<>();
        mapResults(list, baseSelect + " WHERE e.ssn = '" + ssn + "'");
        return list;
    }

    @Override
    public void displaySearchResults(List<Employee> results) {
        int i = 1;
        for (Employee emp : results) {
            System.out.println("[" + i + "] " + emp.getEmpid() + " - " +
                emp.getFname() + " " + emp.getLname());
            System.out.println("    Division: " + emp.getDivision() +
                " | Job: " + emp.getJobTitle());
            i++;
        }
    }

    @Override
    public void updateEmployeeDetails(int empid, String table, String field, String newValue) {
        String sql = "UPDATE " + table + " SET " + field + " = " + newValue +
                     " WHERE empid = " + empid;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Employee updated successfully.");

        } catch (SQLException e) {
            System.out.println("ERROR updating employee: " + e.getMessage());
        }
    }

    public int getCityIdByName(String cityName) {
        String sql = "SELECT city_id FROM city WHERE city_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("city_id") : -1;

        } catch (SQLException e) {
            System.out.println("City lookup error: " + e.getMessage());
            return -1;
        }
    }

    public int getStateIdByName(String stateName) {
        String sql = "SELECT state_id FROM state WHERE state_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stateName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("state_id") : -1;

        } catch (SQLException e) {
            System.out.println("State lookup error: " + e.getMessage());
            return -1;
        }
    }

    public void addEmployee(Employee emp, int jobTitleId, int divisionId,
                            int cityId, int stateId) {

        String empSQL = "INSERT INTO employees (empid, fname, lname, dob, ssn, hire_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        String jobSQL = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";

        String divSQL = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";

        String addrSQL = "INSERT INTO address (empid, street, city_id, state_id, zip_code, dob) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement empStmt = conn.prepareStatement(empSQL);
                 PreparedStatement jobStmt = conn.prepareStatement(jobSQL);
                 PreparedStatement divStmt = conn.prepareStatement(divSQL);
                 PreparedStatement addrStmt = conn.prepareStatement(addrSQL)) {

                empStmt.setInt(1, emp.getEmpid());
                empStmt.setString(2, emp.getFname());
                empStmt.setString(3, emp.getLname());
                empStmt.setDate(4, Date.valueOf(emp.getDob()));
                empStmt.setString(5, emp.getSSN());
                empStmt.setDate(6, Date.valueOf(emp.getHireDate()));
                empStmt.executeUpdate();

                jobStmt.setInt(1, emp.getEmpid());
                jobStmt.setInt(2, jobTitleId);
                jobStmt.executeUpdate();

                divStmt.setInt(1, emp.getEmpid());
                divStmt.setInt(2, divisionId);
                divStmt.executeUpdate();

                addrStmt.setInt(1, emp.getEmpid());
                addrStmt.setString(2, emp.getStreet());
                addrStmt.setInt(3, cityId);
                addrStmt.setInt(4, stateId);
                addrStmt.setString(5, emp.getZip());
                addrStmt.setDate(6, Date.valueOf(emp.getDob()));
                addrStmt.executeUpdate();

                conn.commit();
                System.out.println("Employee added successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Add employee failed: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("DB Connection Error: " + e.getMessage());
        }
    }

    private void mapResults(List<Employee> list, String sql) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee emp = new Employee();

                emp.setEmpid(rs.getInt("empid"));
                emp.setFname(rs.getString("fname"));
                emp.setLname(rs.getString("lname"));
                emp.setDob(rs.getDate("dob").toString());
                emp.setSSN(rs.getString("ssn"));
                emp.setHireDate(rs.getDate("hire_date").toString());

                emp.setJobTitleId(rs.getInt("job_title_id"));
                emp.setJobTitle(rs.getString("job_title"));
                emp.setDivision(rs.getString("division_name"));

                emp.setStreet(rs.getString("street"));
                emp.setZip(rs.getString("zip_code"));
                emp.setCity(rs.getString("city_name"));
                emp.setState(rs.getString("state_name"));

                list.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("Query Error: " + e.getMessage());
        }
    }
}
