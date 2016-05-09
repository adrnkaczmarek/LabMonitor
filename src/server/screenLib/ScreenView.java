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
    private DataInputStream inputStream;

    public ScreenView(Socket socket, OnAcceptInterface event, ImageView image){
        this.socket = socket;
        this.acceptEvent = event;
        view = image;
    }

    public void closeSocket(){
        try {
            socket.close();
            inputStream.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void run() {
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
            closeSocket();
            acceptEvent.deleteView(view);
        }
    }
}
