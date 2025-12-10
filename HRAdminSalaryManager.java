import java.sql.*;
import java.util.Scanner;

public class HRAdminSalaryManager {

    public static void main(String[] args) {

        final String URL = "jdbc:mysql://localhost:3306/company_database";
        final String USER = "root";
        final String PASSWORD = "password";

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== HR ADMIN — SALARY UPDATE ===");
        System.out.println("----------------------------------------");
        System.out.println("1. Update salary for a single employee by ID");
        System.out.println("2. Update salary for all employees below a maximum salary");
        System.out.println("----------------------------------------");
        System.out.print("Enter your choice (1-2): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            if (choice == 1) {
                System.out.print("Enter Employee ID: ");
                int empID = scanner.nextInt();
                System.out.print("Enter percent increase: ");
                double percent = scanner.nextDouble();

                String checkSql = "SELECT empid, fname, lname, total_pay FROM payments WHERE empid = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, empID);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("ERROR: Employee ID " + empID + " does not exist.");
                } else {
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    double currentSalary = rs.getDouble("total_pay");
                    double updatedSalary = currentSalary * (1 + percent / 100);

                    String updateSql = "UPDATE payments SET total_pay = ? WHERE empid = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setDouble(1, updatedSalary);
                    updateStmt.setInt(2, empID);
                    updateStmt.executeUpdate();

                    System.out.printf("Updated %s %s (ID %d): $%.2f -> $%.2f%n",
                            fname, lname, empID, currentSalary, updatedSalary);
                }
            }

            else if (choice == 2) {
                System.out.print("Enter maximum salary: ");
                double maxSalary = scanner.nextDouble();
                System.out.print("Enter percent increase: ");
                double percent = scanner.nextDouble();

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
                    double updatedSalary = currentSalary * (1 + percent / 100);

                    String updateSql = "UPDATE payments SET total_pay = ? WHERE empid = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setDouble(1, updatedSalary);
                    updateStmt.setInt(2, empID);
                    updateStmt.executeUpdate();

                    System.out.printf("Updated %s %s (ID %d): $%.2f -> $%.2f%n",
                            fname, lname, empID, currentSalary, updatedSalary);
                    updatedCount++;
                }

                System.out.printf("%d employee(s) updated under $%.2f with %.2f%% increase.%n",
                        updatedCount, maxSalary, percent);
            }

            else {
                System.out.println("Invalid choice.");
            }



            System.out.println("\n=== TEST CASES ===");

            System.out.println("\nTest 1: Update all employees under 105000 by 3.2%");
            PreparedStatement stmt1 = conn.prepareStatement(
                    "SELECT empid, fname, lname, total_pay FROM payments WHERE total_pay < 105000");
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                double current = rs1.getDouble("total_pay");
                double updated = current * 1.032;
                PreparedStatement u = conn.prepareStatement(
                        "UPDATE payments SET total_pay = ? WHERE empid = ?");
                u.setDouble(1, updated);
                u.setInt(2, rs1.getInt("empid"));
                u.executeUpdate();
                System.out.println("Test 1 Updated ID " + rs1.getInt("empid"));
            }

            System.out.println("\nTest 2: Update salary for employee ID 101 by 5%");
            PreparedStatement stmt2 = conn.prepareStatement(
                    "UPDATE payments SET total_pay = total_pay * 1.05 WHERE empid = 101");
            stmt2.executeUpdate();
            System.out.println("Test 2 Completed!");

            System.out.println("\nTest 3: Update non-existent Employee ID 999");
            PreparedStatement stmt3 = conn.prepareStatement(
                    "UPDATE payments SET total_pay = total_pay * 1.05 WHERE empid = 999");
            int rows = stmt3.executeUpdate();
            if (rows == 0) {
                System.out.println("Test 3: Employee ID 999 not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        scanner.close();
    }
}
