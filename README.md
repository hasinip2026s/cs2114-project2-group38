# Check It Off

A Java console to-do list by students, for students. Users can add tasks, check them off, and ask "Get It Done" what to work on next.

## What it does

Check It Off keeps track of day-to-day tasks so students know exactly what needs to be finished and when.

- **Add a task** — enter a name, due date and time, status, and priority.
- **View the list** — see every current task, numbered, with all its details.
- **Update a task** — change a task's status (not started, in-progress, complete) or priority (low, medium, high) at any time.
- **Check off a task** — remove a finished task from the list.
- **Get It Done** — tells you what to work on next. It picks the task with the earliest due date, skipping anything already complete. If two tasks are due on the same day, the higher priority wins; after that, the earlier time, then whichever was added first.
- **Save and load** — tasks are saved to a file after every change and loaded on startup, so the list is exactly as you left it.

Everything is stored in one generic class, `LinkedChain<T>`. The rest of the program uses the chain only through its public methods and never reaches into its nodes.

## Tech Stack

| Piece | What it is |
|---|---|
| Java | Language |
| `java.time` (`LocalDate`, `LocalTime`, `LocalDateTime`) | Strict date/time parsing and comparison |
| Java file I/O | Saving and loading tasks to `tasks.txt` |
| JUnit | Unit tests for the app classes |
| `LinkedChain<T>` | The generic singly linked list we were given, which holds every task |

## Project Structure

```
check-it-off/
├── README.md
├── src/
│   ├── Main.java
│   ├── Task.java
│   ├── LinkedChain.java
│   ├── InputValidator.java
│   ├── AddTask.java
│   ├── ViewList.java
│   ├── UpdateTask.java
│   ├── RemoveTask.java
│   ├── GetItDone.java
│   └── SaveLoadTasks.java
└── test/
    └── [TODO: list your test files, e.g. TaskTest.java]
```

## Getting Started

### Prerequisites

- A JDK installed
- JUnit [TODO: version, e.g. JUnit 5] on the build path
- Eclipse or VS Code (both work — see below)

### Running the program in Eclipse

1. File → Open Projects from File System → select this folder
2. Right-click the project → Run As → Java Application
3. Select `Main` as the class to run

### Running the program in VS Code

1. Install the "Extension Pack for Java"
2. Open this folder in VS Code
3. Open `Main.java` and click the ▶ Run button above `main()`

### Compiling and running from the command line

```
javac -d bin src/*.java
java -cp bin Main
```

### Running the tests

- **Eclipse:** right-click the `test` folder → Run As → JUnit Test. All tests should show green in the JUnit tab.
- **VS Code:** open the Testing panel (flask icon) and click ▶ Run All Tests.

### Using the menu

```
1 Add
2 View
3 Update
4 Check off
5 Get It Done
6 Quit
```

Dates are entered as `MM-DD-YYYY` and times as `HH:MM` (24-hour, 00:00 to 23:59).

## Scope

### Minimum Viable Product

```
Launch → Load & View Tasks → Menu → Add Tasks → Update Tasks → Complete Tasks → Save → Quit
```

The MVP is one loop around a task's life. Add creates a task in the `LinkedChain`, View lets the user confirm it and pick which task to change, Update moves it toward completion, and Check Off removes it once it's done. Every change is followed by a save, and Load rebuilds the chain on startup.

**Non-functional requirements**

- The program never crashes on bad input. It shows a clear message and asks again.
- Saved data survives closing and reopening the program.
- Menu prompts are simple enough that a new user needs no instructions.

## Design

### Classes

| Class | Purpose |
|---|---|
| `Main` | Starts the program, loads tasks, runs the menu loop, and saves after every change. |
| `Task` | Holds one task's data: name, due date/time, status, priority. |
| `LinkedChain<T>` | Stores elements in insertion order. Supports add, remove, contains, get, size, and iteration. |
| `Node<T>` | One link in the chain (data + next). Private inner class of `LinkedChain`. |
| `InputValidator` | Reads and checks every typed value. Re-prompts until the input is valid. |
| `AddTask` | Collects a new task's fields through `InputValidator` and adds it to the chain. |
| `RemoveTask` | Lets the user pick a task by name and remove it (check off). |
| `UpdateTask` | Lets the user pick a task and change its status or priority. |
| `ViewList` | Prints every task in numbered form. |
| `GetItDone` | Finds and displays the next most important task. |
| `SaveLoadTasks` | Turns the chain into file lines and file lines back into a chain. |

Each menu action is its own class so it can be written and tested separately without merge conflicts in one big file.

### Element types in the LinkedChain

| Element type | Where it's stored | Where it's retrieved |
|---|---|---|
| `Task` | `LinkedChain<Task>` | [TODO: e.g. ViewList, GetItDone, RemoveTask] |
| [TODO: second type, e.g. `TaskList`] | [TODO] | [TODO] |

Both go through the same generic `LinkedChain<T>` class, with no type-specific copies.

