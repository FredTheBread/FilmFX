package filmfx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class ToDoController implements Initializable {

    MainFilmViewController film = new MainFilmViewController();

    Stage stage = new Stage();

    Timer timer = new Timer();

    public Button back;

    public Label updated;

    @FXML
    private Text saved;

    @FXML
    private Text error;

    @FXML
    private DatePicker picker;

    @FXML
    private TextArea notes;

    private Map<LocalDate, String> data = new HashMap<>();
    //This method updates the notes for the selected date
    public void updateNotes() {
        //stores the value of the notes in the data map with the key as the selected date from the picker
        data.put(picker.getValue(), notes.getText());
        //sets the updated label as visible to indicate a successful update
        updated.setVisible(true);
        //A timer is scheduled to make the updated label invisible after 3 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updated.setVisible(false);
            }
        }, 3000);
    }

    public void exit() {
        save();
        Platform.exit();
    }

    public void save() {
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("notes.data")))) {
            stream.writeObject(data);
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("Failed to save: " + e);
        }
    }

    public void load() {
        Path file = Paths.get("notes.data");
        if (Files.exists(file)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))) {
                data = (Map<LocalDate, String>) stream.readObject();
                System.out.println("Loaded!");
            } catch (Exception e) {
                System.out.println("Failed to load: " + e);
            }
        }
    }
    
    // This method is used to initialize the interface and its properties
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // load method is called to load the saved data 
        load();

        // this listener updates the notes text field whenever the date picker value changes 
        picker.valueProperty().addListener((o, oldDate, date) -> {
            // the text in the notes field is set to the value in the data map that corresponds to the selected date
            notes.setText(data.getOrDefault(date, ""));
        });

        // the value of the date picker is set to the current date
        picker.setValue(LocalDate.now());

        // the visibility of the updated label is set to false
        updated.setVisible(false);

        // an action is set for the back button to switch the film and hide the current window
        back.setOnAction(e -> {
            switchFilm();
            ((Node) e.getSource()).getScene().getWindow().hide();
        });
    }

    public void switchFilm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFilmView.fxml"));
            Parent root = loader.load();

            MainFilmViewController controller = loader.getController();
            Scene scene = new Scene(root);

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == ENTER) {
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
}
