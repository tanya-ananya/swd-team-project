import java.sql.*;
import java.util.Scanner;

// Class to manage database connection
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/company_database";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // change to your password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Employee class
class Employee {
    private int empid;
    private String fname;
    private String lname;
    private double total_pay;

    public Employee(int empid, String fname, String lname, double salary) {
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.total_pay = total_pay;
    }

    public int getEmpid() { return empid; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public double getSalary() { return total_pay; }
    public void setSalary(double salary) { this.total_pay = total_pay; }
}

// Service class for salary updates
class SalaryUpdateService {

    // Update a single employee by ID
    public void increaseSalaryByEmpID(int empID, double percentIncrease) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if employee exists
            String checkSql = "SELECT empid, fname, lname, total_pay FROM payments WHERE empid = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, empID);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("ERROR: Employee ID " + empID + " does not exist.");
                return;
            }

            double currentSalary = rs.getDouble("total_pay");
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");

            double updatedSalary = currentSalary * (1 + percentIncrease / 100);

            // Update salary
            String updateSql = "UPDATE payments SET total_pay = ? WHERE empid = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDouble(1, updatedSalary);
            updateStmt.setInt(2, empID);
            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                System.out.printf("Employee %s %s (ID %d) salary updated: $%.2f -> $%.2f%n",
                        fname, lname, empID, currentSalary, updatedSalary);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Update all employees below a max salary
    public void increaseSalaryForRange(double maxSalary, double percentIncrease) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String selectSql = "SELECT empid, fname, lname, total_pay FROM payments WHERE total_pay < ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setDouble(1, maxSalary);
            ResultSet rs = selectStmt.executeQuery();

            int updatedCount = 0;
            while (rs.next()) {
                int empID = rs.getInt("empid");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                double currentSalary = rs.getDouble("total_pay");
                double updatedSalary = currentSalary * (1 + percentIncrease / 100);

                String updateSql = "UPDATE payments SET total_pay = ? WHERE empid = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setDouble(1, updatedSalary);
                updateStmt.setInt(2, empID);
                updateStmt.executeUpdate();

                System.out.printf("Updated %s %s (ID %d): $%.2f -> $%.2f%n",
                        fname, lname, empID, currentSalary, updatedSalary);
                updatedCount++;
            }

            System.out.printf("%d employee(s) earning less than $%.2f have been updated with a %.2f%% increase.%n",
                    updatedCount, maxSalary, percentIncrease);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}

// Main class with test cases
public class HRAdminSalaryManager {
    public static void main(String[] args) {
        SalaryUpdateService salaryService = new SalaryUpdateService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== HR ADMIN SALARY UPDATE ===");
        System.out.println("Choose an option:");
        System.out.println("1. Update salary for a single employee by ID");
        System.out.println("2. Update salary for all employees below a max salary");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Employee ID: ");
                int empID = scanner.nextInt();
                System.out.print("Enter percent increase (e.g., 3.2): ");
                double percent = scanner.nextDouble();
                salaryService.increaseSalaryByEmpID(empID, percent);
                break;

            case 2:
                System.out.print("Enter maximum salary limit: ");
                double maxSalary = scanner.nextDouble();
                System.out.print("Enter percent increase (e.g., 3.2): ");
                double rangePercent = scanner.nextDouble();
                salaryService.increaseSalaryForRange(maxSalary, rangePercent);
                break;

            default:
                System.out.println("Invalid choice.");
        }

        // --- Test Cases ---
        System.out.println("\n=== TEST CASES ===");
        System.out.println("Test 1: Increase salary by 3.2% for all employees below $105,000");
        salaryService.increaseSalaryForRange(105000, 3.2);

        System.out.println("\nTest 2: Update salary for employee ID 101 by 5%");
        salaryService.increaseSalaryByEmpID(101, 5);

        System.out.println("\nTest 3: Attempt update for non-existent Employee ID 999");
        salaryService.increaseSalaryByEmpID(999, 5);

        scanner.close();
    }
}
