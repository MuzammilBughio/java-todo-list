import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveTaskDAO {

    // Add an archived task to the database
    public static void addArchivedTask(TodoTask task, User user) throws SQLException {
        String sql = "INSERT INTO archived_tasks (title, details, deadline_date, deadline_time, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDetails());
            stmt.setString(3, task.getDeadlineDate());
            stmt.setString(4, task.getDeadlineTime());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }
    }

    // Get all archived tasks
    public static List<TodoTask> getAllArchivedTasks() throws SQLException {
        List<TodoTask> archivedTasks = new ArrayList<>();
        String sql = "SELECT * FROM archived_tasks";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TodoTask task = new TodoTask(
                        rs.getString("title"),
                        rs.getString("details"),
                        rs.getString("deadline_date"),
                        rs.getString("deadline_time")
                );
                archivedTasks.add(task);
            }
        }

        return archivedTasks;
    }
    public static void updateArchivedTask(TodoTask oldTask, TodoTask newTask) throws SQLException {
        String sql = "UPDATE archived_tasks SET title = ?, details = ?, deadline_date = ?, deadline_time = ? " +
                "WHERE title = ? AND deadline_date = ? AND deadline_time = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newTask.getTitle());
            stmt.setString(2, newTask.getDetails());
            stmt.setString(3, newTask.getDeadlineDate());
            stmt.setString(4, newTask.getDeadlineTime());

            stmt.setString(5, oldTask.getTitle());
            stmt.setString(6, oldTask.getDeadlineDate());
            stmt.setString(7, oldTask.getDeadlineTime());

            stmt.executeUpdate();
        }
    }

    public static void updateArchivedTask(TodoTask task, String oldTitle) throws SQLException {
        String sql = "UPDATE archived_tasks SET title = ?, details = ?, deadline_date = ?, deadline_time = ? WHERE title = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDetails());
            stmt.setString(3, task.getDeadlineDate());
            stmt.setString(4, task.getDeadlineTime());
            stmt.setString(5, oldTitle);
            stmt.executeUpdate();
        }
    }


    // delete an archived task
    public static void deleteArchivedTask(String title) throws SQLException {
        String sql = "DELETE FROM archived_tasks WHERE title = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        }
    }
    public static List<TodoTask> getArchivedTasksByUser(User user) throws SQLException {
        List<TodoTask> tasks = new ArrayList<>();
        String sql = "SELECT * FROM archived_tasks WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TodoTask task = new TodoTask(
                        rs.getString("title"),
                        rs.getString("details"),
                        rs.getString("deadline_date"),
                        rs.getString("deadline_time")
                );
                tasks.add(task);
            }
        }
        return tasks;
    }

}
