package exactly;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * JavaFX entry point. Loads the FXML and injects Exactly.
 */
public class Main extends Application {

    private Exactly exactly = new Exactly("data/exactly.txt");

    /**
     * Start the JavaFX application by loading MainWindow.fxml.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass()
                    .getResource("/app.css").toExternalForm());

            stage.setTitle("Exactly Chatbot");
            stage.setScene(scene);
            stage.setResizable(true);

            // Inject the Exactly instance into the controller
            MainWindow controller = loader.getController();
            controller.setExactly(exactly);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
