import java.time.LocalDate;

public class GetItDone {

    public static Task pick(LinkedChain<Task> tasks) {
        Task top = null;
        for (Task task : tasks) {
            if (!task.getStatus().equals("complete")) {
                if (top == null) {
                    top = task;
                }
                else {
                    LocalDate taskDate = task.getDue().toLocalDate();
                    LocalDate topDate = top.getDue().toLocalDate();

                    if (!taskDate.equals(topDate)) {
                        if (taskDate.isBefore(topDate)) {
                            top = task;
                        }
                    }
                    else if (task.getPriorityRank() != top.getPriorityRank()) {
                        if (task.getPriorityRank() > top.getPriorityRank()) {
                            top = task;
                        }
                    }
                    else if (task.getDue().toLocalTime().isBefore(top.getDue().toLocalTime())) {
                        top = task;
                    } 
                }
            }
        }
        return top;
    }
    public static void show(LinkedChain<Task> tasks) {
        Task top = pick(tasks);

        if (top == null) {
            System.out.println("Nothing to do!");
        }
        else {
            System.out.println(top);
        }
    }
}