import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles reading tasks from and writing tasks to the hard disk storage file.
 */
public class Storage {
    private static final DateTimeFormatter FILE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads saved tasks from the data file.
     *
     * @return List of parsed tasks. Returns an empty list if the file is missing or corrupted.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File file = prepareFile();

        if (file == null || !file.exists()) {
            return tasks;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            int lineNumber = 0;
            while (fileScanner.hasNextLine()) {
                lineNumber++;
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                Task task = parseLine(line);
                if (task == null) {
                    System.out.println("!!! Skipping corrupted entry on line "
                            + lineNumber + " of the data file.");
                    continue;
                }
                tasks.add(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("!!! Could not find the data file. Starting with an empty list.");
        }

        return tasks;
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line Text line read from storage.
     * @return Corresponding Task object, or null if the line is formatted incorrectly.
     */
    private Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String typeSymbol = parts[0].trim();
        String doneFlag = parts[1].trim();
        String description = parts[2].trim();

        if (!doneFlag.equals("0") && !doneFlag.equals("1")) {
            return null;
        }

        Task task = createTaskFromParts(typeSymbol, description, parts);
        if (task == null) {
            return null;
        }

        if (doneFlag.equals("1")) {
            task.isDone = true;
        }

        return task;
    }

    /**
     * Instantiates the specific Task subclass based on type symbol and parameters.
     */
    private Task createTaskFromParts(String typeSymbol, String description, String[] parts) {
        try {
            switch (typeSymbol) {
                case "T":
                    return new Todo(description);

                case "D":
                    if (parts.length < 4) {
                        return null;
                    }
                    return new Deadline(description, parts[3].trim());

                case "E":
                    if (parts.length < 5) {
                        return null;
                    }
                    return new Event(description, parts[3].trim(), parts[4].trim());

                default:
                    return null;
            }
        } catch (PrismException e) {
            // Fails gracefully if date parsing fails in Deadline/Event constructors
            return null;
        }
    }

    /**
     * Saves the current list of tasks to the storage file.
     *
     * @param tasks List of tasks to write to disk.
     */
    public void save(List<Task> tasks) {
        File file = new File(this.filePath);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                for (Task task : tasks) {
                    writer.write(task.toFileFormat() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("!!! Could not save tasks to disk: " + e.getMessage());
        }
    }

    /**
     * Prepares the file and parent directories, creating them if necessary.
     */
    private File prepareFile() {
        File file = new File(this.filePath);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            System.out.println("!!! Could not set up the data file. Starting with an empty list.");
            return null;
        }
    }
}