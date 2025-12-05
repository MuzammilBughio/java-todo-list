import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/todolist";
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "babu456838"; // your MySQL Workbench password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL JDBC Driver!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to MySQL successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
