package prism.ui;

import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________\n";
    private static final String LOGO =
            " ____       _               \n"
                    + "|  _ \\ _ __(_)___ _ __ ___  \n"
                    + "| |_) | '__| / __| '_ ` _ \\ \n"
                    + "|  __/| |  | \\__ \\ | | | | |\n"
                    + "|_|   |_|  |_|___/_| |_| |_|\n";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return this.scanner.nextLine();
    }

    public void showWelcome() {
        showLine();
        System.out.println(LOGO + "Hello! I'm Prism.\nWhat can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    public void close() {
        this.scanner.close();
    }
}