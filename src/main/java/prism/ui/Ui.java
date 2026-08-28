package prism.ui;

import java.util.Scanner;

/** Handles console input and output for the Prism application. */
public class Ui {
    private static final String LINE = "____________________________________________________________\n";
    private static final String LOGO =
            " ____       _               \n"
                    + "|  _ \\ _ __(_)___ _ __ ___  \n"
                    + "| |_) | '__| / __| '_ ` _ \\ \n"
                    + "|  __/| |  | \\__ \\ | | | | |\n"
                    + "|_|   |_|  |_|___/_| |_| |_|\n";

    private final Scanner scanner;

    /** Creates a UI connected to standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Reads the next complete command from the user. */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /** Displays the application logo and welcome message. */
    public void showWelcome() {
        showLine();
        System.out.println(LOGO + "Hello! I'm Prism.\nWhat can I do for you?");
        showLine();
    }

    /** Displays a separator line. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Displays an error message. */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays a general application message. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Displays the exit message. */
    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /** Closes the input scanner. */
    public void close() {
        this.scanner.close();
    }
}