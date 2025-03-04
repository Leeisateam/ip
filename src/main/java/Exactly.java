import java.util.Scanner;
public class Exactly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;

        // Greeting message
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Exactly");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            // Read the next command from the user
            String input = scanner.nextLine();

            // Check for exit command
            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else {
                // Add the input to the tasks list
                if (taskCount < tasks.length) {
                    tasks[taskCount] = input;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" added: " + input);
                    System.out.println("____________________________________________________________");
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Task list is full! Cannot add more tasks.");
                    System.out.println("____________________________________________________________");
                }
            }
        }
        scanner.close();
    }
}
