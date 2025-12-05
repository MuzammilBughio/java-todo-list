import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // Save a task to the database
    public static void addTask(TodoTask task, User user) throws SQLException {
        String sql = "INSERT INTO tasks (title, details, deadline_date, deadline_time, user_id) VALUES (?, ?, ?, ?, ?)";
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


    // Get all tasks from the database
    public static List<TodoTask> getAllTasks() throws SQLException {
        List<TodoTask> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE archived = FALSE";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String details = rs.getString("details");
                String date = rs.getString("deadline_date");
                String time = rs.getString("deadline_time");

                TodoTask task = new TodoTask(title, details, date, time);
                tasks.add(task);
            }
        }

        return tasks;
    }
    public static void updateTask(TodoTask oldTask, TodoTask newTask) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, details = ?, deadline_date = ?, deadline_time = ? " +
                "WHERE title = ? AND deadline_date = ? AND deadline_time = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newTask.getTitle());
            stmt.setString(2, newTask.getDetails());
            stmt.setString(3, newTask.getDeadlineDate());
            stmt.setString(4, newTask.getDeadlineTime());

            // identifying old record
            stmt.setString(5, oldTask.getTitle());
            stmt.setString(6, oldTask.getDeadlineDate());
            stmt.setString(7, oldTask.getDeadlineTime());

            stmt.executeUpdate();
        }
    }

    public static void archiveTask(TodoTask task, User user) throws SQLException {
        String sql = "UPDATE tasks SET archived = TRUE WHERE title = ? AND deadline_date = ? AND deadline_time = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDeadlineDate());
            stmt.setString(3, task.getDeadlineTime());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }


    public static List<TodoTask> getTasksByUser(User user) throws SQLException {
        List<TodoTask> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND archived = 0"; // assuming `archived` column
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

    public static void deleteTask(TodoTask task, User user) throws SQLException {
        String sql = "DELETE FROM tasks WHERE title = ? AND deadline_date = ? AND deadline_time = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDeadlineDate());
            stmt.setString(3, task.getDeadlineTime());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }


}
