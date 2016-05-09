package server.screenLib;

import javafx.scene.image.ImageView;
import library.Conversions;
import library.IOOperations;

import java.io.DataInputStream;
import java.net.Socket;

public class ScreenView extends Thread {

    private Socket socket;
    private OnAcceptInterface acceptEvent;
    private Object view;

    public ScreenView(Socket socket, OnAcceptInterface event, ImageView image){
        this.socket = socket;
        this.acceptEvent = event;
        view = image;
    }

    @Override
    public void run() {
        DataInputStream inputStream = null;
        int length;
        try {
            inputStream = IOOperations.initInput(socket);
            length = inputStream.readInt();
            view = acceptEvent.createView( Conversions.byteArrayToImage(length, inputStream), socket );

            while ((length=inputStream.readInt())!=-1){
                acceptEvent.onReceive( Conversions.byteArrayToImage(length, inputStream) , view );
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
                inputStream.close();
                acceptEvent.deleteView(view);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
