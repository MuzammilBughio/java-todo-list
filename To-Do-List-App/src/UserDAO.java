import java.sql.*;

public class UserDAO {
    public static User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        }
        return null;
    }

    public static boolean createUser(String username, String password) throws SQLException {
        if (getUserByUsername(username) != null) return false;

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        }
    }

    public static User validateLogin(String username, String password) throws SQLException {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // successful login
        }
        return null; // failed login
    }

}
