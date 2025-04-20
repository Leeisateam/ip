package exactly;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * Represents a task with a description and a status.
 */
class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     *
     * @param description the task description.
     */
    public Task(String description) {
        // Assert that description is not null or empty.
        assert description != null && !description.isEmpty(): "Task description must not be null or empty";
        this.description = description;
        this.isDone = false;
    }

    /** Marks the task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks the task as not done. */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns the status icon.
     *
     * @return "X" if done, otherwise " ".
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }
    /**
     * Return a string representation of the Task.
     *
     * @return formatted string "[<status>] <description>"
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
    /**
     * Compare this Task to another for equality based on type and content.
     *
     * @param obj the object to compare against
     * @return true if both tasks are of the same subclass and have equal data
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        Task other = (Task) obj;
        if (!getClass().equals(other.getClass())) return false;
        if (!description.equals(other.description)) return false;
        if (this instanceof Deadline) {
            return ((Deadline) this).by.equals(((Deadline) other).by);
        }
        if (this instanceof Event) {
            Event e1 = (Event) this, e2 = (Event) other;
            return e1.from.equals(e2.from) && e1.to.equals(e2.to);
        }
        return true; // for Todo
    }
    /**
     * Compute a hash code consistent with equals.
     *
     * @return hash code for this Task
     */
    @Override
    public int hashCode() {
        if (this instanceof Deadline) {
            return Objects.hash(getClass(), description, ((Deadline) this).by);
        }
        if (this instanceof Event) {
            Event e = (Event) this;
            return Objects.hash(getClass(), description, e.from, e.to);
        }
        return Objects.hash(getClass(), description);
    }
}

/**
 * Represents a Todo task.
 */
class Todo extends Task {

    /**
     * Constructs a Todo with the specified description.
     *
     * @param description the todo description.
     */
    public Todo(String description) {
        super(description);
    }
    /**
     * Return a string representation of the Todo task.
     *
     * @return formatted string "[T][<status>] <description>"
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

/**
 * Represents a Deadline task with a due date.
 */
class Deadline extends Task {
    protected LocalDate by;

    /**
     * Constructs a Deadline with the specified description and due date.
     *
     * @param description the deadline description.
     * @param by          the due date in yyyy-MM-dd format.
     */
    public Deadline(String description, String by) {
        super(description);
        assert by != null && !by.isEmpty(): "Deadline date must not be null or empty";
        this.by = LocalDate.parse(by);
    }

    /**
     * Return a string representation of the Deadline task.
     *
     * @return formatted string "[D][<status>] <description> (by: MMM dd yyyy)"
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }
}

/**
 * Represents an Event task with start and end times.
 */
class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs an Event with the specified description, start, and end times.
     *
     * @param description the event description.
     * @param from        the start time.
     * @param to          the end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        assert from != null && !from.isEmpty(): "Event start time must not be null or empty";
        assert to != null && !to.isEmpty(): "Event end time must not be null or empty";
        this.from = from;
        this.to = to;
    }
    /**
     * Return a string representation of the Event task.
     *
     * @return formatted string "[E][<status>] <description> (from: <from> to: <to>)"
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

/**
 * Manages a list of tasks.
 */
class TaskList {
    private final List < Task > tasks;

    /** Constructs an empty TaskList. */
    public TaskList() {
        this.tasks = new ArrayList < > ();
    }

    /** Constructs a TaskList with the given tasks. */
    public TaskList(List < Task > tasks) {
        assert tasks != null: "Initial tasks list must not be null";
        this.tasks = tasks;
    }

    /**
     * Add a single task to this TaskList.
     *
     * @param task the Task to add; must not be null
     */
    public void add(Task task) {
        assert task != null: "Task to add must not be null";
        tasks.add(task);
    }

    /**
     * Add multiple tasks to this TaskList.
     *
     * @param tasksToAdd one or more Task objects; none may be null
     */
    public void add(Task...tasksToAdd) {
        assert tasksToAdd != null: "Tasks array must not be null";
        for (Task t: tasksToAdd) {
            assert t != null: "Individual task must not be null";
            tasks.add(t);
        }
    }
    /**
     * Remove and return the task at the given index.
     *
     * @param index zero-based position of the task to remove
     * @return the Task that was removed
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }
    /**
     * Retrieve the task at the given zero‑based index.
     *
     * @param index position of the task to retrieve
     * @return the Task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }
    /**
     * Report how many tasks are currently in this list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Expose the live list of tasks.
     *
     * @return a modifiable List of all Task objects
     */
    public List < Task > getTasks() {
        return tasks;
    }

