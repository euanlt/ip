package prism.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import prism.PrismException;

public class Parser {
    private static final DateTimeFormatter DATE_INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("[d/M/yyyy][yyyy-MM-dd]");

    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, DATE
    }

    public static CommandType parseCommandType(String fullCommand) throws PrismException {
        String trimmed = fullCommand.trim();
        if (trimmed.equals("bye")) {
            return CommandType.BYE;
        } else if (trimmed.equals("list")) {
            return CommandType.LIST;
        } else if (trimmed.matches("mark(\\s.*)?")) {
            return CommandType.MARK;
        } else if (trimmed.matches("unmark(\\s.*)?")) {
            return CommandType.UNMARK;
        } else if (trimmed.matches("delete(\\s.*)?")) {
            return CommandType.DELETE;
        } else if (trimmed.matches("todo(\\s.*)?")) {
            return CommandType.TODO;
        } else if (trimmed.matches("deadline(\\s.*)?")) {
            return CommandType.DEADLINE;
        } else if (trimmed.matches("event(\\s.*)?")) {
            return CommandType.EVENT;
        } else if (trimmed.matches("date(\\s.*)?")) {
            return CommandType.DATE;
        } else {
            throw new PrismException("!!! I'm sorry, but I don't know what that means");
        }
    }

    public static int parseIndex(String command, String prefix) throws PrismException {
        if (!command.matches("^" + prefix + "\\s+\\d+$")) {
            throw new PrismException("!!! Please tell me which task number to " + prefix + ", e.g. '" + prefix + " 2'.");
        }
        try {
            return Integer.parseInt(command.substring(prefix.length()).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PrismException("!!! Invalid task number provided.");
        }
    }

    public static String parseTodoDescription(String fullCommand) throws PrismException {
        // Extract everything after "todo" and trim standard whitespace
        String description = fullCommand.substring(4).trim();

        if (description.isEmpty()) {
            throw new PrismException("!!! The description of a todo cannot be empty.");
        }

        return description;
    }

    public static String[] parseDeadlineArgs(String command) throws PrismException {
        if (!command.matches("^deadline\\s+.+\\s+/by\\s+.+$")) {
            throw new PrismException(
                    "!!! A deadline needs a description and a '/by' time, e.g. 'deadline return book /by 2019-12-02 1800'.");
        }
        String[] parts = command.substring(9).split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new PrismException("!!! A deadline needs both a description and a '/by' time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public static String[] parseEventArgs(String command) throws PrismException {
        if (!command.matches("^event\\s+.+\\s+/from\\s+.+\\s+/to\\s+.+$")) {
            throw new PrismException(
                    "!!! An event needs a description, a '/from' time, and a '/to' time.");
        }
        String[] parts = command.substring(6).split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new PrismException("!!! An event needs a description, a '/from' time, and a '/to' time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }

    public static LocalDate parseQueryDate(String command) throws PrismException {
        if (!command.matches("^date\\s+(\\d{4}-\\d{2}-\\d{2}|\\d{1,2}/\\d{1,2}/\\d{4})$")) {
            throw new PrismException("!!! Please specify a date in yyyy-MM-dd or d/M/yyyy format, e.g. 'date 2019-12-02'.");
        }
        try {
            return LocalDate.parse(command.substring(4).trim(), DATE_INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new PrismException("!!! Invalid date format.");
        }
    }
}