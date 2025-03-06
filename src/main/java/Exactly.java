package exactly;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a description and a status.
 */
class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * @param description the task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks the task as done. */
    public void markAsDone() { isDone = true; }

    /** Marks the task as not done. */
    public void unmark() { isDone = false; }

    /**
     * Returns the status icon.
     * @return "X" if done, otherwise " ".
     */
    public String getStatusIcon() { return (isDone ? "X" : " "); }

    @Override
    public String toString() { return "[" + getStatusIcon() + "] " + description; }
}

/**
 * Represents a Todo task.
 */
class Todo extends Task {
    /**
     * Constructs a Todo with the specified description.
     * @param description the todo description.
     */
    public Todo(String description) { super(description); }

    @Override
    public String toString() { return "[T]" + super.toString(); }
}

/**
 * Represents a Deadline task with a due date.
 */
class Deadline extends Task {
    protected LocalDate by;

    /**
     * Constructs a Deadline with the specified description and due date.
     * @param description the deadline description.
     * @param by the due date in yyyy-MM-dd format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by);
    }

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
     * @param description the event description.
     * @param from the start time.
     * @param to the end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

/**
 * Manages a list of tasks.
 */
class TaskList {
    private List<Task> tasks;

    /** Constructs an empty TaskList. */
    public TaskList() { this.tasks = new ArrayList<>(); }

    /**
     * Constructs a TaskList with the given tasks.
     * @param tasks the initial tasks.
     */
    public TaskList(List<Task> tasks) { this.tasks = tasks; }

    /** Adds a task to the list. */
    public void add(Task task) { tasks.add(task); }

    /**
     * Removes the task at the specified index.
     * @param index the index to remove.
     * @return the removed task.
     */
    public Task remove(int index) { return tasks.remove(index); }

    /**
     * Returns the task at the specified index.
     * @param index the index.
     * @return the task.
     */
    public Task get(int index) { return tasks.get(index); }

    /** Returns the number of tasks in the list. */
    public int size() { return tasks.size(); }

    /** Returns the underlying list of tasks. */
    public List<Task> getTasks() { return tasks; }

    /**
     * Returns a string representing the tasks in the list.
     * @return the task list as a string.
     */
    public String listTasks() {
        StringBuilder sb = new StringBuilder();
        if (tasks.isEmpty()) {
            sb.append(" Wow, your task list is empty! Let's get started and add some awesome tasks!");
        } else {
            sb.append(" Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(" " + (i + 1) + "." + tasks.get(i) + "\n");
            }
        }
        return sb.toString();
    }
}

/**
 * Handles loading and saving tasks from/to a file.
 */
class Storage {
    private String filePath;

    /**
     * Constructs a Storage with the specified file path.
     * @param filePath the file path.
     */
    public Storage(String filePath) { this.filePath = filePath; }

