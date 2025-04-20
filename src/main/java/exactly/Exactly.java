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

    /** Constructs a TaskList with the given tasks. */
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
        assert tasksToAdd != null : "Tasks array must not be null";
        for (Task t : tasksToAdd) {
            assert t != null : "Individual task must not be null";
            tasks.add(t);
        }
    }

    public Task remove(int index) { return tasks.remove(index); }
    public Task get(int index)     { return tasks.get(index);  }
    public int size()              { return tasks.size();      }
    public List<Task> getTasks()   { return tasks;            }

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

    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be null or empty";
        this.filePath = filePath;
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
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

    public void save(List<Task> tasks) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
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

    public Ui() { scanner = new Scanner(System.in); }
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");
    }
    public String readCommand() { return scanner.nextLine().trim(); }
    public void showLine()    { System.out.println("____________________________________________________________"); }
    public void showError(String message) { System.out.println(message); }
    public void showLoadingError()       { System.out.println("Error loading tasks from file."); }
    public void close() { scanner.close(); }
}

/**
 * Parses user commands.
 */
class Parser {
    public static String[] parse(String input) {
        assert input != null : "Input must not be null";
        return input.split(" ", 2);
    }
}

/**
 * Main class for the Exactly app.
 */
public class Exactly {
    private final Storage storage;
    private final TaskList tasks;
    Ui ui;

    public Exactly(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path must not be null or empty";
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    public String getWelcomeMessage() {
        return "____________________________________________________________\n"
                + " Hey! I'm Exactly and I'm pumped to help you out! What do you need?\n"
                + "____________________________________________________________\n";
    }

    public String getResponse(String input) {
        assert input != null : "Input must not be null";
        StringBuilder output = new StringBuilder();
        output.append("____________________________________________________________\n");
        String[] tokens = Parser.parse(input);
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
                assert tokens.length > 1 : "Mark command requires a task number";
                int idx = Integer.parseInt(tokens[1].trim());
                if (idx < 1 || idx > tasks.size()) {
                    output.append(" Huh? That task number doesn't exist! Check and try again!\n");
                } else {
                    tasks.get(idx - 1).markAsDone();
                    output.append(" Awesome! I've marked this task as done:\n    ")
                            .append(tasks.get(idx - 1)).append("\n");
                }
                break;
            }
            case "unmark": {
                assert tokens.length > 1 : "Unmark command requires a task number";
                int idx = Integer.parseInt(tokens[1].trim());
                if (idx < 1 || idx > tasks.size()) {
                    output.append(" That task number is off! Check and try again!\n");
                } else {
                    tasks.get(idx - 1).unmark();
                    output.append(" Got it! I've marked this task as not done yet:\n    ")
                            .append(tasks.get(idx - 1)).append("\n");
                }
                break;
            }
            case "todo": {
                String desc = tokens.length < 2 ? "" : tokens[1].trim();
                if (desc.isEmpty()) {
                    output.append(" Huh? The description for a todo task cannot be empty! Please give me a proper task!\n");
                } else {
                    Todo newTodo = new Todo(desc);
                    if (tasks.getTasks().contains(newTodo)) {
                        output.append(" Whoa! You already have this task! Won't add duplicate.\n");
                    } else {
                        tasks.add(newTodo);
                        output.append(" Got it. I've added this task:\n    ")
                                .append(tasks.get(tasks.size()-1)).append("\n")
                                .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                    }
                }
                break;
            }
            case "deadline": {
                String inputStr = tokens.length < 2 ? "" : tokens[1].trim();
                String[] parts = inputStr.split(" /by ");
                if (inputStr.isEmpty() || parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    output.append(" Nope - a deadline command must have a description and a '/by' time! Please use: deadline <description> /by <yyyy-MM-dd>\n");
                } else {
                    Deadline newDeadline = new Deadline(parts[0].trim(), parts[1].trim());
                    if (tasks.getTasks().contains(newDeadline)) {
                        output.append(" Whoa! You already have this task! Won't add duplicate.\n");
                    } else {
                        tasks.add(newDeadline);
                        output.append(" Got it. I've added this task:\n    ")
                                .append(tasks.get(tasks.size()-1)).append("\n")
                                .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                    }
                }
                break;
            }
            case "event": {
                String inputStr = tokens.length < 2 ? "" : tokens[1].trim();
                String[] fromParts = inputStr.split(" /from ");
                if (inputStr.isEmpty() || fromParts.length != 2 || fromParts[0].trim().isEmpty()) {
                    output.append(" Nope - an event command must include a description and a start time using '/from'! Format: event <description> /from <start> /to <end>\n");
                } else {
                    String descEvt = fromParts[0].trim();
                    String[] toParts = fromParts[1].split(" /to ");
                    if (toParts.length != 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                        output.append(" Nope - The event command is missing '/to' or has empty times! Format: event <description> /from <start> /to <end>\n");
                    } else {
                        Event newEvent = new Event(descEvt, toParts[0].trim(), toParts[1].trim());
                        if (tasks.getTasks().contains(newEvent)) {
                            output.append(" Whoa! You already have this task! Won't add duplicate.\n");
                        } else {
                            tasks.add(newEvent);
                            output.append(" Got it. I've added this task:\n    ")
                                    .append(tasks.get(tasks.size()-1)).append("\n")
                                    .append(" Now you have ").append(tasks.size()).append(" tasks in the list!\n");
                        }
                    }
                }
                break;
            }
            case "delete": {
                assert tokens.length > 1 : "Delete command requires a task number";
                int idx = Integer.parseInt(tokens[1].trim());
                if (idx < 1 || idx > tasks.size()) {
                    output.append(" Whoops! That task number doesn't exist! Check and try again!\n");
                } else {
                    Task removed = tasks.remove(idx - 1);
                    output.append(" Noted. I've removed this task:\n    ")
                            .append(removed).append("\n")
                            .append(" Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                }
                break;
            }
            case "find": {
                String kw = tokens.length < 2 ? "" : tokens[1].trim();
                if (kw.isEmpty()) {
                    output.append(" Please provide a keyword to search for.\n");
                } else {
                    List<Task> match = tasks.getTasks().stream()
                            .filter(t -> t.description.contains(kw))
                            .collect(Collectors.toList());
                    if (match.isEmpty()) {
                        output.append(" No matching tasks found!\n");
                    } else {
                        output.append(" Here are the matching tasks in your list:\n");
                        for (int i = 0; i < match.size(); i++) {
                            output.append(" ").append(i+1).append(". ").append(match.get(i)).append("\n");
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

    public static void main(String[] args) {
        new Exactly("data/exactly.txt").run();
    }
}
