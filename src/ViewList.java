/**
 * Shows/ displays the task list.
 * @author Cassidy McCahill
 * @version 2026.10.14
 */

public class ViewList {

    /*
    * Prints out the task numbered task list. 
    * Prints out a message stating that there are no tasks if the list is empty.
    @param tasks the list of tasks
    */

    public static void show(LinkedChain<Task> tasks) {
        System.out.println();
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks in your list yet.");
        }
        else {
            int i = 1;
            for (Task task : tasks) {
                System.out.println(i + ". " + task);
                i++;
            }
        }
    }
}