# Check It Off

A Java console to-do list by students, for students. Users can add tasks, check them off, and ask "Get It Done" what to work on next.

## What it does

Check It Off keeps track of day-to-day tasks so students know exactly what needs to be finished and when.

- **Add a task** — enter a name, due date and time, status, and priority.
- **View the list** — see every current task, numbered, with all its details, plus a count summary (e.g. "4 tasks: 2 not started, 1 in-progress, 2 high priority.").
- **Update a task** — change a task's status (not started, in-progress, complete), priority (low, medium, high), name, or due date and time.
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
| JUnit 5 | Unit tests (`lib/junit-platform-console-standalone-1.10.2.jar`) |
| `LinkedChain<T>` | The generic singly linked list we were given (with our changes), which stores every `Task`, `TaskList`, and skipped save-file line |

## Project Structure

```
cs2114-project2-group38/
├── README.md
├── .vscode/
│   └── settings.json        ← tells VS Code where the code and JUnit are
├── lib/
│   └── junit-platform-console-standalone-1.10.2.jar
└── src/
    ├── Main.java
    ├── Task.java
    ├── TaskList.java
    ├── LinkedChain.java
    ├── InputValidator.java
    ├── AddTask.java
    ├── ViewList.java
    ├── UpdateTask.java
    ├── RemoveTask.java
    ├── GetItDone.java
    ├── CountSummary.java
    ├── SaveLoadTasks.java
    ├── MainTest.java
    ├── TaskTest.java
    ├── TaskListTest.java
    ├── LinkedChainTest.java
    ├── InputValidatorTest.java
    ├── AddTaskTest.java
    ├── ViewListTest.java
    ├── UpdateTaskTest.java
    ├── RemoveTaskTest.java
    ├── GetItDoneTest.java
    ├── CountSummaryTest.java
    └── SaveLoadTasksTest.java
```

The tests live in `src` next to the classes they test, so everything is in the same (default) package.

## Getting Started

### Prerequisites

- A JDK installed
- JUnit 5: the jar in `lib/` (already in the repo)
- VS Code with the "Extension Pack for Java", or Eclipse

### Running the program in VS Code

1. Open this folder in VS Code (the folder with `src` and `lib`, not `src` itself)
2. Open `Main.java` and click the ▶ Run button above `main()`

### Running the program in Eclipse

1. File → Open Projects from File System → select this folder
2. Right-click the project → Run As → Java Application
3. Select `Main` as the class to run

### Compiling and running from the command line

```
javac -cp "lib/*" -d bin src/*.java
java -cp bin Main
```

### Running the tests

- **Command line** (works in any editor or Codespace):
  ```
  javac -cp "lib/*" -d bin src/*.java
  java -jar lib/junit-platform-console-standalone-1.10.2.jar -cp bin --scan-classpath
  ```
- **VS Code:** open the Testing panel (flask icon) and click ▶ Run All Tests.
- **Eclipse:** add JUnit 5 (Build Path → Add Libraries → JUnit → JUnit 5), then right-click `src` → Run As → JUnit Test.

The tests use their own files (`test_tasks.txt`), and `MainTest` backs up and restores the real `tasks.txt`, so running them doesn't touch your saved tasks.

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
| `Main` | Starts the program, loads tasks into a default `TaskList`, runs the menu loop, and saves after every change. |
| `Task` | Holds one task's data: name, due date/time, status, priority. |
| `TaskList` | A named list with its own `LinkedChain<Task>`. `Main` stores it in a `LinkedChain<TaskList>`. |
| `LinkedChain<T>` | The provided generic chain, plus a tail reference and an iterator. |
| `Node<T>` | One link in the chain (data + next). Private inner class of `LinkedChain`. |
| `InputValidator` | Reads and checks every typed value. Re-prompts until the input is valid. |
| `AddTask` | Collects a new task's fields through `InputValidator` and adds it to the end of the chain. |
| `RemoveTask` | Lets the user pick a task by name and remove it (check off). |
| `UpdateTask` | Lets the user pick a task and change its status, priority, name, or due date and time. |
| `ViewList` | Prints every task in numbered form, then the count summary. |
| `GetItDone` | Finds and displays the next most important task. |
| `CountSummary` | Stretch goal: counts tasks by status and high priority. |
| `SaveLoadTasks` | Turns the chain into file lines and file lines back into a chain. |

