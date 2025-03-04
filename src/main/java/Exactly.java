import java.util.Scanner;

class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmark() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

public class Exactly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Exactly! Ready to rock? What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye! Remember, excellence is a habit! See you next time!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim());
                    if (index < 1 || index > taskCount) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Oops! That task number doesn't exist! Try a valid number!");
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
                    System.out.println(" Seriously? You need to enter a valid number after 'mark'!");
                    System.out.println("____________________________________________________________");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim());
                    if (index < 1 || index > taskCount) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Hmm, that task number is off! Check and try again!");
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
                    System.out.println(" Come on! You must specify a valid task number after 'unmark'!");
                    System.out.println("____________________________________________________________");
                }
            } else {
                // If it's not a known command, add it as a new task!
                if (taskCount < tasks.length) {
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Sweet! I've added: " + input);
                    System.out.println("____________________________________________________________");
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Whoa! Your task list is full! Trim it down and try again!");
                    System.out.println("____________________________________________________________");
                }
            }
        }
        scanner.close();
    }
}
