package filmfx;

import com.sun.javafx.runtime.VersionInfo;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FilmFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFilmView.fxml"));
            Parent root = loader.load();
            
            MainFilmViewController controller = loader.getController();
            Scene scene = new Scene(root);
            
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == ENTER) {
                        controller.enter();
                    }
                }    
            });
            
            stage.setTitle("Film Manager");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
