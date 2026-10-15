import java.util.Scanner;

public class Main {
    private static final String SAVE_FILE = "tasks.txt";

    private static void printMenu() {
        System.out.println("Check it off Menu:");
        System.out.println("1. Add a task");
        System.out.println("2. View tasks");
        System.out.println("3. Update a task");
        System.out.println("4. Check off a task");
        System.out.println("5. Get It Done");
        System.out.println("6. Quit");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SaveLoadTasks saver = new SaveLoadTasks(SAVE_FILE);
        LinkedChain<Task> tasks = saver.load();

        while (true) {
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
            else if (choice == 6) {
                saver.save(tasks);
                System.out.println("See you next time!");
                break;
            }
        }
    }
}