Each menu action is its own class so it can be written and tested separately without merge conflicts in one big file.

### Element types in the LinkedChain

| Element type | Where it's stored | Where it's retrieved |
|---|---|---|
| `Task` | `AddTask.add()` and `SaveLoadTasks.load()` call `addToEnd(new Task(...))` | `ViewList`, `GetItDone`, `RemoveTask`, `CountSummary`, `SaveLoadTasks.save()` (for-each loops); `UpdateTask` and `RemoveTask` (`get()`) |
| `TaskList` | `Main` calls `lists.addToEnd(new TaskList("My Tasks", ...))` | `Main` calls `lists.get(0).getTasks()` |
| `String` | `SaveLoadTasks.load()` calls `skippedLines.addToEnd(line)` for each unreadable line | `SaveLoadTasks.load()` loops over `skippedLines` to print each one |

All three go through the same generic `LinkedChain<T>` class, with no type-specific copies.

### The Generic Contract

```java
public class LinkedChain<T> implements Iterable<T>
```

- Any non-null reference type works. `add(null)` and `addToEnd(null)` throw `IllegalArgumentException`, so the chain never holds nulls.
- `add()` puts new entries at the front; `addToEnd()` puts them at the tail and keeps insertion order. The app always uses `addToEnd()`.
- Duplicates are allowed (two "Laundry" tasks on different days).
- `contains`, `count`, and `remove` use `T.equals()`. `Task` overrides `equals()` and `hashCode()` so this works by value.
- No bounds on `T`, since the chain is unordered and only ever calls `equals()`.

### LinkedChain Methods

| Method | Provided or added | Big-O |
|---|---|---|
| `LinkedChain()` | Provided (now also sets `lastNode`) | O(1) |
| `add(T)` | Provided (inserts at the front; now also sets `lastNode` on an empty chain) | O(1) |
| `addToEnd(T)` | Provided, changed to use `lastNode` | O(1) (was O(n)) |
| `remove()` | Provided (now resets `lastNode` when the chain becomes empty) | O(1) |
| `remove(T)` | Provided (now updates `lastNode` when the last node is removed) | O(n) |
| `clear()` | Provided (now resets `lastNode`) | O(1) |
| `get(int)` | Provided | O(n) |
| `contains(T)` | Provided | O(n) |
| `count(T)` | Provided | O(n) |
| `size()` | Provided | O(1) |
| `isEmpty()` | Provided | O(1) |
| `toArray(T[])` | Provided | O(n) |
| `iterator()` | Added | O(1) to create, O(n) to walk |

Loops over the chain use the iterator (for-each), not `get(i)` in a for loop, which would make the loop O(n²).

### App Method Big-O

