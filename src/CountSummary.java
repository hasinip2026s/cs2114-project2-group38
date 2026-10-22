/*
 * Stretch goal: counts the tasks by status and priority, like
 * "5 tasks: 2 not started, 2 in-progress, 1 high priority."
 * @author [YOUR NAME HERE]
 * @version 2026.10.15
 */

public class CountSummary {

    /*
    Builds the summary line.
    Big-O: O(n), one pass with the iterator.
    @param tasks the list of tasks
    @return the summary, e.g. "5 tasks: 2 not started, 2 in-progress,
            1 high priority."
    */

    public static String summarize(LinkedChain<Task> tasks) {
        int notStarted = 0;
        int inProgress = 0;
        int high = 0;
        for (Task t : tasks) {
            if (t.getStatus().equals("not started")) {
                notStarted++;
            }
            else if (t.getStatus().equals("in-progress")) {
                inProgress++;
            }
            if (t.getPriority().equals("high")) {
                high++;
            }
        }

        String word = "tasks";
        if (tasks.size() == 1) {
            word = "task";
        }
        System.out.println();
        return tasks.size() + " " + word + ": " + notStarted
            + " not started, " + inProgress + " in-progress, " + high
            + " high priority.";
    }

}