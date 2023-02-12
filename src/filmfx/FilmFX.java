package filmfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FilmFX extends Application {
    // Declare a new Stage object
    Stage filmStage = new Stage();

    // Overrides the start method from the Application class
    @Override
    public void start(Stage primaryStage) {
        try {
            // Loads the FXML file that defines the user interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFilmView.fxml"));
            Parent root = loader.load();

            // Gets the controller for the FXML file
            MainFilmViewController controller = loader.getController();
            // Creates a new Scene with the FXML file's root node
            Scene scene = new Scene(root);

            // Adds a KeyEvent handler to the scene to listen for the ENTER key
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    // Calls the enter method of the controller if the ENTER key is pressed
                    if (event.getCode() == ENTER) {
                        controller.enter();
                    }
                }
            });
            // Sets the title of the Stage
            filmStage.setTitle("Film Manager");
            // Sets the Scene of the Stage
            filmStage.setScene(scene);
            // Shows the Stage on the screen
            filmStage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
