package exactly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Exactly chatbot = new Exactly("data/exactly.txt");
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    @Override
    public void start(Stage stage) {
        // Set up the dialog container inside a scroll pane
        dialogContainer = new VBox();
        scrollPane = new ScrollPane();
        scrollPane.setContent(dialogContainer);

        // Set up user input field and send button
        userInput = new TextField();
        sendButton = new Button("Send");

        // Create main layout and add all components
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        Scene scene = new Scene(mainLayout, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Exactly Chatbot");
        stage.show();

        // Configure layout sizes and positions
        scrollPane.setPrefSize(385, 535);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        userInput.setPrefWidth(320);
        sendButton.setPrefWidth(55);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        // Display the welcome message from Exactly
        dialogContainer.getChildren().add(new Label(chatbot.getWelcomeMessage()));

        // Set event handlers for user input
        sendButton.setOnAction(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
    }

    /**
     * Reads the user input, gets the response, and updates the chat dialog.
     */
    private void handleUserInput() {
        String input = userInput.getText();
        Label userText = new Label("You: " + input);
        dialogContainer.getChildren().add(userText);

        String response = chatbot.getResponse(input);
        Label botText = new Label("Exactly: " + response);
        dialogContainer.getChildren().add(botText);

        userInput.clear();
    }
}
