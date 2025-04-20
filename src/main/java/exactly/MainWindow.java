package exactly;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Exactly exactly;
    private final Image userImage  = new Image(this.getClass()
            .getResourceAsStream("/images/DaUser.png"));
    private final Image botImage   = new Image(this.getClass()
            .getResourceAsStream("/images/DaBot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Inject the Exactly instance so we can call getResponse().
     */
    public void setExactly(Exactly ex) {
        exactly = ex;
        dialogContainer.getChildren()
               .add(DialogBox.getBotDialog(exactly.getWelcomeMessage(), botImage));
    }

    /** Handle user input from TextField or Send button. */
    @FXML
    private void handleUserInput() {
        String input    = userInput.getText();
        String response = exactly.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, botImage)
        );
        userInput.clear();
    }
}