    /**
     * Produce a user‑friendly listing of all tasks.
     *
     * @return formatted string of numbered tasks or empty‑list message
     */
    public String listTasks() {
        StringBuilder sb = new StringBuilder();
        if (tasks.isEmpty()) {
            sb.append(" Wow, your task list is empty! Let's get started and add some awesome tasks!\n");
        } else {
            sb.append(" Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
        }
        return sb.toString();
    }
}

/**
 * Handles loading and saving tasks from/to a file.
 */
class Storage {
    private final String filePath;

    /**
     * Create a Storage handler for the given file path.
     *
     * @param filePath path to the data file where tasks are persisted
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty(): "File path must not be null or empty";
        this.filePath = filePath;
    }

    /**
     * Load all tasks from the storage file.
     *
     * @return a List of Tasks (empty if file not found or empty)
     */
    public List < Task > load() {
        List < Task > tasks = new ArrayList < > ();
        try {
            File file = new File(filePath);
            if (!file.exists()) return tasks;
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" | ");
                try {
                    String type = parts[0];
                    boolean isDone = parts[1].trim().equals("1");
                    switch (type) {
                    case "T":
                        Todo t = new Todo(parts[2]);
                        if (isDone) t.markAsDone();
                        tasks.add(t);
                        break;
                    case "D":
                        Deadline d = new Deadline(parts[2], parts[3]);
                        if (isDone) d.markAsDone();
                        tasks.add(d);
                        break;
                    case "E":
                        Event e = new Event(parts[2], parts[3], parts[4]);
                        if (isDone) e.markAsDone();
                        tasks.add(e);
                        break;
                    default:
                        System.out.println("Warning: Unknown task type in file: " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping invalid task entry in file: " + line);
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }
    /**
     * Save the given tasks to the storage file.
     *
     * @param tasks the list of tasks to persist
     */
    public void save(List < Task > tasks) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
            FileWriter fw = new FileWriter(filePath);
            for (Task t: tasks) {
                if (t instanceof Todo) {
                    fw.write("T | " + (t.isDone ? "1" : "0") + " | " + t.description + "\n");
                } else if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    fw.write("D | " + (t.isDone ? "1" : "0") + " | " + t.description + " | " + d.by + "\n");
                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    fw.write("E | " + (t.isDone ? "1" : "0") + " | " + t.description + " | " + e.from + " | " + e.to + "\n");
                }
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}

/**
 * Manages user interactions.
 */
class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }
    /**
     * Print the initial greeting banner to the console.
     */
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");
    }
    /**
     * Read a line of user input from the console.
     *
     * @return the trimmed input string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }
    /**
     * Print a divider line to the console.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }
    /**
     * Display an error message in the console.
     *
     * @param message the error text to show
     */
    public void showError(String message) {
        System.out.println(message);
    }
    /**
     * Display a loading‑error notice when storage fails.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file.");
    }
    /**
     * Close any resources held by the UI (e.g., the Scanner).
     */
    public void close() {
        scanner.close();
    }
}

/**
 * Parses user commands.
 */
class Parser {
    /**
     * Split raw user input into command and arguments.
     *
     * @param input the full input string
     * @return an array where element 0 is the command, element 1 (optional) are the args
     */
    public static String[] parse(String input) {
        assert input != null: "Input must not be null";
        return input.split(" ", 2);
    }
}

/**
 * Main class for the Exactly chatbot application.
 *
 * Manages the UI, command parsing, task list, and storage.
 */
public class Exactly {
    private final Storage storage;
    private final TaskList tasks;
    Ui ui;
    /**
     * Initialize Exactly with the given storage file path and load existing tasks.
     *
     * @param filePath path to the file used for loading and saving tasks
     */
    public Exactly(String filePath) {
        assert filePath != null && !filePath.isEmpty(): "File path must not be null or empty";
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }
    /**
     * Return the welcome banner shown when the application starts.
     *
     * @return the welcome message including dividers and prompt
     */
    public String getWelcomeMessage() {
        return "____________________________________________________________\n"
                + " Hey! I'm Exactly and I'm pumped to help you out! What do you need?\n"
                + "____________________________________________________________\n";
    }
    /**
     * Processes a single user input and returns Exactly’s reply.
     *
     * @param input the raw command entered by the user
     * @return the full formatted response including dividers
     */
    public String getResponse(String input) {
        assert input != null: "Input must not be null";
        StringBuilder output = new StringBuilder();
        appendHeader(output);

        String[] tokens = Parser.parse(input);
        assert tokens.length > 0: "Parser should return at least one token";
        String command = tokens[0];
        try {
            switch (command) {
            case "bye":
                processBye(output);
                break;
            case "list":
                processList(output);
                break;
            case "mark":
                processMark(tokens, output);
                break;
            case "unmark":
                processUnmark(tokens, output);
                break;
            case "todo":
                processTodo(tokens, output);
                break;
            case "deadline":
                processDeadline(tokens, output);
                break;
            case "event":
                processEvent(tokens, output);
                break;
            case "delete":
                processDelete(tokens, output);
                break;
            case "find":
                processFind(tokens, output);
                break;
            default:
                output.append(" Huh? I don't understand what you said!\n");
            }
        } catch (Exception e) {
            output.append(" Error: ").append(e.getMessage()).append("\n");
        }
        appendFooter(output);
        storage.save(tasks.getTasks());
        return output.toString();
    }