### The Generic Contract

```java
public class LinkedChain<T> implements Iterable<T>
```

- Any non-null reference type works. `add(null)` throws `IllegalArgumentException`, so the chain never holds nulls.
- Insertion order is preserved; new items go at the tail.
- Duplicates are allowed (two "Laundry" tasks on different days).
- `contains`, `count`, and `remove` use `T.equals()`. `Task` overrides `equals()` and `hashCode()` so this works by value.
- No bounds on `T`, since the chain is unordered and only ever calls `equals()`.

### LinkedChain Methods

| Method | Provided or added | Big-O |
|---|---|---|
| `LinkedChain()` | Provided | O(1) |
| `add(T item)` | Provided (null check added) | O(1) — links after tail |
| `remove(T item)` | Provided | O(n) |
| `removeAt(int index)` | Added | O(n) |
| `contains(T item)` | Provided | O(n) |
| `get(int index)` | Added | O(n) |
| `count(T item)` | Added | O(n) |
| `size()` | Provided | O(1) |
| `isEmpty()` | Added | O(1) |
| `iterator()` | Added | O(1) to create, O(n) to walk |

Loops over the chain use the iterator (for-each), not `get(i)` in a for loop, which would make the loop O(n²).

### Task

| Field | Type | Values |
|---|---|---|
| `name` | `String` | Any non-empty name without `\|` |
| `due` | `LocalDateTime` | Built from `MM-DD-YYYY` and `HH:MM` input |
| `status` | `String` | "not started", "in-progress", "complete" |
| `priority` | `String` | "low", "medium", "high" (ranked 1, 2, 3 for Get It Done) |

### Save File Format

Tasks are saved to `tasks.txt`, one per line, fields separated by a pipe:

```
name|yyyy-MM-ddTHH:mm|status|priority
```

## Handling Bad Input

| Bad input | Example | Caught in | What happens |
|---|---|---|---|
| Date has passed | 01-15-2020 | `InputValidator.readDate()` | Warns "That date has already passed" and asks whether to keep it |
| Impossible date | 02-30-2026 | `InputValidator.readDate()` | "Enter a real date as MM-DD-YYYY", re-prompts |
| Bad time | 25:70 | `InputValidator.readTime()` | "Enter a time as HH:MM (00:00 to 23:59)", re-prompts |
| Bad status or priority | "urgent" | `InputValidator.readStatus()` / `readPriority()` | Shows the valid options, re-prompts |
| Empty task name | (Enter) | `InputValidator.readName()` | "Task name can't be empty", re-prompts |
| Name contains `\|` | "Lab \| part 2" | `InputValidator.readName()` | "Task name can't contain \|", re-prompts |
| Bad menu choice | "7" or "abc" | `InputValidator.readMenuChoice()` | Shows the valid range, re-prompts |
| Removing a task that doesn't exist | "Laundry" | `RemoveTask.remove()` | "No task named Laundry found", back to menu |
| Two tasks with the same name | Two "Laundry" tasks | `RemoveTask.remove()` | Lists the matches and asks which one |
| Empty list | Update / Check off / Get It Done | `UpdateTask` / `RemoveTask` / `GetItDone` | "No tasks yet" or "Nothing to do!" |
| `null` passed to the chain | `add(null)` | `LinkedChain.add()` | Throws `IllegalArgumentException` |
| Save file missing | First run | `SaveLoadTasks.load()` | Starts with an empty list, no error |
| Corrupted line in save file | Missing fields | `SaveLoadTasks.load()` | Skips it and prints "Skipped N unreadable task(s)." |
| File can't be written | Permission problem | `SaveLoadTasks.save()` | "Could not save tasks", program keeps running |

## Reflection

### What changed from our specification?

| What changed | The spec said | What we built | Why | Where the change came from |
|---|---|---|---|---|
| [TODO: class, method signature, validation spot, or test] | [TODO] | [TODO] | [TODO] | [TODO: group discussion, GenAI, or own reflection] |
| [TODO] | [TODO] | [TODO] | [TODO] | [TODO] |

### What changed in the LinkedChain?

