package exactly;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ===== Task and Subclasses =====
class Task {
    protected String description;
    protected boolean isDone;
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    public void markAsDone() { isDone = true; }
    public void unmark() { isDone = false; }
    public String getStatusIcon() { return (isDone ? "X" : " "); }
    @Override
    public String toString() { return "[" + getStatusIcon() + "] " + description; }
}

class Todo extends Task {
    public Todo(String description) { super(description); }
    @Override
    public String toString() { return "[T]" + super.toString(); }
}

class Deadline extends Task {
    protected LocalDate by;
    public Deadline(String description, String by) {
        super(description);
        // Expect input in yyyy-MM-dd format, e.g., "2019-10-15"
        this.by = LocalDate.parse(by);
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }
}

class Event extends Task {
    protected String from;
    protected String to;
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

// ===== TaskList Class =====
class TaskList {
    private List<Task> tasks;
    public TaskList() { this.tasks = new ArrayList<>(); }
    public TaskList(List<Task> tasks) { this.tasks = tasks; }
    public void add(Task task) { tasks.add(task); }
    public Task remove(int index) { return tasks.remove(index); }
    public Task get(int index) { return tasks.get(index); }
    public int size() { return tasks.size(); }
    public List<Task> getTasks() { return tasks; }
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

// ===== Storage Class =====
class Storage {
    private String filePath;
    public Storage(String filePath) { this.filePath = filePath; }
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

// ===== Ui Class =====
class Ui {
    private Scanner scanner;
    public Ui() { scanner = new Scanner(System.in); }
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");
    }
    public String readCommand() { return scanner.nextLine().trim(); }
    public void showLine() { System.out.println("____________________________________________________________"); }
    public void showError(String message) { System.out.println(message); }
    public void showLoadingError() { System.out.println("Error loading tasks from file."); }
    public void close() { scanner.close(); }
}

// ===== Parser Class (Minimal) =====
class Parser {
    public static String[] parse(String input) {
        return input.split(" ", 2);
    }
}

// ===== Main Class: Exactly =====
public class Exactly {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Exactly(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

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

    public static void main(String[] args) {
        new Exactly("data/exactly.txt").run();
    }
}
