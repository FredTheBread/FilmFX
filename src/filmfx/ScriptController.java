package filmfx;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ScriptController implements Initializable {

    public TextArea script;
    public Button openScript;
    public Button saveScript;
    public Label pathLabel;
    public Label successLabel;

    FileChooser scriptFile = new FileChooser();
    File selectedFile = scriptFile.showOpenDialog(null);
    Timer timer = new Timer();
    String FILE = selectedFile.toString();

    //This method reads data from a file at a specific position with a specific size
    private static byte[] readDataFromFile(String file, int pos, int size) throws IOException {
        //Open the file for reading with "r" access mode
        RandomAccessFile fileScript = new RandomAccessFile(file, "r");

        //Seek to the specified position in the file
        fileScript.seek(pos);

        //Create a byte array with the specified size
        byte[] bytesToRead = new byte[size];

        //Read the bytes from the file into the byte array
        fileScript.read(bytesToRead);

        //Close the file
        fileScript.close();

        //Return the bytes that have been read from the file
        return bytesToRead;
    }

    private static byte[] writeDataToFile(String file, String data, int pos) throws IOException {
        // Open the file with read/write access
        RandomAccessFile fileScript = new RandomAccessFile(file, "rw");

        // Go to the specified position in the file
        fileScript.seek(pos);

        // Write the data to the file
        fileScript.write(data.getBytes());

        // Close the file
        fileScript.close();

        // Return null as the function's return type is byte[] but we don't need to return anything
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initOpen();
        successLabel.setVisible(false);
        pathLabel.setText(selectedFile.toString());
    }

    public void initOpen() {
        //only accept files with the extension.txt
        scriptFile.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));
        if (scriptFile != null) {
            try {
                //System.out.println(new String(readDataFromFile(FILE, 0, FILE.length())));
                //use the previous meethod to get the file's data and use it
                script.appendText(new String(readDataFromFile(FILE, 0, 999999)));
            } catch (IOException e) {
                System.out.println("An error occured! " + e);
            }
        }
    }
//
//    public void open() {
//        File selected = scriptFile.showOpenDialog(null);
//        String selectedF = selected.toString();
//        scriptFile.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));
//        if (scriptFile != null) {
//            try {
//                pathLabel.setText(selectedF);
//                script.setText(new String(readDataFromFile(selectedF, 0, selectedF.length())));
//            } catch (IOException e) {
//                System.out.println("There has been an error!" + e);
//            }
//        }
//    }

    //This method saves the script data to a file
    public void save() {
        try {
            // Create a writer to write to the file
            PrintWriter writer = new PrintWriter(FILE, "UTF-8");
            // Clear the contents of the file
            writer.flush();
            // Write the script data to the file at position 0
            writeDataToFile(FILE, script.getText(), 0);
            // Make the success label visible
            successLabel.setVisible(true);
            // Schedule a task to hide the success label after 3 seconds
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    successLabel.setVisible(false);
                }
            }, 3000);
            // Print a message indicating the file has been saved successfully
            System.out.println("File has been saved successfully!");
        } catch (IOException e) {
            System.out.println("An error has occured!" + e);
        }
    }
}