[TODO: Compare our spec's "Provided or added" table against the `LinkedChain.java` we were given. For each method or field you added or changed (tail reference, null check in `add`, `get`, `removeAt`, `count`, `isEmpty`, `iterator`), say why the app needed it. If you used something unchanged, say so.]

### What happened to our unknowns?

| Unknown from our scope | Status | What we did |
|---|---|---|
| How to sort a `LinkedChain` by due date or priority | [figured out] | [Get It Done doesn’t sort. It walks the chain once with the iterator and keeps the best task seen so far (earliest due date, ties broken by highest priority).] |
| How to save and load the chain to a file | [Figured out] | [Main loads at startup and saves after every add, update, and check-off, and once more on Quit. readName rejects names containing] |
| How to validate dates | [Figured out] | [	InputValidator parses MM-dd-uuuu with ResolverStyle.STRICT, so impossible dates like 02-30-2026 are rejected. It compares against LocalDate.now(), and for a past date it asks whether to keep it anyway (y/n). Time is parsed strictly as HH:mm.] |

### What doesn't work yet?

- Everything works.

## Next Steps

### Finishing the MVP

- MVP is finished.


### Stretch goals

| Stretch goal | Decision | Owner |
|---|---|---|
| Count summary ("5 tasks: 2 not started, 2 in-progress, 1 high priority") | Committed | [Hasini] |
| Multiple lists (Homework, Groceries, Errands) |  [dropped] | [Both] |
| Sorting by due date, priority, or status | [dropped] | [No one] |
| Overdue flag | [dropped] | [No one] |
| Undo last removal | [dropped] | [No one] |

### New tests we'll need

| Method | Normal case | Bad-input case |
|---|---|---|
| MultipleLists.createList(name) | Creating "Groceries" takes the number of lists from 1 to 2 | A duplicate name (any capitalization), an empty name, or a name containing `\|` is rejected and the number of lists stays the same |
| Add / view with two lists | A task added to "Homework" shows up there and not in "Groceries" | Viewing an empty list prints "There are no tasks in your list yet." and doesn't show another list's tasks |
| MultipleList.selectList() | Picking list 2 of 3 makes it the active list | "0", "99", or "abc" re-prompts and the active list doesn't change |
| SaveLoadTasks save() then load() | Two lists with 2 and 1 tasks come back with the same names, tasks, and order | A list with zero tasks is still there after reload |
| MainTest (full flow) | Create a list, add a task, quit, restart: both are still there | A corrupted line in one list is skipped and counted, and the other lists load normally |

## Attribution

| File | Team member(s) | How (wrote, revised, reviewed, tested, GenAI-assisted, …) |
|---|---|---|
| `Main.java` | Cassidy | [Drafted on my own using the spec, then used GenAI to refine, assist/ find errors and point out what may be missing (or how the code could function more efficiently). I took that input and appplied it to the code, and then finalized it.] |
| `Task.java` | Hasini | [TODO] |
| `LinkedChain.java` | Provided by the course; changes by Hasini | [TODO] |
| `InputValidator.java` | Cassidy | [Drafted on my own using the spec, then used GenAI to refine, assist/ find errors and point out what may be missing (or how the code could function more efficiently). I took that input and appplied it to the code, and then finalized it.] |
| `AddTask.java` | Hasini | [TODO] |
| `ViewList.java` | Cassidy | [Drafted on my own using the spec, then used GenAI to refine, assist/ find errors and point out what may be missing (or how the code could function more efficiently). I took that input and appplied it to the code, and then finalized it.]  |
| `UpdateTask.java` | Hasini | [TODO] |
| `RemoveTask.java` | Hasini | [TODO] |
| `GetItDone.java` | Cassidy | [Drafted on my own using the spec, then used GenAI to refine, assist/ find errors and point out what may be missing (or how the code could function more efficiently). I took that input and appplied it to the code, and then finalized it.]  |
| `SaveLoadTasks.java` | Hasini | [TODO] |
| `CountSummary.java` | Hasini | [TODO] |
| `MainTest.java` | Cassidy | [Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI check if I was testing everything correctly, and implemented the feedback into my code.] |
| `TaskTest.java` | Hasini | [TODO] |
| `LinkedChainTest.java` | Hasini | [TODO] |
| `InputValidatorTest.java` | Cassidy | [Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI check if I was testing everything correctly, and implemented the feedback into my code.] |
| `AddTaskTest.java` | Hasini | [TODO] |
| `ViewListTest.java` | Cassidy | [Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI check if I was testing everything correctly, and implemented the feedback into my code.] |
| `UpdateTaskTest.java` | Hasini | [TODO] |
| `RemoveTaskTest.java` | Hasini | [TODO] |
| `GetItDoneTest.java` | Cassidy | [Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI check if I was testing everything correctly, and implemented the feedback into my code.] |
| `CountSummaryTest.java` | Hasini | [TODO] |
| `SaveLoadTasksTest.java` | Hasini | [TODO] |
| `README.md` | Cassidy & Hasini | [Drafted on our own, then used GenAI to correct and format our respone. GenAI also helped draft our tests for multiple lists.] |

### How we used GenAI

[We used GenAI in everyfile but for different reasons, in our regular files we used AI to look over and review our drafted code and give us feedback that were able to implement. For the testing files, we use GenAI to suggest edge-case tests and check over/ review our testing code. In the regular files, we checked it is output by seeing its result in the terminal as well as walking through the code and interpreting what it was doing on our own making sure it was producing what our idea was. For testing files, we were able to see the failures through vscode to see if the GenAI output was working correctly.]

## Notes

- Tasks are saved after every add, update, and check-off, and once more on Quit.
- Checked-off tasks are deleted, not archived. There's no undo yet.
- Get It Done doesn't sort the list. It walks the chain once and keeps the best task seen so far.