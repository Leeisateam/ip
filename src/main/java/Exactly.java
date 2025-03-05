import java.util.Scanner;

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
        Task[] tasks = new Task[100];
        int taskCount = 0;

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
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim());
                    if (index < 1 || index > taskCount) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Whoops! That task number doesn't exist! Check and try again!");
                        System.out.println("____________________________________________________________");
                    } else {
                        tasks[index - 1].markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Awesome! I've marked this task as done:");
                        System.out.println("   " + tasks[index - 1]);
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
                    if (index < 1 || index > taskCount) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" That task number is off! Check and try again!");
                        System.out.println("____________________________________________________________");
                    } else {
                        tasks[index - 1].unmark();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Got it! I've marked this task as not done yet:");
                        System.out.println("   " + tasks[index - 1]);
                        System.out.println("____________________________________________________________");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Seriously? You need to provide a valid task number after 'unmark'!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5).trim();
                if (description.isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Hey, don't leave the task empty! Tell me what to do!");
                    System.out.println("____________________________________________________________");
                } else {
                    tasks[taskCount++] = new Todo(description);
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("deadline ")) {
                String details = input.substring(9).trim();
                String[] parts = details.split(" /by ");
                if (parts.length != 2) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Hmm... I need a description and a deadline separated by ' /by '! Try again!");
                    System.out.println("____________________________________________________________");
                } else {
                    tasks[taskCount++] = new Deadline(parts[0].trim(), parts[1].trim());
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("event ")) {
                String details = input.substring(6).trim();
                // Expecting format: <description> /from <start> /to <end>
                String[] partsFrom = details.split(" /from ");
                if (partsFrom.length != 2) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Uh-oh! The event command should have a description and a time period separated by ' /from '!");
                    System.out.println("____________________________________________________________");
                } else {
                    String description = partsFrom[0].trim();
                    String[] partsTo = partsFrom[1].split(" /to ");
                    if (partsTo.length != 2) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" You missed the ' /to ' part! Format should be: event <desc> /from <start> /to <end>!");
                        System.out.println("____________________________________________________________");
                    } else {
                        String from = partsTo[0].trim();
                        String to = partsTo[1].trim();
                        tasks[taskCount++] = new Event(description, from, to);
                        System.out.println("____________________________________________________________");
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1]);
                        System.out.println(" Now you have " + taskCount + " tasks in the list!");
                        System.out.println("____________________________________________________________");
                    }
                }
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" Uh-oh! I don't recognize that command! Try 'todo', 'deadline', 'event', 'mark', 'unmark', 'list' or 'bye'!");
                System.out.println("____________________________________________________________");
            }
        }
        scanner.close();
    }
}