    /**
     * Loads tasks from the file.
     * @return a list of tasks.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) { return tasks; }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                try {
                    String type = parts[0];
                    boolean isDone = parts[1].trim().equals("1");
                    if (type.equals("T")) {
                        Todo t = new Todo(parts[2]);
                        if (isDone) t.markAsDone();
                        tasks.add(t);
                    } else if (type.equals("D")) {
                        Deadline d = new Deadline(parts[2], parts[3]);
                        if (isDone) d.markAsDone();
                        tasks.add(d);
                    } else if (type.equals("E")) {
                        Event e = new Event(parts[2], parts[3], parts[4]);
                        if (isDone) e.markAsDone();
                        tasks.add(e);
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
     * Saves the given tasks to the file.
     * @param tasks the list of tasks.
     */
    public void save(List<Task> tasks) {
        try {
            File dir = new File("data");
            if (!dir.exists()) { dir.mkdir(); }
            FileWriter fw = new FileWriter(filePath);
            for (Task t : tasks) {
                if (t instanceof Todo) {
                    fw.write("T | " + (t.isDone ? "1" : "0") + " | " + t.description + "\n");
                } else if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    fw.write("D | " + (t.isDone ? "1" : "0") + " | " + t.description + " | " + d.by.toString() + "\n");
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
    private Scanner scanner;

    /** Constructs a Ui with a new Scanner. */
    public Ui() { scanner = new Scanner(System.in); }

    /** Displays the welcome message. */
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");
    }

    /**
     * Reads a command from the user.
     * @return the command as a string.
     */
    public String readCommand() { return scanner.nextLine().trim(); }

    /** Displays a divider line. */
    public void showLine() { System.out.println("____________________________________________________________"); }

    /** Displays an error message. */
    public void showError(String message) { System.out.println(message); }

    /** Displays a loading error message. */
    public void showLoadingError() { System.out.println("Error loading tasks from file."); }

    /** Closes the scanner. */
    public void close() { scanner.close(); }
}

/**
 * Parses user commands.
 */
class Parser {
    /**
     * Splits the input into command tokens.
     * @param input the user input.
     * @return an array of tokens.
     */
    public static String[] parse(String input) {
        return input.split(" ", 2);
    }
}

/**
 * Main class for the Exactly app.
 */
public class Exactly {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs an Exactly app with the specified file path.
     * @param filePath the path to the storage file.
     */
    public Exactly(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            ui.showLine();
            String[] tokens = Parser.parse(input);
            String command = tokens[0];
            try {
                switch (command) {
                case "bye":
                    System.out.println(" Bye! Keep crushing it and never settle for less!");
                    isExit = true;
                    break;
                case "list":
                    System.out.print(tasks.listTasks());
                    break;
                case "mark": {
                    int markIndex = Integer.parseInt(tokens[1].trim());
                    if (markIndex < 1 || markIndex > tasks.size()) {
                        System.out.println(" Huh? That task number doesn't exist! Check and try again!");
                    } else {
                        tasks.get(markIndex - 1).markAsDone();
                        System.out.println(" Awesome! I've marked this task as done:");
                        System.out.println("   " + tasks.get(markIndex - 1));
                    }
                    break;
                }
                case "unmark": {
                    int unmarkIndex = Integer.parseInt(tokens[1].trim());
                    if (unmarkIndex < 1 || unmarkIndex > tasks.size()) {
                        System.out.println(" That task number is off! Check and try again!");
                    } else {
                        tasks.get(unmarkIndex - 1).unmark();
                        System.out.println(" Got it! I've marked this task as not done yet:");
                        System.out.println("   " + tasks.get(unmarkIndex - 1));
                    }
                    break;
                }
                case "todo": {
                    String todoDesc = tokens.length < 2 ? "" : tokens[1].trim();
                    if (todoDesc.isEmpty()) {
                        System.out.println(" Huh? The description for a todo task cannot be empty! Please give me a proper task!");
                    } else {
                        tasks.add(new Todo(todoDesc));
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks.get(tasks.size() - 1));
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                    }
                    break;
                }
                case "deadline": {
                    String deadlineInput = tokens.length < 2 ? "" : tokens[1].trim();
                    String[] deadlineParts = deadlineInput.split(" /by ");
                    if (deadlineInput.isEmpty() || deadlineParts.length != 2 ||
                            deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
                        System.out.println(" Nope - a deadline command must have a description and a '/by' time! Please use: deadline <description> /by <yyyy-MM-dd>");
                    } else {
                        tasks.add(new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim()));
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks.get(tasks.size() - 1));
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                    }
                    break;
                }
                case "event": {
                    String eventInput = tokens.length < 2 ? "" : tokens[1].trim();
                    String[] partsFrom = eventInput.split(" /from ");
                    if (eventInput.isEmpty() || partsFrom.length != 2 || partsFrom[0].trim().isEmpty()) {
                        System.out.println(" Nope - an event command must include a description and a start time using '/from'! Format: event <description> /from <start> /to <end>");
                    } else {
                        String eventDesc = partsFrom[0].trim();
                        String[] partsTo = partsFrom[1].split(" /to ");
                        if (partsTo.length != 2 || partsTo[0].trim().isEmpty() || partsTo[1].trim().isEmpty()) {
                            System.out.println(" Nope - The event command is missing '/to' or has empty times! Format: event <description> /from <start> /to <end>");
                        } else {
                            tasks.add(new Event(eventDesc, partsTo[0].trim(), partsTo[1].trim()));
                            System.out.println(" Got it. I've added this task:");
                            System.out.println("   " + tasks.get(tasks.size() - 1));
                            System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                        }
                    }
                    break;
                }
                case "delete": {
                    int deleteIndex = Integer.parseInt(tokens[1].trim());
                    if (deleteIndex < 1 || deleteIndex > tasks.size()) {
                        System.out.println(" Whoops! That task number doesn't exist! Check and try again!");
                    } else {
                        Task removed = tasks.remove(deleteIndex - 1);
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removed);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    }
                    break;
                }
                case "find": {
                    String keyword = tokens.length < 2 ? "" : tokens[1].trim();
                    if (keyword.isEmpty()) {
                        System.out.println(" Please provide a keyword to search for.");
                    } else {
                        List<Task> matchingTasks = new ArrayList<>();
                        for (Task t : tasks.getTasks()) {
                            if (t.description.contains(keyword)) {
                                matchingTasks.add(t);
                            }
                        }
                        if (matchingTasks.isEmpty()) {
                            System.out.println(" No matching tasks found!");
                        } else {
                            System.out.println(" Here are the matching tasks in your list:");
                            for (int i = 0; i < matchingTasks.size(); i++) {
                                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
                            }
                        }
                    }
                    break;
                }
                default:
                    System.out.println(" Huh? I don't understand what you said!");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
            }
            ui.showLine();
            storage.save(tasks.getTasks());
        }
        ui.close();
    }

    /**
     * The main entry point for the Exactly app.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        new Exactly("data/exactly.txt").run();
    }
}
