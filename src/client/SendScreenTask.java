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

    public void closeConn(){
        try {
            getOutput().close();
            refSock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOutput(){
        return this.output;
    }

    @Override
    public void run() {
        try{
            byte[] a = Conversions.imageToByteArray(Screen.getScreenshot(screenshotDimension));
            getOutput().writeInt(a.length);
            getOutput().write(a);

        }catch(Exception e){
            e.printStackTrace();
            timer.cancel();
            if(!refSock.isClosed()){
                closeConn();
                (new ScreenViewClient(11938)).listenForMaximized();
            }
        }
    }
}