| Method | Big-O | Matches spec? |
|---|---|---|
| `AddTask.add` | O(1) | Yes, using `addToEnd()` instead of `add()` |
| `ViewList.show` | O(n) | Yes (one pass to print, one for the summary) |
| `UpdateTask.update` | O(n) | Yes |
| `RemoveTask.remove` | O(n) | Yes |
| `GetItDone.pick` / `show` | O(n) | Yes |
| `CountSummary.summarize` | O(n) | New (stretch goal) |
| `SaveLoadTasks.save` / `load` | O(n) | Yes |
| `Main.main` | O(n) per menu action | Yes |

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
| Date has passed | 01-15-2020 | `InputValidator.readDate()` | Warns "That date has already passed" and asks whether to keep it (y/n) |
| Impossible date | 02-30-2026 | `InputValidator.readDate()` | "Enter a real date as MM-DD-YYYY", re-prompts |
| Bad time | 25:70 | `InputValidator.readTime()` | "Enter a time as HH:MM (00:00 to 23:59)", re-prompts |
| Bad status or priority | "urgent" | `InputValidator.readStatus()` / `readPriority()` | "Your options are: …", re-prompts |
| Empty task name | (Enter) | `InputValidator.readName()` | "The task name can't be empty", re-prompts |
| Name contains `\|` | "Lab \| part 2" | `InputValidator.readName()` | "The task name can't contain \|", re-prompts |
| Bad menu choice | "7" or "abc" | `InputValidator.readMenuChoice()` | "Enter a number from 1 to 6", re-prompts |
| Removing a task that doesn't exist | "Laundry" | `RemoveTask.remove()` | "No task named Laundry found", back to menu |
| Two tasks with the same name | Two "Laundry" tasks | `RemoveTask.remove()` | Lists the matches and asks which one |
| Empty list | Update / Check off / Get It Done / View | `UpdateTask` / `RemoveTask` / `GetItDone` / `ViewList` | "No tasks yet.", "Nothing to do!", or "There are no tasks in your list yet." |
| `null` passed to the chain | `add(null)` | `LinkedChain.add()` / `addToEnd()` | Throws `IllegalArgumentException` |
| Save file missing | First run | `SaveLoadTasks.load()` | Starts with an empty list, no error |
| Corrupted line in save file | Missing fields or a bad date | `SaveLoadTasks.load()` | Skips it, prints "Skipped N unreadable task(s):" and lists each bad line |
| File can't be written | Permission problem | `SaveLoadTasks.save()` | "Could not save tasks.", program keeps running |

## Reflection

### What changed from our specification?

| What changed | The spec said | What we built | Why | Where the change came from |
|---|---|---|---|---|
| `LinkedChain.add()` | `add` appends at the tail in O(1) | The provided `add()` inserts at the front, so the app uses `addToEnd()` | Using `add()` would show tasks in reverse order and flip the order every save/load | GenAI, when reviewing the provided `LinkedChain` |
| `LinkedChain` methods | `get`, `count`, `isEmpty` are "added", plus a new `removeAt` | `get`, `count`, `isEmpty` were already provided; `removeAt` was never added | Nothing in the app calls `removeAt` | GenAI, when comparing the provided class to our spec |
| `UpdateTask` | Change status or priority only | Can also change the name and the due date and time | So a typo or wrong date can be fixed without deleting the task | Own reflection |
| `Task` | Setters for status and priority only | Added `setName` and `setDue` | `UpdateTask` needed them for the new options | Followed from the `UpdateTask` change |
| `TaskList` (new class) | Only in the multiple-lists stretch goal | A name and its own `LinkedChain<Task>`; `Main` stores one default "My Tasks" list in a `LinkedChain<TaskList>` | Reuses `LinkedChain` for a second type and is the first step toward multiple lists | GenAI, when we checked our code against the rubric |
| `CountSummary` (new class) | Stretch goal | Built; prints under the list in `ViewList` | Our committed stretch goal | Group discussion (chose it as our stretch goal) |
| `SaveLoadTasks.load()` | Count skipped lines | Stores skipped lines in a `LinkedChain<String>`, prints the count and each bad line | Reuses `LinkedChain` for a third type and tells the user which lines were bad | GenAI, when we checked our code against the rubric |
| `Main` | Load, then show the menu | Shows the task list right after loading | Matches our scope's "Load & View Tasks" step | GenAI, when reviewing `Main` |
| Messages | "No tasks yet.", "Task name can't be empty" / "can't contain \|" | `ViewList` prints "There are no tasks in your list yet."; `InputValidator` prints "The task name can't …" | Wording choice; the response (message and re-prompt) is the same as the spec | Own reflection |
| Date parsing | Strict `MM-DD-YYYY` | Pattern `MM-dd-uuuu` with `ResolverStyle.STRICT` | With `yyyy`, strict mode rejects every date | GenAI |
| Test plan | `readStatus("In-Progress")` returns "in progress" | Returns "in-progress" | Typo in the spec | Found while writing tests |
| Tests | The spec's test plan | Added `MainTest`, `TaskListTest`, `CountSummaryTest`, `LinkedChainTest` | New classes need tests; `MainTest` checks the whole menu path | GenAI suggested cases; we reviewed and ran them |
| `Task` tests | One normal and one bad-input case per method | No bad-input cases for getters/setters | `Task` doesn't validate; bad values are caught in `InputValidator`, which is tested | Our spec (validation lives in `InputValidator`) |
| Where validation lives | `InputValidator`, `RemoveTask`, `SaveLoadTasks`, `LinkedChain.add` | No change | — | — |

