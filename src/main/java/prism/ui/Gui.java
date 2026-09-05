package prism.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import prism.Prism;

/** Provides a JavaFX chat window for the Prism task manager. */
public class Gui extends Application {
    private final Prism prism = new Prism("./data/prism.txt", new Ui(false));

    /** Starts the JavaFX application window. */
    @Override
    public void start(Stage stage) {
        VBox conversation = new VBox(8);
        conversation.setPadding(new Insets(10));
        addMessage(conversation, "Prism: Hello! I'm Prism.\nWhat can I do for you?", false);
        ScrollPane conversationPane = new ScrollPane(conversation);
        conversationPane.setFitToWidth(true);
        conversation.heightProperty().addListener((observable, oldHeight, newHeight) ->
                conversationPane.setVvalue(1.0));

        TextField commandField = new TextField();
        commandField.setPromptText("Enter a command, e.g. list or todo read book");
        Button sendButton = new Button("Send");
        Runnable sendCommand = () -> {
            String command = commandField.getText().trim();
            if (!command.isEmpty()) {
                String response = prism.executeCommand(command);
                addMessage(conversation, "You: " + command, true);
                if (command.equals("bye")) {
                    response = "Bye. Hope to see you again soon!";
                }
                addMessage(conversation, "Prism: " + response, false);
                if (command.equals("bye")) {
                    conversation.setDisable(true);
                    sendButton.setDisable(true);
                    commandField.setDisable(true);
                }
                commandField.clear();
            }
        };
        sendButton.setOnAction(event -> sendCommand.run());
        commandField.setOnAction(event -> sendCommand.run());

        HBox inputBar = new HBox(8, commandField, sendButton);
        HBox.setHgrow(commandField, Priority.ALWAYS);
        inputBar.setPadding(new Insets(10));
        BorderPane root = new BorderPane(conversationPane, null, null, inputBar, null);
        root.setPadding(new Insets(10));
        stage.setTitle("Prism");
        stage.setScene(new Scene(root, 650, 450));
        stage.show();
    }

    /** Adds a message bubble aligned according to whether it came from the user. */
    private void addMessage(VBox conversation, String message, boolean isUserMessage) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setPadding(new Insets(8));
        messageLabel.setStyle(isUserMessage
                ? "-fx-background-color: #d9ecff; -fx-background-radius: 10;"
                : "-fx-background-color: #eeeeee; -fx-background-radius: 10;");

        HBox messageRow = new HBox(messageLabel);
        messageRow.setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        conversation.getChildren().add(messageRow);
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }
}
