package de.mikebudimon.raspirobotcontrol;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketConnection {

    private String mServerName;
    private int mPort;
    private Socket mClient;
    private DataInputStream mInputStream;
    private DataOutputStream mOutputStream;
    private boolean mFlag = false;
    private String mCommand = "5";
    private String mDistance = "0";
    private boolean mWarningState = false;
    private Label textDistance;
    private ImageView imageAlarm;


    public SocketConnection(String serverName, int port, Label textDistance, ImageView imageAlarm) {
        this.mServerName = serverName;
        this.mPort = port;
        this.imageAlarm = imageAlarm;
        this.textDistance = textDistance;

    }

    public boolean getFlag() {
        return mFlag;
    }

    public void setCommand(String mCommand) {
        this.mCommand = mCommand;
    }

    public void stop() throws IOException {
        mOutputStream.writeUTF("stop");
        mOutputStream.flush();
        mFlag = false;
        mInputStream.close();
        mOutputStream.close();
        mClient.close();
    }

    public void connect() {
        try {
            mClient = new Socket(mServerName, mPort);
            mOutputStream = new DataOutputStream(mClient.getOutputStream());
            mInputStream = new DataInputStream(mClient.getInputStream());
            mFlag = true;
            String oldCommand = "5";

            while (mFlag) {

                if (mInputStream.available() > 0) {
                    mDistance = mInputStream.readUTF();
                    Platform.runLater(() -> textDistance.setText(mDistance + " cm"));
                }

                // activates the warning image
                if (Double.parseDouble(mDistance) <= 10.0 && !mWarningState) {
                    Platform.runLater(() -> imageAlarm.setVisible(true));
                    mWarningState = true;
                }

                // deactivates the warning image
                if (Double.parseDouble(mDistance) > 10.0 && mWarningState) {
                    Platform.runLater(() -> imageAlarm.setVisible(false));
                    mWarningState = false;
                }

                if (!oldCommand.equals(mCommand)) {
                    mOutputStream.flush();
                    mOutputStream.writeUTF(mCommand);
                    oldCommand = mCommand;
                }

                Thread.sleep(150);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
