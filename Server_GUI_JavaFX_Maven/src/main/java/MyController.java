import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.stage.Window;
import javafx.util.Duration;
import java.util.Objects;
import java.io.IOException;


public class MyController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private BorderPane root1;
    @FXML
    private TextField portTextField;
    @FXML
    private Label serverStat;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public MyController(){
    }

    //Method to Start the server
    public void startServer(ActionEvent e) throws IOException {
        if (isPortValid()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/InfoScreen.fxml"));
            Parent root3 = fxmlLoader.load();
            root3.getStylesheets().add("/CSS/InfoScreen.css");
            root.getScene().setRoot(root3);
        }

    }

    public void closeServer(ActionEvent e){
        Stage stage = (Stage) root1.getScene().getWindow();
        stage.close();
    }


    public boolean isPortValid(){
        try {
            String portText = portTextField.getText();

            // Checks if the text field is empty
            if (portText.isEmpty()) {
                throw new IllegalArgumentException("ERROR!!! Port cannot be empty");
            }

            // Parse the text to an integer
            int port = Integer.parseInt(portText);

            // Checks for valid port range
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("ERROR!!! Port number must be between 1 and 65535");
            }

            return true;// If everything is valid
        } catch (Exception e) { // Catch all exceptions
            serverStat.setText(e.getMessage() != null ? e.getMessage() : "ERROR!!! Please enter a valid port number");
            portTextField.clear();
            portTextField.setPromptText("Enter valid port number");
            return false;
        }
    }

//    public void setPort(){
//        Server.server.setPortNum(Integer.parseInt(portTextField.getText()));
//        System.out.println(Server.server.getPortNum());
//    }
}
