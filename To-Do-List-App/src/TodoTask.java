public class TodoTask {
    private int id; // new field
    private String title;
    private String details;
    private String deadlineDate;
    private String deadlineTime;

    public TodoTask(int id, String title, String details, String deadlineDate, String deadlineTime) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    public TodoTask(String title, String details, String deadlineDate, String deadlineTime) {
        this(0, title, details, deadlineDate, deadlineTime);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(String deadlineDate) { this.deadlineDate = deadlineDate; }

    public String getDeadlineTime() { return deadlineTime; }
    public void setDeadlineTime(String deadlineTime) { this.deadlineTime = deadlineTime; }
}
