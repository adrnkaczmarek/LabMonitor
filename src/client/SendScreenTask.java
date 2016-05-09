package client;

import library.Conversions;
import library.IOOperations;
import library.Screen;

import java.awt.*;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class SendScreenTask extends TimerTask {

    private Socket refSock;
    private DataOutputStream output;
    private Dimension screenshotDimension;
    private Timer timer;

    protected SendScreenTask(Socket socket, Timer timer, double imageSizeDivider){
        refSock = socket;
        output = IOOperations.initOutput(refSock);
        screenshotDimension = Screen.getScreenDimension(imageSizeDivider);
        this.timer = timer;
    }


    public DataOutputStream getOutput(){
        return this.output;
    }

    @Override
    public void run() {

        DataOutputStream outputLocal = null;
        try{
            outputLocal = getOutput();

            byte[] a = Conversions.imageToByteArray(Screen.getScreenshot(screenshotDimension));
            outputLocal.writeInt(a.length);
            outputLocal.write(a);

        }catch(Exception e){
            e.printStackTrace();
            timer.cancel();
            try {
                outputLocal.close();
                refSock.close();
            } catch (Exception exc) {e.printStackTrace();}
            (new ScreenViewClient(11938)).listenForMaximized();
        }
    }
}
