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

Everything is stored in one generic class, `LinkedChain<T>`. The rest of the program uses the chain but never reaches into its nodes.

## Tech Stack

| Piece | What it is |
|---|---|
| Java | Language |
| `java.time` (`LocalDate`, `LocalTime`, `LocalDateTime`) | Strict date/time parsing and comparison |
| Java file I/O | Saving and loading tasks to `tasks.txt` |
| `LinkedChain<T>` | Our generic singly linked list (head + tail) that holds every task |

## Getting Started

### Prerequisites

- A JDK installed
- Eclipse or VS Code (both work — see below)

### Running it in Eclipse

1. File → Open Projects from File System → select this folder
2. Right-click the project → Run As → Java Application
3. Select `Main` as the class to run

### Running it in VS Code

1. Install the "Extension Pack for Java"
2. Open this folder in VS Code
3. Open `Main.java` and click the ▶ Run button above `main()`

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

### Stretch Goal

- **Count summary** — show something like "5 tasks: 2 not started, 2 in-progress, 1 high priority."

### Not in this version (future ideas)

- **Multiple lists** (Homework, Groceries, Errands). `LinkedChain` is generic, so it could hold `TaskList` objects later without changes.
- **Sorting** by due date, priority, or status. Not needed for the MVP, since Get It Done finds the best task in one pass.
- **Overdue flag** that compares due dates to today and marks late tasks.
- **Undo last removal** by keeping the most recently removed task so it can be restored.

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

| Bad input | Example | What happens |
|---|---|---|
| Date has passed | 01-15-2020 | Warns "That date has already passed" and asks whether to keep it |
| Impossible date | 02-30-2026 | "Enter a real date as MM-DD-YYYY", re-prompts |
| Bad time | 25:70 | "Enter a time as HH:MM (00:00 to 23:59)", re-prompts |
| Bad status or priority | "urgent" | Shows the valid options, re-prompts |
| Empty task name | (Enter) | "Task name can't be empty", re-prompts |
| Name contains `\|` | "Lab \| part 2" | "Task name can't contain \|", re-prompts |
| Bad menu choice | "7" or "abc" | Shows the valid range, re-prompts |
| Removing a task that doesn't exist | "Laundry" | "No task named Laundry found", back to menu |
| Two tasks with the same name | Two "Laundry" tasks | Lists the matches and asks which one |
| Empty list | Update / Check off / Get It Done | "No tasks yet" or "Nothing to do!" |
| Save file missing | First run | Starts with an empty list, no error |
| Corrupted line in save file | Missing fields | Skips it and prints "Skipped N unreadable task(s)." |
| File can't be written | Permission problem | "Could not save tasks", program keeps running |

## Notes

- Tasks are saved after every add, update, and check-off, and once more on Quit.
- Checked-off tasks are deleted, not archived. There's no undo yet.
- Get It Done doesn't sort the list. It walks the chain once and keeps the best task seen so far.
