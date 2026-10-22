import java.util.Scanner;

/**
 * 
 * @author Cassidy McCahill
 * @version 2026.10.14
 */

public class Main {

    private static final String SAVE_FILE = "tasks.txt";

    /*
    * Prints out the menu.
    */
    private static void printMenu() {
        System.out.println();
        System.out.println("Check It Off Menu:");
        System.out.println("1. Add a task");
        System.out.println("2. View tasks");
        System.out.println("3. Update a task");
        System.out.println("4. Check off a task");
        System.out.println("5. Get It Done");
        System.out.println("6. Quit");
        System.out.println();
    }

    /*
    * Makes the menu functionable. 
    * Choice 1 adds the task, choice 2 lets the user view the task list, choice 3 updates and saves the task,
    * choice 4 removes and saves the task, choice 5 shows the most important task (GetItDone), choice 6 ends the program.
    @param args
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SaveLoadTasks saver = new SaveLoadTasks(SAVE_FILE);
        LinkedChain<Task> tasks = saver.load();
        System.out.println();
        System.out.println("Tasks:");
        ViewList.show(tasks);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputValidator.readMenuChoice(scanner, 1, 6);

            if (choice == 1) {
                AddTask.add(tasks, scanner);
                saver.save(tasks);
            }
            else if (choice == 2) {
                ViewList.show(tasks);
            }
            else if (choice == 3) {
                if (UpdateTask.update(tasks, scanner)) {
                    saver.save(tasks);
                }
            }
            else if (choice == 4) {
                if (RemoveTask.remove(tasks, scanner)) {
                    saver.save(tasks);
                }
            }
            else if (choice == 5) {
                GetItDone.show(tasks);
            }
            else {
                saver.save(tasks);
                System.out.println();
                System.out.println("See you next time!");
                running = false;
            }
        }
        scanner.close();
    }

}