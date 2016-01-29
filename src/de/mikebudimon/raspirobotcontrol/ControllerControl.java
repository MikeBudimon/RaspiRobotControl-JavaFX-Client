package de.mikebudimon.raspirobotcontrol;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerControl implements Initializable {
    public Label textDistance;
    public ImageView imageAlarm;
    public Button buttonBackward, buttonForward, buttonStop, buttonBuzzer, buttonRight, buttonLeft;
    public WebView webView;
    public AnchorPane controlView;
    private String mIP;
    private int mPort = 0;
    private boolean mFlag = false;
    private SocketConnection connection;


    public void setFlag(boolean flag) {
        this.mFlag = flag;
    }

    public void setPort(int port) {
        this.mPort = port;
    }

    public void setIP(String IP) {
        this.mIP = IP;
    }

    /**
     * Go back to login scene
     */
    public void goBack(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(content));
        if (connection.getFlag()) {
            connection.stop();
        }
    }

    /**
     * Connect to a socket server
     */
    public void startConnection() {
        connection = new SocketConnection(mIP, mPort, textDistance, imageAlarm);
        connection.connect();
    }

    /**
     * Starts when object is created
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            while (!mFlag) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startConnection();
        }).start();

        Platform.runLater(() -> {
            WebEngine webEngine = webView.getEngine();
            webEngine.load("http://" + mIP + ":8080/javascript_simple.html");
        });
    }

    /**
     * Handle key input
     */
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equals("4")) buttonLeftClick();
        if (keyEvent.getCharacter().equals("6")) buttonRightClick();
        if (keyEvent.getCharacter().equals("8")) buttonForwardClick();
        if (keyEvent.getCharacter().equals("2")) buttonBackwardClick();
        if (keyEvent.getCharacter().equals("5")) buttonStopClick();
        if (keyEvent.getCharacter().equals("0")) buttonBuzzerClick();

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Buttons
     */

    public void buttonLeftClick() {
        connection.setCommand("4");
    }

    public void buttonRightClick() {
        connection.setCommand("6");
    }

    public void buttonForwardClick() {
        connection.setCommand("8");
    }

    public void buttonBackwardClick() {
        connection.setCommand("2");
    }

    public void buttonStopClick() {
        connection.setCommand("5");
    }

    public void buttonBuzzerClick() {
        connection.setCommand("0");
    }
}
