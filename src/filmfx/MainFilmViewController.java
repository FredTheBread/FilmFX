package filmfx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainFilmViewController implements Initializable {

    //Declaring Labels and TextFields for movie data, cast data and cast table columns
    public Label movieName;
    public Label scriptLabel;
    
    public TextField actorName;
    public TextField characterName;
    public TextField actorAge;
    public TextField scenesNumber;
    public ChoiceBox actorGender;
    
    public TextField MovieTitle;
    public TextField DirectorName;
    public TextField EditorName;
    public TextField SoundName;
    
    public TableView<Actor> film;
    public TableColumn<Actor, String> colActor;
    public TableColumn<Actor, String> colCharacter;
    public TableColumn<Actor, String> colAge;
    public TableColumn<Actor, String> colScenes;
    public TableColumn<Actor, String> colGender;
    
    public DatePicker date;
    public Button scriptFile;
    public Button scriptOpenButton;
    public Button toDoList;

    //Declaring file to store data
    File infoFile = new File("cast.txt");

    //Creating instance of FilmFX and Stage
    FilmFX main = new FilmFX();
    Stage stage = new Stage();

    //overriding initialize method to setup UI components 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting up the action for toDoList button
        toDoList.setOnAction(e -> {
            switchToDo();
            ((Node) e.getSource()).getScene().getWindow().hide();
        });
        
        //calling method to accept only integers for age input
        onlyIntegers();
        
        //Adding gender options to choice box
        actorGender.getItems().add("Male");
        actorGender.getItems().add("Female");
        actorGender.setValue("Male");
        
        //setting cell value according to actor object variable names
        colActor.setCellValueFactory(new PropertyValueFactory<>("actor"));
        colCharacter.setCellValueFactory(new PropertyValueFactory<>("character"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("ageOfActor"));
        colScenes.setCellValueFactory(new PropertyValueFactory<>("numberOfScenes"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("selectedGender"));
        
        //setting alignment for each column in table
        colActor.setStyle("-fx-alignment: CENTER;");
        colCharacter.setStyle("-fx-alignment: CENTER;");
        colAge.setStyle("-fx-alignment: CENTER;");
        colScenes.setStyle("-fx-alignment: CENTER;");
        colGender.setStyle("-fx-alignment: CENTER;");
        
        //setting data to the table
        film.setItems(getPeople());
        
        //setting table editable
        film.setEditable(true);
        
        //setting cells in table editable
        colActor.setCellFactory(TextFieldTableCell.forTableColumn());
        colCharacter.setCellFactory(TextFieldTableCell.forTableColumn());
        colAge.setCellFactory(TextFieldTableCell.forTableColumn());
        colScenes.setCellFactory(TextFieldTableCell.forTableColumn());
        colGender.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //setting multiple selection mode for table
        film.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    //Loads the FXML file "ToDo.fxml" using FXMLLoader and sets it as the scene for the stage.
    public void switchToDo() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ToDo.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Film To Do List");
        } catch (IOException e) {
            System.out.println(e);
        }

    }
    //Clears the file if it exists by overwriting it with an empty string and displaying an "Information" alert.
    public void clearFile() {
        //writer.flush();
        if (infoFile.exists()) {
            try {
                PrintWriter writer = new PrintWriter(infoFile);
                writer.write(" ");
                writer.close();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "File has been cleared!", ButtonType.OK);
                a.show();
            } catch (IOException e) {
                System.out.println("There's been an error!" + e);
            }
        } else {
            System.out.println("This file does not exist!");
        }
    }
    //Saves information to a file using a buffered writer.
    public void saveFile(ObservableList<Actor> observableActorList, File file) {
        try {
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));
            //Writes the movie title, director name, editor name, sound editor name, date of filming, and script location.
            if (isExportValid() == true) {
                outWriter.write("Movie Title: " + MovieTitle.getText());
                outWriter.write("\nDirector: " + DirectorName.getText());
                outWriter.write("\nEditor: " + EditorName.getText());
                outWriter.write("\nSound Editor: " + SoundName.getText());
                if (date.getValue() == null) {
                    outWriter.write("\nDate of Filming: Not set\n");
                } else {
                    outWriter.write("\nDate of Filming: " + date.getValue());
                }
                if (scriptLabel.getText().equals("Not added")) {
                    outWriter.write("\nScript Location: None added\n\n");
                } else {
                    outWriter.write("\nScript Location: " + scriptLabel.getText() + "\n\n");
                }

                for (Actor actor : observableActorList) {
                    outWriter.write(actor.toString());
                    outWriter.newLine();
                }
                System.out.println(observableActorList.toString());
                outWriter.close();

                Alert a = new Alert(Alert.AlertType.INFORMATION, "Information has been saved!", ButtonType.OK);
                a.show();
            }
        } catch (IOException e) {
            Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", ButtonType.OK);
            ioAlert.setContentText("An error has occured!");
            ioAlert.showAndWait();
            if (ioAlert.getResult() == ButtonType.OK) {
                ioAlert.close();
            }
        }
    }
    //Adds tableview content to saveFile
    public void handleSave(ActionEvent event) {
        if (infoFile != null) {
            saveFile(film.getItems(), infoFile);
        }
    }
    //makes sure only numbers can be added to age and scene number
    public void onlyIntegers() {
        actorAge.textProperty().addListener(new ChangeListener<String>() {
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    actorAge.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        scenesNumber.textProperty().addListener(new ChangeListener<String>() {
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    scenesNumber.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }
    //opens dialog box to select txt file with script
    public void selectScript() {
        FileChooser scriptFile = new FileChooser();
        scriptFile.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));
        File selectedFile = scriptFile.showOpenDialog(null);
        try {
            if (scriptFile != null) {
                scriptLabel.setText(selectedFile.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //opens fxml file for a new script window
    public void createScript() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("newScript.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Your Movie Script");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //opens fxml file to show existing scripts in a window
    public void openScript() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Script.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Your Movie Script");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //when clicked, all textfields are added to the table
    public void ButtonAdd() {
        if (isInputValid() == true) {
            String actor = actorName.getText();
            String character = characterName.getText();
            String ageOfActor = actorAge.getText();
            String numberOfScenes = scenesNumber.getText();
            String selectedGender = actorGender.getValue().toString();
            Actor newActor = new Actor(actor, character, ageOfActor, numberOfScenes, selectedGender);
            film.getItems().add(newActor);

            actorName.clear();
            characterName.clear();
            actorAge.clear();
            scenesNumber.clear();
            actorGender.setValue("Male");
        }
    }
    
    //clears all rows in the table of content
    public void buttonClear() {
        film.getItems().clear();
    }
    
    //adds a new actor to the table
    public ObservableList<Actor> getPeople() {
        ObservableList<Actor> actor = FXCollections.observableArrayList();
        actor.add(new Actor("John Smith", "Johnny Mustermann", "17", "25", "Male"));
        return actor;
    }
    //sets the movie label to the movie title
    public void enter() {
        if (!MovieTitle.getText().isEmpty()) {
            movieName.setText(MovieTitle.getText());
        }
    }
    //removes selected row from the table
    public void buttonRemove() {
        ObservableList<Actor> selectedRows, allPeople;
        allPeople = film.getItems();

        selectedRows = film.getSelectionModel().getSelectedItems();

        selectedRows.forEach((actor) -> {
            allPeople.remove(actor);
        });
    }
    //checks if all fields have been entered before pressing add
    private boolean isInputValid() {
        String errorMessage = "";
        if (actorName.getText() == null || actorName.getText().length() == 0) {
            errorMessage += "Actor Name is not valid\n";
        }
        if (characterName.getText() == null || characterName.getText().length() == 0) {
            errorMessage += "Character Name is not valid\n";
        }
        if (actorAge.getText() == null || actorAge.getText().length() == 0) {
            errorMessage += "Actor Age is not valid\n";
        }
        if (scenesNumber.getText() == null || scenesNumber.getText().length() == 0) {
            errorMessage += "Scenes Number is not valid\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    //checks if all textfields have been filled before saving to textfile
    public boolean isExportValid() {
        String errorMessage = "";
        if (MovieTitle.getText() == null || MovieTitle.getText().length() == 0) {
            errorMessage += "No Movie Title added\n";
        }
        if (DirectorName.getText() == null || DirectorName.getText().length() == 0) {
            errorMessage += "No Director Name added\n";
        }
        if (EditorName.getText() == null || EditorName.getText().length() == 0) {
            errorMessage += "No Editor name added\n";
        }
        if (SoundName.getText() == null || SoundName.getText().length() == 0) {
            errorMessage += "No Sound Editor name added\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

}
