package prism.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prism.Prism;

/** Provides a JavaFX chat window for the Prism task manager. */
public class Gui extends Application {
    private final Prism prism = new Prism("./data/prism.txt", new Ui(false));

    /** Starts the JavaFX application window. */
    @Override
    public void start(Stage stage) {
        VBox conversation = new VBox(8);
        conversation.setPadding(new Insets(16, 14, 16, 14));
        conversation.setStyle("-fx-background-color: #f7f9fc;");
        addMessage(conversation, "Prism: Hello! I'm Prism.\nWhat can I do for you?", false);
        ScrollPane conversationPane = new ScrollPane(conversation);
        conversationPane.setFitToWidth(true);
        conversationPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        conversationPane.setStyle("-fx-background: #f7f9fc; -fx-background-color: #f7f9fc;");
        conversation.heightProperty().addListener((observable, oldHeight, newHeight) ->
                conversationPane.setVvalue(1.0));

        TextField commandField = new TextField();
        commandField.setPromptText("Enter a command, e.g. list or todo read book");
        commandField.setStyle("-fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 10 12;");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; "
                + "-fx-padding: 10 18; -fx-background-color: #3568d4; -fx-text-fill: white;");
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
        inputBar.setPadding(new Insets(10, 0, 0, 0));
        BorderPane root = new BorderPane(conversationPane, null, null, inputBar, null);
        root.setPadding(new Insets(14));
        root.setStyle("-fx-background-color: #f7f9fc;");
        stage.setTitle("Prism");
        stage.setScene(new Scene(root, 650, 450));
        stage.setMinWidth(420);
        stage.setMinHeight(300);
        stage.show();
    }

    /** Adds a message bubble aligned according to whether it came from the user. */
    private void addMessage(VBox conversation, String message, boolean isUserMessage) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(isUserMessage ? new Insets(8, 12, 8, 12) : new Insets(10, 14, 10, 14));
        messageLabel.setStyle(getMessageStyle(message, isUserMessage));

        HBox messageRow = new HBox(messageLabel);
        messageRow.setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        if (!isUserMessage) {
            messageLabel.maxWidthProperty().bind(conversation.widthProperty().subtract(18));
        } else {
            messageLabel.setMaxWidth(420);
        }
        conversation.getChildren().add(messageRow);
    }

    /** Returns the visual style that distinguishes user, application, and error messages. */
    private String getMessageStyle(String message, boolean isUserMessage) {
        if (isUserMessage) {
            return "-fx-background-color: #3568d4; -fx-text-fill: white; -fx-background-radius: 12; "
                    + "-fx-font-size: 14px;";
        }
        if (message.contains(": !!!")) {
            return "-fx-background-color: #fff0f0; -fx-text-fill: #a51d2d; -fx-border-color: #e25563; "
                    + "-fx-border-width: 0 0 0 4; -fx-font-size: 14px;";
        }
        return "-fx-background-color: white; -fx-text-fill: #253047; -fx-background-radius: 10; "
                + "-fx-border-color: #e0e6f0; -fx-border-radius: 10; -fx-font-size: 14px;";
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }
}
