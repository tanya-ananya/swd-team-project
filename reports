import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reports {

    private static final String URL = "jdbc:mysql://localhost:3306/company_database";
    private static final String USER = "root";
    private static final String PASSWORD = "pALOmo1130";

    public void generateDivisionReport(int month, int year, int divisionId) {
        String sql = """
            SELECT d.division_name AS Division,
            SUM(p.total_pay) AS TotalPay
            FROM payments p
            JOIN employee_division ed ON p.empid = ed.empid
            JOIN division d ON ed.div_ID = d.ID
            WHERE MONTH(p.payment_date) = ?
            AND YEAR(p.payment_date) = ?
            AND d.ID = ?
            GROUP BY d.division_name;
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, divisionId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\nTotal Pay by Division for " + divisionId + month + "/" + year + ":");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                System.out.printf("%-20s $%.2f%n",
                        rs.getString("Division"),
                        rs.getDouble("TotalPay"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating division report: " + e.getMessage());
        }
    }


    public void generateJobTitleReport(int month, int year, int jobTitleId) {
        String sql = """
            SELECT jt.job_title AS JobTitle,
                   SUM(p.total_pay) AS TotalPay
            FROM payments p
            JOIN employee_job_titles ejt ON p.empid = ejt.empid
            JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id
            WHERE MONTH(p.payment_date) = ?
              AND YEAR(p.payment_date) = ?
              AND jt.job_title_id = ?
            GROUP BY jt.job_title;
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, jobTitleId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\nTotal Pay for Job Title ID " + jobTitleId +
                               " for " + month + "/" + year + ":");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                System.out.printf("%-25s $%.2f%n",
                        rs.getString("JobTitle"),
                        rs.getDouble("TotalPay"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating job title report: " + e.getMessage());
        }
    }

    public void generateHireDateReport(String startDate, String endDate) {
        String sql = """
            SELECT empid, fname, lname, hire_date
            FROM employees
            WHERE hire_date BETWEEN ? AND ?
            ORDER BY hire_date ASC;
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\nEmployees hired between " + startDate + " and " + endDate + ":");
            System.out.println("--------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%d | %s %s | %s%n",
                        rs.getInt("empid"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getDate("hire_date"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating hire date report: " + e.getMessage());
        }
    }

    public void generatePayHistory(int empid) {
        String sql = """
            SELECT payment_date, total_pay
            FROM payments
            WHERE empid = ?
            ORDER BY payment_date DESC;
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empid);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\nPay Statement History for Employee ID: " + empid);
            System.out.println("------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("Date: %s | Pay: $%.2f%n",
                        rs.getDate("payment_date"),
                        rs.getDouble("total_pay"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating pay history report: " + e.getMessage());
        }
    }
}
