package prism.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
        setConversationBackground(conversation);
        addMessage(conversation, "Prism: Hello! I'm Prism, your quietly enthusiastic task companion.\n"
                + "Give me a command and we'll bring your day into focus.", false);
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
                    response = "Until next time—keep your day in focus!";
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

        ImageView avatar = createAvatar(isUserMessage ? "/images/user-avatar.png" : "/images/prism-avatar.png");
        HBox messageRow = new HBox(8);
        if (isUserMessage) {
            messageRow.getChildren().add(messageLabel);
            if (avatar != null) {
                messageRow.getChildren().add(avatar);
            }
        } else {
            if (avatar != null) {
                messageRow.getChildren().add(avatar);
            }
            messageRow.getChildren().add(messageLabel);
        }
        messageRow.setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageRow.setMaxWidth(Double.MAX_VALUE);
        if (!isUserMessage) {
            messageLabel.maxWidthProperty().bind(conversation.widthProperty().subtract(18));
        } else {
            messageLabel.setMaxWidth(420);
        }
        conversation.getChildren().add(messageRow);
    }

    /** Applies the low-contrast background image, falling back to a plain color if unavailable. */
    private void setConversationBackground(VBox conversation) {
        Image background = loadImage("/images/prism-background.png");
        if (background == null) {
            conversation.setStyle("-fx-background-color: #f7f9fc;");
            return;
        }
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        conversation.setBackground(new Background(backgroundImage));
    }

    /** Returns a small circular avatar image, or {@code null} when its resource is unavailable. */
    private ImageView createAvatar(String resourcePath) {
        Image image = loadImage(resourcePath);
        if (image == null) {
            return null;
        }
        ImageView avatar = new ImageView(image);
        avatar.setFitWidth(30);
        avatar.setFitHeight(30);
        avatar.setPreserveRatio(true);
        Circle clip = new Circle(15, 15, 15);
        avatar.setClip(clip);
        return avatar;
    }

    /** Loads an image resource without preventing the application from starting if it is missing. */
    private Image loadImage(String resourcePath) {
        try {
            return new Image(getClass().getResourceAsStream(resourcePath));
        } catch (RuntimeException exception) {
            return null;
        }
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
