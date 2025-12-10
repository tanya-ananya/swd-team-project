import java.sql.*;

public class HRAdminSalaryManager {

    private static final String URL = "jdbc:mysql://localhost:3306/company_database";
    private static final String USER = "root";
    private static final String PASSWORD = "pALOmo1130";

    public void increaseSalaryByEmpID(int empID, double percentIncrease) {

        String employeeCheckSQL = "SELECT fname, lname FROM employees WHERE empid = ?";
        String paymentCheckSQL = "SELECT COUNT(*) FROM payments WHERE empid = ?";
        String updateSQL = """
            UPDATE payments 
            SET total_pay = total_pay * (1 + ? / 100)
            WHERE empid = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement empCheckStmt = conn.prepareStatement(employeeCheckSQL);
            PreparedStatement payCheckStmt = conn.prepareStatement(paymentCheckSQL);
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

            empCheckStmt.setInt(1, empID);
            ResultSet empRs = empCheckStmt.executeQuery();

            if (!empRs.next()) {
                System.out.println("Employee ID " + empID + " does NOT exist in the system.");
                return;
            }

            String fname = empRs.getString("fname");
            String lname = empRs.getString("lname");

            payCheckStmt.setInt(1, empID);
            ResultSet payRs = payCheckStmt.executeQuery();
            payRs.next();

            if (payRs.getInt(1) == 0) {
                System.out.println("Employee " + fname + " " + lname +
                        " (ID " + empID + ") has NO payments on file.");
                return;
            }

            updateStmt.setDouble(1, percentIncrease);
            updateStmt.setInt(2, empID);
            int rows = updateStmt.executeUpdate();

            System.out.printf("Salary updated for %s %s (ID %d): %d payment record(s) updated.%n",
                    fname, lname, empID, rows);

        } catch (SQLException e) {
            System.out.println("Database Error (Single Update): " + e.getMessage());
        }
    }

    public void increaseSalaryForRange(double maxSalary, double percentIncrease) {

        String selectSQL = """
            SELECT e.empid, e.fname, e.lname, p.total_pay
            FROM payments p
            JOIN employees e ON p.empid = e.empid
            WHERE p.total_pay < ?
        """;

        String updateSQL = "UPDATE payments SET total_pay = ? WHERE empid = ?";

        int updatedCount = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

            selectStmt.setDouble(1, maxSalary);
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                int empID = rs.getInt("empid");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                double currentSalary = rs.getDouble("total_pay");
                double updatedSalary = currentSalary * (1 + percentIncrease / 100);

                updateStmt.setDouble(1, updatedSalary);
                updateStmt.setInt(2, empID);
                updateStmt.executeUpdate();

                System.out.printf(" Updated %s %s (ID %d):  $%.2f → $%.2f%n", fname, lname, empID, currentSalary, updatedSalary);

                updatedCount++;
            }

            System.out.printf("%n %d employee(s) under $%.2f updated by %.2f%%%n", updatedCount, maxSalary, percentIncrease);

        } catch (SQLException e) {
            System.out.println("Database Error (Range Update): " + e.getMessage());
        }
    }

    // Test Case 1 (bulk)
    public void testBulkIncrease() {
        System.out.println("\n[TEST CASE 1] Increase all salaries under $105,000 by 3.2%");
        increaseSalaryForRange(105000, 3.2);
    }

    // Test Case 2 (single valid employee)
    public void testSingleEmployeeIncrease() {
        System.out.println("\n[TEST CASE 2] Increase salary for employee ID 101 by 5%");
        increaseSalaryByEmpID(101, 5);
    }

    // Test Case 3 (invalid employee)
    public void testInvalidEmployee() {
        System.out.println("\n[TEST CASE 3] Attempt update for non-existent employee ID 999");
        increaseSalaryByEmpID(999, 5);
    }
}
