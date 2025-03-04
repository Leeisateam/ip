import java.util.Scanner;
public class Exactly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
            } else {
                // Echo the user's input
                System.out.println("____________________________________________________________");
                System.out.println(" " + input);
                System.out.println("____________________________________________________________");
            }
        }
        scanner.close();
    }

}
