import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a single deadline date and time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("[d/M/yyyy HHmm][yyyy-MM-dd HHmm]");
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter FILE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected LocalDateTime by;

    public Deadline(String description, String by) throws PrismException {
        super(description, TaskType.DEADLINE);
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String text) throws PrismException {
        try {
            String trimmedText = text.trim();
            if (trimmedText.length() == 10) {
                trimmedText += " 2359";
            }
            return LocalDateTime.parse(trimmedText, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new PrismException(
                    "!!! Invalid date format. Please use 'd/M/yyyy HHmm' or 'yyyy-MM-dd HHmm'.");
        }
    }

    public LocalDateTime getBy() {
        return this.by;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + this.by.format(FILE_FORMATTER);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.by.format(OUTPUT_FORMATTER) + ")";
    }
}