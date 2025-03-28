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
     *
     * @param description the task description.
     */
    public Task(String description) {
        // Assert that description is not null or empty.
        assert description != null && !description.isEmpty() : "Task description must not be null or empty";
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
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
        // Assert that the due date string is not null or empty.
        assert by != null && !by.isEmpty() : "Deadline date must not be null or empty";
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
     *
     * @param description the event description.
     * @param from        the start time.
     * @param to          the end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        // Assert that both start and end times are provided.
        assert from != null && !from.isEmpty() : "Event start time must not be null or empty";
        assert to != null && !to.isEmpty() : "Event end time must not be null or empty";
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
    private final List<Task> tasks;

    /** Constructs an empty TaskList. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given tasks.
     *
     * @param tasks the initial tasks.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Initial tasks list must not be null";
        this.tasks = tasks;
    }

    /** Adds a task to the list. */
    public void add(Task task) {
        assert task != null : "Task to add must not be null";
        tasks.add(task);
    }

    /**
     * Overloaded add method that accepts multiple tasks using varargs.
     * @param tasksToAdd the tasks to add.
     */
    public void add(Task... tasksToAdd) {
        // Assert that tasksToAdd array is not null.
        assert tasksToAdd != null : "Tasks array must not be null";
        for (Task t : tasksToAdd) {
            // Each task should not be null.
            assert t != null : "Individual task must not be null";
            tasks.add(t);
        }
    }
        tasks.add(task);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index the index to remove.
     * @return the removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the index.
     * @return the task.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns the underlying list of tasks. */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns a string representing the tasks in the list.
     *
     * @return the task list as a string.
     */
    public String listTasks() {
        StringBuilder sb = new StringBuilder();
        if (tasks.isEmpty()) {
            sb.append(" Wow, your task list is empty! Let's get started and add some awesome tasks!");
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
     * Constructs a Storage with the specified file path.
     *
     * @param filePath the file path.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be null or empty";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file.
     *
     * @return a list of tasks.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return tasks;
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                try {
                    String type = parts[0];
                    boolean isDone = parts[1].trim().equals("1");
                    switch (type) {
                    case "T":
                        Todo t = new Todo(parts[2]);
                        if (isDone) {
                            t.markAsDone();
                        }
                        tasks.add(t);
                        break;
                    case "D":
                        Deadline d = new Deadline(parts[2], parts[3]);
                        if (isDone) {
                            d.markAsDone();
                        }
                        tasks.add(d);
                        break;
                    case "E":
                        Event e = new Event(parts[2], parts[3], parts[4]);
                        if (isDone) {
                            e.markAsDone();
                        }
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
     * Saves the given tasks to the file.
     *
     * @param tasks the list of tasks.
     */
    public void save(List<Task> tasks) {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileWriter fw = new FileWriter(filePath);
            for (Task t : tasks) {
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

    /** Constructs a Ui with a new Scanner. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the welcome message. */
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");
    }

    /**
     * Reads a command from the user.
     *
     * @return the command as a string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Displays a divider line. */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /** Displays an error message. */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays a loading error message. */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file.");
    }

    /** Closes the scanner. */
    public void close() {
        scanner.close();
    }
}

/**
 * Parses user commands.
 */
class Parser {
    /**
     * Splits the input into command tokens.
     *
     * @param input the user input.
     * @return an array of tokens.
     */
    public static String[] parse(String input) {
        assert input != null : "Input must not be null";
        return input.split(" ", 2);
    }
}

/**
 * Main class for the Exactly app.
 *
 * This version has been refactored to improve code quality. It now includes a
 * getWelcomeMessage() method and a getResponse(String input) method that processes
 * a single command and returns the chatbot's reply. These changes facilitate the
 * integration of a JavaFX GUI while maintaining backward compatibility with the CLI.
 */
public class Exactly {
    private final Storage storage;
    private final TaskList tasks;
    // Package-visible for access from GUI components.
    Ui ui;

    /**
     * Constructs an Exactly app with the specified file path.
     *
     * @param filePath the path to the storage file.
     */
    public Exactly(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be null or empty";
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Returns the welcome message.
     *
     * @return the welcome message as a String.
     */
    public String getWelcomeMessage() {
        return "____________________________________________________________\n"
                + " Hey! I'm Exactly and I'm pumped to help you out! What do you need?\n"
                + "____________________________________________________________\n";
    }

    /**
     * Processes a single user command and returns the chatbot's response.
     *
     * @param input the user's command.
     * @return the response as a String.
     */
    public String getResponse(String input) {
        // Assert input is not null.
        assert input != null : "Input must not be null";
        StringBuilder output = new StringBuilder();
        output.append("____________________________________________________________\n");
        String[] tokens = Parser.parse(input);
        // Assert that at least one token is present.
        assert tokens.length > 0 : "Parser should return at least one token";
        String command = tokens[0];
        try {
            switch (command) {
            case "bye":
                output.append(" Bye! Keep crushing it and never settle for less!\n");
                break;
            case "list":
                output.append(tasks.listTasks());
                break;
            case "mark": {
                // Assert that a parameter exists for the mark command.
                assert tokens.length > 1 : "Mark command requires a task number";
                int markIndex = Integer.parseInt(tokens[1].trim());
                if (markIndex < 1 || markIndex > tasks.size()) {
                    output.append(" Huh? That task number doesn't exist! Check and try again!\n");
                } else {
                    tasks.get(markIndex - 1).markAsDone();
                    output.append(" Awesome! I've marked this task as done:\n")
                            .append("   ").append(tasks.get(markIndex - 1)).append("\n");
                }
                break;
            }
            case "unmark": {
                // Assert that a parameter exists for the unmark command.
                assert tokens.length > 1 : "Unmark command requires a task number";
                int unmarkIndex = Integer.parseInt(tokens[1].trim());
                if (unmarkIndex < 1 || unmarkIndex > tasks.size()) {
                    output.append(" That task number is off! Check and try again!\n");
                } else {
                    tasks.get(unmarkIndex - 1).unmark();
                    output.append(" Got it! I've marked this task as not done yet:\n")
                            .append("   ").append(tasks.get(unmarkIndex - 1)).append("\n");
                }
                break;
            }
            case "todo": {
                String todoDesc = tokens.length < 2 ? "" : tokens[1].trim();
                if (todoDesc.isEmpty()) {
                    output.append(" Huh? The description for a todo task cannot be empty! Please give me a proper task!\n");
                } else {
                    tasks.add(new Todo(todoDesc));
                    output.append(" Got it. I've added this task:\n")
                            .append("   ").append(tasks.get(tasks.size() - 1)).append("\n")
                            .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                }
                break;
            }
            case "deadline": {
                String deadlineInput = tokens.length < 2 ? "" : tokens[1].trim();
                String[] deadlineParts = deadlineInput.split(" /by ");
                if (deadlineInput.isEmpty() || deadlineParts.length != 2 ||
                        deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
                    output.append(" Nope - a deadline command must have a description and a '/by' time! Please use: deadline <description> /by <yyyy-MM-dd>\n");
                } else {
                    tasks.add(new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim()));
                    output.append(" Got it. I've added this task:\n")
                            .append("   ").append(tasks.get(tasks.size() - 1)).append("\n")
                            .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                }
                break;
            }
            case "event": {
                String eventInput = tokens.length < 2 ? "" : tokens[1].trim();
                String[] partsFrom = eventInput.split(" /from ");
                if (eventInput.isEmpty() || partsFrom.length != 2 || partsFrom[0].trim().isEmpty()) {
                    output.append(" Nope - an event command must include a description and a start time using '/from'! Format: event <description> /from <start> /to <end>\n");
                } else {
                    String eventDesc = partsFrom[0].trim();
                    String[] partsTo = partsFrom[1].split(" /to ");
                    if (partsTo.length != 2 || partsTo[0].trim().isEmpty() || partsTo[1].trim().isEmpty()) {
                        output.append(" Nope - The event command is missing '/to' or has empty times! Format: event <description> /from <start> /to <end>\n");
                    } else {
                        tasks.add(new Event(eventDesc, partsTo[0].trim(), partsTo[1].trim()));
                        output.append(" Got it. I've added this task:\n")
                                .append("   ").append(tasks.get(tasks.size() - 1)).append("\n")
                                .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                    }
                }
                break;
            }
            case "delete": {
                // Assert that a parameter exists for the delete command.
                assert tokens.length > 1 : "Delete command requires a task number";
                int deleteIndex = Integer.parseInt(tokens[1].trim());
                if (deleteIndex < 1 || deleteIndex > tasks.size()) {
                    output.append(" Whoops! That task number doesn't exist! Check and try again!\n");
                } else {
                    Task removed = tasks.remove(deleteIndex - 1);
                    output.append(" Noted. I've removed this task:\n")
                            .append("   ").append(removed).append("\n")
                            .append(" Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                }
                break;
            }
            case "find": {
                String keyword = tokens.length < 2 ? "" : tokens[1].trim();
                if (keyword.isEmpty()) {
                    output.append(" Please provide a keyword to search for.\n");
                } else {
                    List<Task> matchingTasks = new ArrayList<>();
                    for (Task t : tasks.getTasks()) {
                        if (t.description.contains(keyword)) {
                            matchingTasks.add(t);
                        }
                    }
                    if (matchingTasks.isEmpty()) {
                        output.append(" No matching tasks found!\n");
                    } else {
                        output.append(" Here are the matching tasks in your list:\n");
                        for (int i = 0; i < matchingTasks.size(); i++) {
                            output.append(" ").append(i + 1).append(". ").append(matchingTasks.get(i)).append("\n");
                        }
                    }
                }
                break;
            }
            default:
                output.append(" Huh? I don't understand what you said!\n");
            }
        } catch (Exception e) {
            output.append(" Error: ").append(e.getMessage()).append("\n");
        }
        output.append("____________________________________________________________\n");
        storage.save(tasks.getTasks());
        return output.toString();
    }

    /**
     * Runs the CLI version of Exactly.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            ui.showLine();
            System.out.print(getResponse(input));
            ui.showLine();
            if (input.trim().equals("bye")) {
                isExit = true;
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Exactly("data/exactly.txt").run();
    }
}
