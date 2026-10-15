import java.util.Scanner;

    /*
    * @author Hasini Pottipati
    * @version 2026.10.15
    */

public class RemoveTask {

    /*
    @param tasks the list of tasks
    @param in    the scanner to read from
    @return true if a task was removed, false if not
    */

    public static boolean remove(LinkedChain<Task> tasks, Scanner in) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet.");
            return false;
        }

        String name = InputValidator.readName(in);

        // find every task with that name (capital letters don't matter)
        LinkedChain<Task> matches = new LinkedChain<Task>();
        for (Task t : tasks) {
            if (t.getName().equalsIgnoreCase(name)) {
                matches.addToEnd(t);
            }
        }

        if (matches.isEmpty()) {
            System.out.println("No task named " + name + " found");
            return false;
        }

        Task toRemove;
        if (matches.size() == 1) {
            toRemove = matches.get(0);
        }
        else {
            System.out.println("More than one task is named " + name
                + ". Which one?");
            int number = 1;
            for (Task t : matches) {
                System.out.println(number + ". " + t);
                number++;
            }
            int choice = InputValidator.readMenuChoice(in, 1,
                matches.size());
            toRemove = matches.get(choice - 1);
        }

        return tasks.remove(toRemove);
    }

}