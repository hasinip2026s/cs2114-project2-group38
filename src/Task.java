import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Holds a task's data: name, due date/time, status, and priority.
 *
 * @author Hasini Pottipati
 * @version 2026.10.14
 */

public class Task {

    private String name;
    private LocalDateTime due;
    private String status;
    private String priority;
    
    /*
    Stores the four fields.
    @param name     the task's name
    @param due      the due date and time
    @param status   "not started", "in-progress", or "complete"
    @param priority "low", "medium", or "high"
    */

    public Task(String name, LocalDateTime due, String status, String priority) {
        this.name = name;
        this.due = due;
        this.status = status;
        this.priority = priority;
    }

    /*
    @return the task's name
    */
    
    public String getName() {
        return this.name;
    } 


    /*
    @return the due date and time
    */

    public LocalDateTime getDue() {
        return this.due;
    }
 
    /*
    @return the status
    */

    public String getStatus() {
        return this.status;
    }
 
    /*
    @return the priority
    */

    public String getPriority() {
        return this.priority;
    }
 
    /*
    Changes the status.
    @param status the new status
    */

    public void setStatus(String status) {
        this.status = status;
    }
 
    /*
    Changes the priority.
    @param priority the new priority
    */
    
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /*
    Turns the priority into a number so GetItDone can compare tasks.
    @return 3 for high, 2 for medium, 1 for low
    */

    public int getPriorityRank() {
        if (this.priority.equals("high")) {
            return 3;
        }
        else if (this.priority.equals("medium")) {
            return 2;
        }
        else {
            return 1;
        }
    }

    /*
    Checks if two tasks are the same. The name can have different
    capital letters, but everything else has to match exactly.
    @param other the thing we're comparing to
    @return true if they're the same task, false if not (or if other
            is null or isn't a Task)
    */

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Task)) {
            return false;
        }
        Task that = (Task)other;
        return this.name.equalsIgnoreCase(that.name)
            && this.due.equals(that.due)
            && this.status.equals(that.status)
            && this.priority.equals(that.priority);
    }

    /*
    Matches equals. The name is lowercased because equals ignores
    capital letters.
    @return the hash code
    */

    @Override
    public int hashCode() {
        return Objects.hash(this.name.toLowerCase(), this.due, this.status,
            this.priority);
    }

    /*
    Shows the task on one line, like:
    Buy textbook — due 10-02-2026 17:00 — not started — low
    @return the task as a string
    */

    @Override
    public String toString() {
        DateTimeFormatter format =
            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return this.name + " — due " + this.due.format(format) + " — "
            + this.status + " — " + this.priority;
    }

}