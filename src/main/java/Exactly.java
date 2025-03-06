import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
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

public class Exactly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        System.out.println("____________________________________________________________");
        System.out.println(" Hey! I'm Exactly and I'm pumped to help you out! What do you need?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye! Keep crushing it and never settle for less!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                if (tasks.isEmpty()) {
                    System.out.println(" Wow, your task list is empty! Let's get started and add some awesome tasks!");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim());
                    if (index < 1 || index > tasks.size()) { // changed: use tasks.size()
                        System.out.println("____________________________________________________________");
                        System.out.println(" Huh? That task number doesn't exist! Check and try again!");
                        System.out.println("____________________________________________________________");
                    } else {
                        tasks.get(index - 1).markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Awesome! I've marked this task as done:");
                        System.out.println("   " + tasks.get(index - 1));
                        System.out.println("____________________________________________________________");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" C'mon, you need to provide a valid task number after 'mark'!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim());
                    if (index < 1 || index > tasks.size()) { // changed: use tasks.size()
                        System.out.println("____________________________________________________________");
                        System.out.println(" That task number is off! Check and try again!");
                        System.out.println("____________________________________________________________");
                    } else {
                        tasks.get(index - 1).unmark();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Got it! I've marked this task as not done yet:");
                        System.out.println("   " + tasks.get(index - 1));
                        System.out.println("____________________________________________________________");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Seriously? You need to provide a valid task number after 'unmark'!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("todo")) {
                String description = input.equals("todo") ? "" : input.substring(4).trim();
                if (description.isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Huh? The description for a todo task cannot be empty! Please give me a proper task!");
                    System.out.println("____________________________________________________________");
                } else {
                    tasks.add(new Todo(description));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("deadline")) {
                String details = input.equals("deadline") ? "" : input.substring(8).trim();
                String[] parts = details.split(" /by ");
                if (details.isEmpty() || parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nope - a deadline command must have a description and a '/by' time! Please use: deadline <description> /by <time>");
                    System.out.println("____________________________________________________________");
                } else {
                    tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("event")) {
                String details = input.equals("event") ? "" : input.substring(5).trim();
                String[] partsFrom = details.split(" /from ");
                if (details.isEmpty() || partsFrom.length != 2 || partsFrom[0].trim().isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nope - an event command must include a description and a start time using '/from'! Format: event <description> /from <start> /to <end>");
                    System.out.println("____________________________________________________________");
                } else {
                    String description = partsFrom[0].trim();
                    String[] partsTo = partsFrom[1].split(" /to ");
                    if (partsTo.length != 2 || partsTo[0].trim().isEmpty() || partsTo[1].trim().isEmpty()) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Nope - The event command is missing '/to' or has empty times! Format: event <description> /from <start> /to <end>");
                        System.out.println("____________________________________________________________");
                    } else {
                        String from = partsTo[0].trim();
                        String to = partsTo[1].trim();
                        // Removed erroneous line using array indexing and taskCount
                        tasks.add(new Event(description, from, to)); // minimal change: only add via ArrayList
                        System.out.println("____________________________________________________________");
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks.get(tasks.size() - 1));
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list!");
                        System.out.println("____________________________________________________________");
                    }
                }
            } else if (input.startsWith("delete ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim());
                    if (index < 1 || index > tasks.size()) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Whoops! That task number doesn't exist! Check and try again!");
                        System.out.println("____________________________________________________________");
                    } else {
                        Task removed = tasks.remove(index - 1);
                        System.out.println("____________________________________________________________");
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removed);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("____________________________________________________________");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Please provide a valid task number after 'delete'!");
                    System.out.println("____________________________________________________________");
                }
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" Huh? I don't understand what you said!");
                System.out.println("____________________________________________________________");
            }
        }
        scanner.close();
    }
}
