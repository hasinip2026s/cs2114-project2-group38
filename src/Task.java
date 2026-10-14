import java.time.LocalDateTime;

 // Holds task's data: name, due date/time, status, and priority.

public class Task {

    private String name;
    private LocalDateTime due;
    private String status;
    private String priority;
    
    /*
    Stores the four fields. Assumes the values were already checked by InputValidator.
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


}