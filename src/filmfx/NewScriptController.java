/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

public class NewScriptController implements Initializable {

    public TextArea script;
    public Button openScript;
    public Button saveScript;
    public Label pathLabel;
    public Label successLabel;

    FileChooser scriptFile = new FileChooser();
    File selectedFile = scriptFile.showSaveDialog(null);
    Timer timer = new Timer();
    String FILE = selectedFile.toString();

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
        successLabel.setVisible(false);
        pathLabel.setText(selectedFile.toString());
    }

    // This method saves the script data to a file
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
