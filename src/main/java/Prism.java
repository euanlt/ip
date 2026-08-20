import java.util.Objects;
import java.util.Scanner;

public class Prism {
    public static void main(String[] args) {
        Task[] list = new Task[100];
        int index = 0;
        int taskNum;
        String banner =
                "____________________________________________________________\n"
                        + " ____       _               \n"
                        + "|  _ \\ _ __(_)___ _ __ ___  \n"
                        + "| |_) | '__| / __| '_ ` _ \\ \n"
                        + "|  __/| |  | \\__ \\ | | | | |\n"
                        + "|_|   |_|  |_|___/_| |_| |_|\n"
                        + "Hello! I'm Prism.\n"
                        + "What can I do for you?\n"
                        + "____________________________________________________________\n";
        System.out.println(banner);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!Objects.equals(input, "bye")) {
            System.out.println("____________________________________________________________\n");

            try {
                if (Objects.equals(input, "list")) {
                    System.out.println("Here are the tasks in your list:\n");
                    for (int i = 0; i < index; i++) {
                        System.out.println((i + 1) + "." + list[i].toString());
                    }

                } else if (input.matches("mark(\\s+\\d+)?")) {
                    if (!input.matches("mark\\s+\\d+")) {
                        throw new PrismException("OOPS!!! Please tell me which task number to mark, e.g. 'mark 2'.");
                    }
                    taskNum = Integer.parseInt(input.substring(5).trim());
                    if (taskNum <= 0 || taskNum > index) {
                        throw new PrismException("OOPS!!! That task number doesn't exist. You currently have "
                                + index + " task(s).");
                    }
                    list[taskNum - 1].markAsDone();
                    System.out.println("Nice! I've marked this task as done:\n" + list[taskNum - 1].toString());

                } else if (input.matches("unmark(\\s+\\d+)?")) {
                    if (!input.matches("unmark\\s+\\d+")) {
                        throw new PrismException("OOPS!!! Please tell me which task number to unmark, e.g. 'unmark 2'.");
                    }
                    taskNum = Integer.parseInt(input.substring(7).trim());
                    if (taskNum <= 0 || taskNum > index) {
                        throw new PrismException("OOPS!!! That task number doesn't exist. You currently have "
                                + index + " task(s).");
                    }
                    list[taskNum - 1].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:\n" + list[taskNum - 1].toString());

                } else if (input.matches("todo(\\s.*)?")) {
                    if (!input.matches("^todo\\s+.+$")) {
                        throw new PrismException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    if (index >= list.length) {
                        throw new PrismException("OOPS!!! Your task list is full, I can't add any more tasks.");
                    }
                    list[index++] = new Todo(input.substring(5).trim());
                    System.out.println(
                            "Got it. I've added this task:\n"
                                    + list[index - 1].toString() + "\n"
                                    + "Now you have " + index + " tasks in the list."
                    );

                } else if (input.matches("deadline(\\s.*)?")) {
                    if (!input.matches("^deadline\\s+.+\\s+/by\\s+.+$")) {
                        throw new PrismException(
                                "!!! A deadline needs a description and a '/by' time, "
                                        + "e.g. 'deadline return book /by Sunday'.");
                    }
                    if (index >= list.length) {
                        throw new PrismException("OOPS!!! Your task list is full, I can't add any more tasks.");
                    }
                    String[] parts = input.substring(9).split(" /by ", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new PrismException(
                                "OOPS!!! A deadline needs both a description and a '/by' time.");
                    }
                    list[index++] = new Deadline(parts[0].trim(), parts[1].trim());
                    System.out.println(
                            "Got it. I've added this task:\n"
                                    + list[index - 1].toString() + "\n"
                                    + "Now you have " + index + " tasks in the list."
                    );

                } else if (input.matches("event(\\s.*)?")) {
                    if (!input.matches("^event\\s+.+\\s+/from\\s+.+\\s+/to\\s+.+$")) {
                        throw new PrismException(
                                "!!! An event needs a description, a '/from' time, and a '/to' time, "
                                        + "e.g. 'event project meeting /from Mon 2pm /to 4pm'.");
                    }
                    if (index >= list.length) {
                        throw new PrismException("!!! Your task list is full, I can't add any more tasks.");
                    }
                    String[] parts = input.substring(6).split(" /from | /to ", 3);
                    if (parts.length < 3 || parts[0].trim().isEmpty()
                            || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                        throw new PrismException(
                                "!!! An event needs a description, a '/from' time, and a '/to' time.");
                    }
                    list[index++] = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
                    System.out.println(
                            "Got it. I've added this task:\n"
                                    + list[index - 1].toString() + "\n"
                                    + "Now you have " + index + " tasks in the list."
                    );

                } else {
                    throw new PrismException("I'm sorry, but I don't understand what that means");
                }

            } catch (PrismException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("____________________________________________________________\n");
            input = sc.nextLine();
        }
        System.out.println(
                "____________________________________________________________\n"
                        + "Bye. Hope to see you again soon!\n"
                        + "____________________________________________________________\n"
        );
    }
}