    /**
     * Append the standard header divider to the response.
     *
     * @param sb the StringBuilder to append to
     */
    private void appendHeader(StringBuilder sb) {
        sb.append("____________________________________________________________\n");
    }
    /**
     * Append the standard footer divider to the response.
     *
     * @param sb the StringBuilder to append to
     */
    private void appendFooter(StringBuilder sb) {
        sb.append("____________________________________________________________\n");
    }

    /**
     * Handle the "bye" command by appending the exit message.
     *
     * @param sb the StringBuilder to append to
     */
    private void processBye(StringBuilder sb) {
        sb.append(" Bye! Keep crushing it and never settle for less!\n");
    }
    /**
     * Handle the "list" command by appending the current task list.
     *
     * @param sb the StringBuilder to append to
     */
    private void processList(StringBuilder sb) {
        sb.append(tasks.listTasks());
    }
    /**
     * Handle the "mark" command to mark a task as done.
     *
     * @param tokens the command tokens (["mark", "<index>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processMark(String[] tokens, StringBuilder sb) {
        assert tokens.length > 1: "Mark command requires a task number";
        int idx = Integer.parseInt(tokens[1].trim());
        if (idx < 1 || idx > tasks.size()) {
            sb.append(" Huh? That task number doesn't exist! Check and try again!\n");
        } else {
            tasks.get(idx - 1).markAsDone();
            sb.append(" Awesome! I've marked this task as done:\n    ")
                    .append(tasks.get(idx - 1)).append("\n");
        }
    }
    /**
     * Handle the "unmark" command to mark a task as not done.
     *
     * @param tokens the command tokens (["unmark", "<index>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processUnmark(String[] tokens, StringBuilder sb) {
        assert tokens.length > 1: "Unmark command requires a task number";
        int idx = Integer.parseInt(tokens[1].trim());
        if (idx < 1 || idx > tasks.size()) {
            sb.append(" That task number is off! Check and try again!\n");
        } else {
            tasks.get(idx - 1).unmark();
            sb.append(" Got it! I've marked this task as not done yet:\n    ")
                    .append(tasks.get(idx - 1)).append("\n");
        }
    }
    /**
     * Handle the "todo" command by creating and adding a Todo task.
     *
     * @param tokens the command tokens (["todo", "<description>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processTodo(String[] tokens, StringBuilder sb) {
        String desc = tokens.length < 2 ? "" : tokens[1].trim();
        if (desc.isEmpty()) {
            sb.append(" Huh? The description for a todo task cannot be empty! Please give me a proper task!\n");
        } else {
            handleAdd(new Todo(desc), sb);
        }
    }

    /**
     * Handle the "deadline" command by creating and adding a Deadline task.
     *
     * @param tokens the command tokens (["deadline", "<desc> /by <date>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processDeadline(String[] tokens, StringBuilder sb) {
        String inputStr = tokens.length < 2 ? "" : tokens[1].trim();
        String[] parts = inputStr.split(" /by ");
        if (inputStr.isEmpty() || parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            sb.append(" Nope - a deadline command must have a description and a '/by' time! Please use: deadline <description> /by <yyyy-MM-dd>\n");
        } else {
            handleAdd(new Deadline(parts[0].trim(), parts[1].trim()), sb);
        }
    }

    /**
     * Handle the "event" command by creating and adding an Event task.
     *
     * @param tokens the command tokens (["event", "<desc> /from <start> /to <end>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processEvent(String[] tokens, StringBuilder sb) {
        String inputStr = tokens.length < 2 ? "" : tokens[1].trim();
        String[] fromParts = inputStr.split(" /from ");
        if (inputStr.isEmpty() || fromParts.length != 2 || fromParts[0].trim().isEmpty()) {
            sb.append(" Nope - an event command must include a description and a start time using '/from'! Format: event <description> /from <start> /to <end>\n");
        } else {
            String descEvt = fromParts[0].trim();
            String[] toParts = fromParts[1].split(" /to ");
            if (toParts.length != 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                sb.append(" Nope - The event command is missing '/to' or has empty times! Format: event <description> /from <start> /to <end>\n");
            } else {
                handleAdd(new Event(descEvt, toParts[0].trim(), toParts[1].trim()), sb);
            }
        }
    }

    /**
     * Handle the "delete" command by removing a task.
     *
     * @param tokens the command tokens (["delete", "<index>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processDelete(String[] tokens, StringBuilder sb) {
        assert tokens.length > 1: "Delete command requires a task number";
        int idx = Integer.parseInt(tokens[1].trim());
        if (idx < 1 || idx > tasks.size()) {
            sb.append(" Whoops! That task number doesn't exist! Check and try again!\n");
        } else {
            Task removed = tasks.remove(idx - 1);
            sb.append(" Noted. I've removed this task:\n    ")
                    .append(removed).append("\n")
                    .append(" Now you have ").append(tasks.size()).append(" tasks in the list.\n");
        }
    }

    /**
     * Handle the "find" command by searching for tasks matching a keyword.
     *
     * @param tokens the command tokens (["find", "<keyword>"])
     * @param sb     the StringBuilder to append the result to
     */
    private void processFind(String[] tokens, StringBuilder sb) {
        String kw = tokens.length < 2 ? "" : tokens[1].trim();
        if (kw.isEmpty()) {
            sb.append(" Please provide a keyword to search for.\n");
        } else {
            List < Task > match = tasks.getTasks().stream()
                    .filter(t -> t.description.contains(kw))
                    .collect(Collectors.toList());
            if (match.isEmpty()) {
                sb.append(" No matching tasks found!\n");
            } else {
                sb.append(" Here are the matching tasks in your list:\n");
                for (int i = 0; i < match.size(); i++) {
                    sb.append(" ").append(i + 1).append(". ").append(match.get(i)).append("\n");
                }
            }
        }
    }

    /**
     * Add a new task if it is not a duplicate, and append feedback to the response.
     *
     * @param newTask the Task to add
     * @param sb      the StringBuilder to append user feedback to
     */
    private void handleAdd(Task newTask, StringBuilder sb) {
        if (tasks.getTasks().contains(newTask)) {
            sb.append(" Whoa! You already have this task! Won't add duplicate.\n");
        } else {
            tasks.add(newTask);
            sb.append(" Got it. I've added this task:\n    ")
                    .append(tasks.get(tasks.size() - 1)).append("\n")
                    .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
        }
    }

    /**
     * Start the main interaction loop:
     * read commands from stdin, print responses to stdout,
     * and exit when the user types "bye".
     */
    public void run() {
        ui.showWelcome();
        boolean exit = false;
        while (!exit) {
            String input = ui.readCommand();
            ui.showLine();
            System.out.print(getResponse(input));
            ui.showLine();
            if ("bye".equals(input.trim())) exit = true;
        }
        ui.close();
    }

    /**
     * The application's entry point.
     *
     * @param args expects a single argument: the storage file path
     */
    public static void main(String[] args) {
        new Exactly("data/exactly.txt").run();
    }
}