### What changed in the LinkedChain?

We kept the provided `LinkedChain` and made two changes:

- **Tail reference (`lastNode`).** The provided `addToEnd()` walked the whole chain to find the end (O(n)). We added a `lastNode` field so `addToEnd()` links right after it in O(1). The app needs this because it always adds at the end to keep tasks in the order they were added. Every method that changes the chain keeps `lastNode` correct: `add()` sets it on an empty chain, `remove()` and `remove(T)` reset it when the chain becomes empty, `remove(T)` moves it back one node when the last node is removed, and `clear()` resets it.
- **`Iterable<T>` and `iterator()`.** A private inner `ChainIterator` walks from the first node to the last. This lets the app use for-each loops, which are O(n). Using `get(i)` inside a loop would be O(n²).

Everything else we used unchanged: `add()` (still inserts at the front), `remove()`, `remove(T)`'s matching, `get`, `contains`, `count`, `size`, `isEmpty`, `toArray`, `toString`, the null check, and `Node`. `LinkedChainTest` checks the tail in every case where it could break.

### What happened to our unknowns?

| Unknown from our scope | Status | What we did |
|---|---|---|
| How to sort a `LinkedChain` by due date or priority | Figured out | Get It Done doesn't sort. It walks the chain once with the iterator and keeps the best task seen so far (earliest due date, ties broken by highest priority, then earlier time). |
| How to save and load the chain to a file | Figured out | `Main` loads at startup and saves after every add, update, and check-off, and once more on Quit. Each task is one line separated by `\|`, and `readName` rejects names containing `\|` so the file stays readable. A missing file gives an empty list, and corrupted lines are skipped, counted, and printed. |
| How to validate dates | Figured out | `InputValidator` parses `MM-dd-uuuu` with `ResolverStyle.STRICT`, so impossible dates like 02-30-2026 are rejected. It compares against `LocalDate.now()`, and for a past date it asks whether to keep it anyway (y/n). Time is parsed strictly as `HH:mm`. |

### What doesn't work yet?

- All MVP features work, every bad-input case from our scope is handled, and all tests pass.
- `TaskList` holds only one default list ("My Tasks"). There's no menu option yet to create, switch, or save more than one list.
- Some messages are worded differently from our spec (see the table above).

## Next Steps

### Finishing the MVP

The MVP is finished. The remaining work extends `TaskList` into the multiple-lists stretch goal, in this order:

| Order | Remaining item | Owner |
|---|---|---|
| 1 | Add menu options to create a list and switch the active list | Hasini |
| 2 | Validate list names (empty, duplicate in any capitalization, or containing `\|`) in `InputValidator` | Cassidy |
| 3 | Change the save format so each task line records its list name, and update `save()`/`load()` | Hasini |
| 4 | Write the new tests below | Cassidy & Hasini |
| 5 | Update the README and spec for the final | Cassidy & Hasini |

### Stretch goals

