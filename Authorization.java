import java.sql.*;
public class Authorization {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/company_z";
    private static final String USER = "root"; // change if needed
    private static final String PASSWORD = "pALOmo1130"; // your MySQL password

    // Method to log in and return user role
    public static String login(String username, String password) {
        String sql = "SELECT role FROM authorization WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // HR_ADMIN or EMPLOYEE
            } else {
                return null; // no match
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
