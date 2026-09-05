package prism;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import prism.parser.Parser;
import prism.storage.Storage;
import prism.task.Deadline;
import prism.task.Event;
import prism.task.Task;
import prism.task.Todo;
import prism.tasklist.TaskList;
import prism.ui.Ui;

/** Coordinates the Prism command loop and delegates work to the application components. */
public class Prism {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** Creates a Prism application backed by the specified data file. */
    public Prism(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(this.storage.load());
    }

    /** Runs the interactive command loop until the user enters {@code bye}. */
    public void run() {
        this.ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = this.ui.readCommand();
            this.ui.showLine();

            try {
                Parser.CommandType commandType = Parser.parseCommandType(fullCommand);
                switch (commandType) {
                    case BYE:
                        isExit = true;
                        break;

                    case LIST:
                        handleList();
                        break;

                    case MARK:
                        handleMark(fullCommand);
                        break;

                    case UNMARK:
                        handleUnmark(fullCommand);
                        break;

                    case DELETE:
                        handleDelete(fullCommand);
                        break;

                    case TODO:
                        handleTodo(fullCommand);
                        break;

                    case DEADLINE:
                        handleDeadline(fullCommand);
                        break;

                    case EVENT:
                        handleEvent(fullCommand);
                        break;

                    case DATE:
                        handleDate(fullCommand);
                        break;

                    case FIND:
                        handleFind(fullCommand);
                        break;

                    default:
                        throw new PrismException("!!! Unknown command.");
                }
            } catch (PrismException e) {
                this.ui.showError(e.getMessage());
            }

            this.ui.showLine();
        }

        this.ui.showBye();
        this.ui.close();
    }

    /** Displays all tasks currently stored in the task list. */
    private void handleList() {
        if (this.tasks.getSize() == 0) {
            this.ui.showMessage("Your task list is empty.");
            return;
        }
        this.ui.showMessage("Here are the tasks in your list:\n");
        for (int i = 0; i < this.tasks.getSize(); i++) {
            try {
                this.ui.showMessage((i + 1) + "." + this.tasks.getTask(i));
            } catch (PrismException ignored) {
                // Task index guaranteed valid by loop bounds
            }
        }
    }

    /** Marks the task selected by the command as done and persists the change. */
    private void handleMark(String command) throws PrismException {
        int index = Parser.parseIndex(command, "mark");
        Task task = this.tasks.markTask(index);
        this.storage.save(this.tasks.getTasks());
        this.ui.showMessage("Nice! I've marked this task as done:\n  " + task);
    }

    /** Marks the task selected by the command as not done and persists the change. */
    private void handleUnmark(String command) throws PrismException {
        int index = Parser.parseIndex(command, "unmark");
        Task task = this.tasks.unmarkTask(index);
        this.storage.save(this.tasks.getTasks());
        this.ui.showMessage("OK, I've marked this task as not done yet:\n  " + task);
    }

    /** Deletes the task selected by the command and persists the change. */
    private void handleDelete(String command) throws PrismException {
        int index = Parser.parseIndex(command, "delete");
        Task removed = this.tasks.deleteTask(index);
        this.storage.save(this.tasks.getTasks());
        this.ui.showMessage("Noted. I've removed this task:\n"
                + "  " + removed + "\n"
                + "Now you have " + this.tasks.getSize() + " tasks in the list.");
    }

    /** Creates and stores a todo task from the command. */
    private void handleTodo(String command) throws PrismException {
        String description = Parser.parseTodoDescription(command);
        Task newTask = this.tasks.addTask(new Todo(description));
        this.storage.save(this.tasks.getTasks());
        showAddedTaskMessage(newTask);
    }

    /** Creates and stores a deadline task from the command. */
    private void handleDeadline(String command) throws PrismException {
        String[] args = Parser.parseDeadlineArgs(command);
        Task newTask = this.tasks.addTask(new Deadline(args[0], args[1]));
        this.storage.save(this.tasks.getTasks());
        showAddedTaskMessage(newTask);
    }

    /** Creates and stores an event task from the command. */
    private void handleEvent(String command) throws PrismException {
        String[] args = Parser.parseEventArgs(command);
        Task newTask = this.tasks.addTask(new Event(args[0], args[1], args[2]));
        this.storage.save(this.tasks.getTasks());
        showAddedTaskMessage(newTask);
    }

    /** Displays tasks that occur on the date supplied in the command. */
    private void handleDate(String command) throws PrismException {
        LocalDate queryDate = Parser.parseQueryDate(command);
        List<Task> matchingTasks = this.tasks.getTasksOnDate(queryDate);

        this.ui.showMessage("Here are the tasks occurring on "
                + queryDate.format(DISPLAY_DATE_FORMATTER) + ":\n");
        if (matchingTasks.isEmpty()) {
            this.ui.showMessage("  No matching tasks found.");
            return;
        }
        for (Task task : matchingTasks) {
            this.ui.showMessage("  " + task);
        }
    }

    /** Displays tasks whose descriptions contain the keyword supplied in the command. */
    private void handleFind(String command) throws PrismException {
        String keyword = Parser.parseFindKeyword(command);
        List<Task> matchingTasks = this.tasks.findTasks(keyword);

        this.ui.showMessage("Here are the matching tasks in your list:\n");
        if (matchingTasks.isEmpty()) {
            this.ui.showMessage("  No matching tasks found.");
            return;
        }
        for (int i = 0; i < matchingTasks.size(); i++) {
            this.ui.showMessage((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /** Displays the standard confirmation message for a newly added task. */
    private void showAddedTaskMessage(Task task) {
        this.ui.showMessage("Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + this.tasks.getSize() + " tasks in the list.");
    }

    /** Starts Prism using the default data file. */
    public static void main(String[] args) {
        new Prism("./data/prism.txt").run();
    }
}