| Stretch goal | Decision | Owner |
|---|---|---|
| Count summary ("5 tasks: 2 not started, 2 in-progress, 1 high priority") | Committed — done | Hasini |
| Multiple lists (Homework, Groceries, Errands) | Committed — `TaskList` started | Both |
| Sorting by due date, priority, or status | Dropped | No one |
| Overdue flag | Dropped | No one |
| Undo last removal | Dropped | No one |

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
| `Main.java` | Cassidy | Drafted on my own using the spec, then used GenAI to refine, assist/find errors and point out what may be missing (or how the code could function more efficiently). I took that input and applied it to the code, and then finalized it. GenAI added the default `TaskList`. |
| `Task.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `TaskTest`. |
| `TaskList.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `TaskListTest`. |
| `LinkedChain.java` | Provided by the course; modified by Cassidy and Hasini | Outlined the changes our app needed (a tail reference and an iterator) using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it with `LinkedChainTest`. |
| `InputValidator.java` | Cassidy | Drafted on my own using the spec, then used GenAI to refine, assist/find errors and point out what may be missing (or how the code could function more efficiently). I took that input and applied it to the code, and then finalized it. |
| `AddTask.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `AddTaskTest`. |
| `ViewList.java` | Cassidy | Drafted on my own using the spec, then used GenAI to refine, assist/find errors and point out what may be missing (or how the code could function more efficiently). I took that input and applied it to the code, and then finalized it. GenAI added the count summary line. |
| `UpdateTask.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I also added name and due-date editing. I reviewed the final version and tested it by running the program and `UpdateTaskTest`. |
| `RemoveTask.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `RemoveTaskTest`. |
| `GetItDone.java` | Cassidy | Drafted on my own using the spec, then used GenAI to refine, assist/find errors and point out what may be missing (or how the code could function more efficiently). I took that input and applied it to the code, and then finalized it. |
| `SaveLoadTasks.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `SaveLoadTasksTest`. |
| `CountSummary.java` | Hasini | Drafted an outline of the code using the spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. I reviewed the final version and tested it by running the program and `CountSummaryTest`. |
| `MainTest.java` | Cassidy | Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI to check if I was testing everything correctly, and implemented the feedback into my code. |
| `TaskTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `TaskListTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `LinkedChainTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `InputValidatorTest.java` | Cassidy | Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI to check if I was testing everything correctly, and implemented the feedback into my code. |
| `AddTaskTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `ViewListTest.java` | Cassidy | Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI to check if I was testing everything correctly, and implemented the feedback into my code. |
| `UpdateTaskTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `RemoveTaskTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `GetItDoneTest.java` | Cassidy | Used GenAI to suggest edge-case tests, while also trying to figure out the testing on my own. Used GenAI to check if I was testing everything correctly, and implemented the feedback into my code. |
| `CountSummaryTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `SaveLoadTasksTest.java` | Hasini | Drafted an outline of the tests from our spec's test plan, then used GenAI to help understand, refine, and clean up the tests, suggest edge cases, and compare them against the rubric. I set up JUnit, reviewed each test, and ran them to confirm they pass. |
| `README.md` | Cassidy & Hasini | Drafted on our own, then used GenAI to correct and format our response. GenAI also helped draft our tests for multiple lists and fill in the Reflection and Next Steps tables. |

### How we used GenAI

We used GenAI in every file but for different reasons. In Cassidy's files, we drafted the code ourselves and used GenAI to review it and give feedback that we implemented. In Hasini's files, we drafted an outline of the code from our spec, then used GenAI to help understand, refine, and clean up the code, and to compare it against the rubric. For the testing files, we used GenAI to suggest edge-case tests and check over our testing code. We checked GenAI's output by running the program in the terminal, walking through the code to make sure it did what our spec said, and running the tests in VS Code to see any failures.

## Notes

- Tasks are saved after every add, update, and check-off, and once more on Quit.
- Checked-off tasks are deleted, not archived. There's no undo yet.
- Get It Done doesn't sort the list. It walks the chain once and keeps the best task seen so far.
