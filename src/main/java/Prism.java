void main() {

    Storage storage = new Storage("./data/prism.txt");
    List<Task> list = new ArrayList<>(storage.load());
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
    IO.println(banner);
    Scanner sc = new Scanner(System.in);
    String input = sc.nextLine();

    while (!Objects.equals(input, "bye")) {
        IO.println("____________________________________________________________\n");

        try {
            if (Objects.equals(input, "list")) {
                IO.println("Here are the tasks in your list:\n");
                for (int i = 0; i < list.size(); i++) {
                    IO.println((i + 1) + "." + list.get(i).toString());
                }

            } else if (input.matches("mark(\\s+\\d+)?")) {
                if (!input.matches("mark\\s+\\d+")) {
                    throw new PrismException("!!! Please tell me which task number to mark, e.g. 'mark 2'.");
                }
                taskNum = Integer.parseInt(input.substring(5).trim());
                if (taskNum <= 0 || taskNum > list.size()) {
                    throw new PrismException("!!! That task number doesn't exist. You currently have "
                            + list.size() + " task(s).");
                }
                list.get(taskNum - 1).markAsDone();
                storage.save(list);

            } else if (input.matches("unmark(\\s+\\d+)?")) {
                if (!input.matches("unmark\\s+\\d+")) {
                    throw new PrismException("!!! Please tell me which task number to unmark, e.g. 'unmark 2'.");
                }
                taskNum = Integer.parseInt(input.substring(7).trim());
                if (taskNum <= 0 || taskNum > list.size()) {
                    throw new PrismException("!!! That task number doesn't exist. You currently have "
                            + list.size() + " task(s).");
                }
                list.get(taskNum - 1).markAsNotDone();
                storage.save(list);

            } else if (input.matches("delete(\\s+\\d+)?")) {
                if (!input.matches("delete\\s+\\d+")) {
                    throw new PrismException("!!! Please tell me which task number to delete, e.g. 'delete 3'.");
                }
                taskNum = Integer.parseInt(input.substring(7).trim());
                if (taskNum <= 0 || taskNum > list.size()) {
                    throw new PrismException("!!! That task number doesn't exist. You currently have "
                            + list.size() + " task(s).");
                }
                Task removed = list.remove(taskNum - 1);
                storage.save(list);
                IO.println(
                        "Noted. I've removed this task:\n"
                                + "  " + removed.toString() + "\n"
                                + "Now you have " + list.size() + " tasks in the list."
                );

            } else if (input.matches("todo(\\s.*)?")) {
                if (!input.matches("^todo\\s+.+$")) {
                    throw new PrismException("!!! The description of a todo cannot be empty.");
                }
                list.add(new Todo(input.substring(5).trim()));
                storage.save(list);
                IO.println(
                        "Got it. I've added this task:\n"
                                + list.get(list.size() - 1).toString() + "\n"
                                + "Now you have " + list.size() + " tasks in the list."
                );

            } else if (input.matches("deadline(\\s.*)?")) {
                if (!input.matches("^deadline\\s+.+\\s+/by\\s+.+$")) {
                    throw new PrismException(
                            "!!! A deadline needs a description and a '/by' time, "
                                    + "e.g. 'deadline return book /by Sunday'.");
                }
                String[] parts = input.substring(9).split(" /by ", 2);
                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    throw new PrismException(
                            "!!! A deadline needs both a description and a '/by' time.");
                }
                list.add(new Deadline(parts[0].trim(), parts[1].trim()));
                storage.save(list);
                IO.println(
                        "Got it. I've added this task:\n"
                                + list.get(list.size() - 1).toString() + "\n"
                                + "Now you have " + list.size() + " tasks in the list."
                );

            } else if (input.matches("event(\\s.*)?")) {
                if (!input.matches("^event\\s+.+\\s+/from\\s+.+\\s+/to\\s+.+$")) {
                    throw new PrismException(
                            "!!! An event needs a description, a '/from' time, and a '/to' time, "
                                    + "e.g. 'event project meeting /from Mon 2pm /to 4pm'.");
                }
                String[] parts = input.substring(6).split(" /from | /to ", 3);
                if (parts.length < 3 || parts[0].trim().isEmpty()
                        || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                    throw new PrismException(
                            "!!! An event needs a description, a '/from' time, and a '/to' time.");
                }
                list.add(new Event(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                storage.save(list);
                IO.println(
                        "Got it. I've added this task:\n"
                                + list.get(list.size() - 1).toString() + "\n"
                                + "Now you have " + list.size() + " tasks in the list."
                );

            } else if (input.matches("date(\\s.*)?")) {
                if (!input.matches("^date\\s+(\\d{4}-\\d{2}-\\d{2}|\\d{1,2}/\\d{1,2}/\\d{4})$")) {
                    throw new PrismException(
                            "!!! Please specify a date in yyyy-MM-dd or d/M/yyyy format, e.g. 'date 2019-12-02'.");
                }
                String dateStr = input.substring(4).trim();
                java.time.LocalDate queryDate;
                try {
                    queryDate = java.time.LocalDate.parse(
                            dateStr,
                            java.time.format.DateTimeFormatter.ofPattern("[d/M/yyyy][yyyy-MM-dd]"));
                } catch (java.time.format.DateTimeParseException e) {
                    throw new PrismException("!!! Invalid date format.");
                }

                IO.println("Here are the tasks occurring on "
                        + queryDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n");
                int count = 0;
                for (Task task : list) {
                    if (task instanceof Deadline) {
                        Deadline deadline = (Deadline) task;
                        if (deadline.getBy().toLocalDate().equals(queryDate)) {
                            IO.println("  " + task);
                            count++;
                        }
                    } else if (task instanceof Event) {
                        Event event = (Event) task;
                        java.time.LocalDate startDate = event.getFrom().toLocalDate();
                        java.time.LocalDate endDate = event.getTo().toLocalDate();
                        boolean isWithinRange = (queryDate.isEqual(startDate) || queryDate.isAfter(startDate))
                                && (queryDate.isEqual(endDate) || queryDate.isBefore(endDate));
                        if (isWithinRange) {
                            IO.println("  " + task);
                            count++;
                        }
                    }
                }
                if (count == 0) {
                    IO.println("  No matching tasks found.");
                }
            } else {
                throw new PrismException("!!! I'm sorry, but I don't know what that means");
            }

        } catch (PrismException e) {
            IO.println(e.getMessage());
        }

        IO.println("____________________________________________________________\n");
        input = sc.nextLine();
    }
    IO.println(
            "____________________________________________________________\n"
                    + "Bye. Hope to see you again soon!\n"
                    + "____________________________________________________________\n"
    );
}