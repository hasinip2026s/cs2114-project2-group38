public class ViewList {

    public static void show(LinkedChain<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks in your list yet.");
        }
        else {
            int i = 1
            for (Task task : tasks) {
                System.out.println(i +". "+ task);
                i++
            }
        }
    }
}