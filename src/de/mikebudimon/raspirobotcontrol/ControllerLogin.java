package de.mikebudimon.raspirobotcontrol;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerLogin {

    public TextField textIP;
    public TextField textPort;

    /**
     * Switches to the control scene and sends data to it's controller
     *
     * @param event when "Start" button is clicked
     * @throws IOException
     */
    public void startControl(ActionEvent event) throws IOException {

        // load fxml to a new parent
        FXMLLoader loader = new FXMLLoader(getClass().getResource("control.fxml"));
        Parent content = loader.load();
        // get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(content));

        // get controller from the new scene and send data to it
        ControllerControl controller = loader.<ControllerControl>getController();
        controller.setIP(textIP.getText());
        controller.setPort(Integer.parseInt(textPort.getText()));
        controller.setFlag(true);
    }
}
