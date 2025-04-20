package exactly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * JavaFX entry point for Exactly GUI.
 */
public class Main extends Application {
    private ListView<Message> chatListView;
    private TextField inputField;
    private Button sendButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Chat list view with custom cell factory
        chatListView = new ListView<>();
        chatListView.setCellFactory(lv -> new ListCell<Message>() {
            private final Label bubble = new Label();
            private final HBox row = new HBox();
            {
                // Enable wrapping and limit bubble width to 60% of list width
                bubble.setWrapText(true);
                bubble.setTextAlignment(TextAlignment.LEFT);
                HBox.setHgrow(bubble, Priority.ALWAYS);
                bubble.maxWidthProperty().bind(lv.widthProperty().multiply(0.6));
                row.setPadding(new Insets(4));
                row.getChildren().add(bubble);
            }
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setGraphic(null);
                } else {
                    bubble.getStyleClass().setAll(
                            msg.isError()    ? "error-message" :
                                    msg.isFromUser() ? "user-message"  :
                                            "bot-message"
                    );
                    row.setAlignment(msg.isFromUser() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                    bubble.setText(msg.getText());
                    setGraphic(row);
                }
            }
        });

        // Input field and send button
        inputField = new TextField();
        inputField.setPromptText("Type a command...");
        sendButton = new Button("Send");
        sendButton.setDefaultButton(true);

        // Actions on send
        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        HBox inputBar = new HBox(8, inputField, sendButton);
        inputBar.setPadding(new Insets(0, 10, 10, 10));
        HBox.setHgrow(inputField, Priority.ALWAYS);

        // Root layout
        BorderPane root = new BorderPane();
        root.setCenter(chatListView);
        root.setBottom(inputBar);
        BorderPane.setMargin(chatListView, new Insets(10));

        // Scene + CSS
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        // Stage setup
        primaryStage.setTitle("Exactly");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        // User message
        chatListView.getItems().add(new Message(text, true, false));

        // Bot response
        String response;
        boolean isError = false;
        try {
            response = new Exactly("data/exactly.txt").getResponse(text);
        } catch (Exception ex) {
            response = ex.getMessage();
            isError = true;
        }
        chatListView.getItems().add(new Message(response, false, isError));

        inputField.clear();
        chatListView.scrollTo(chatListView.getItems().size() - 1);
    }

    /**
     * Simple model for chat messages.
     */
    public static class Message {
        private final String text;
        private final boolean fromUser;
        private final boolean error;

        public Message(String text, boolean fromUser, boolean error) {
            this.text = text;
            this.fromUser = fromUser;
            this.error = error;
        }

        public String getText() { return text; }
        public boolean isFromUser() { return fromUser; }
        public boolean isError() { return error; }
    }
}
