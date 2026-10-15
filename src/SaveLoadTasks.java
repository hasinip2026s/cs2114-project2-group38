import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class SaveLoadTasks {

    private static final String DELIMITER = "|";
    private String filePath;

    /*
    Stores the file path. Doesn't open the file yet.
    Big-O: O(1)
    @param filePath where the tasks get saved
    */

    public SaveLoadTasks(String filePath) {
        this.filePath = filePath;
    }

    /*
    Writes one line per task. An empty chain writes an empty file.
    If the file can't be written, prints "Could not save tasks." and
    the program keeps running.
    Big-O: O(n), one line per task.
    @param tasks the list of tasks to save
    */

    public void save(LinkedChain<Task> tasks) {
        try (PrintWriter out = new PrintWriter(
            new FileWriter(this.filePath))) {
            for (Task t : tasks) {
                // LocalDateTime's toString gives yyyy-MM-ddTHH:mm
                out.println(t.getName() + DELIMITER + t.getDue()
                    + DELIMITER + t.getStatus() + DELIMITER
                    + t.getPriority());
            }
        }
        catch (IOException e) {
            System.out.println("Could not save tasks.");
        }
    }

    /*
    Reads the file into a new chain. A missing or empty file gives an
    empty chain. Lines with missing fields or a bad date are skipped
    and counted.
    Big-O: O(n), one line per task and each addToEnd() is O(1).
    @return a chain with every task that could be read
    */

    public LinkedChain<Task> load() {
        LinkedChain<Task> tasks = new LinkedChain<Task>();
        int skipped = 0;

        try (Scanner file = new Scanner(new File(this.filePath))) {
            while (file.hasNextLine()) {
                String line = file.nextLine();
                // | means "or" in split, so it has to be quoted
                String[] parts = line.split(Pattern.quote(DELIMITER), -1);

                if (parts.length != 4) {
                    skipped++;
                }
                else {
                    try {
                        LocalDateTime due = LocalDateTime.parse(parts[1]);
                        tasks.addToEnd(new Task(parts[0], due, parts[2],
                            parts[3]));
                    }
                    catch (DateTimeParseException e) {
                        skipped++;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            // first run, no save file yet: start with an empty list
            return tasks;
        }

        if (skipped > 0) {
            System.out.println("Skipped " + skipped
                + " unreadable task(s).");
        }
        return tasks;
    }

}