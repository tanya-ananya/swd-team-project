import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authorization {

    // Returns "hr_admin", "employee", or null
    public static String login(String username, String password) {
        String sql = "SELECT role FROM login_credentials WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // 'hr_admin' or 'employee'
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Database error during login: " + e.getMessage());
            return null;
        }
    }
}
