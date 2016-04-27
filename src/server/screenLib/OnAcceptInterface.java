package server.screenLib;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.net.Socket;

public interface OnAcceptInterface
{
    public ImageView createView( BufferedImage img , Socket socket );
    public void deleteView( ImageView view );
    public void onReceive( BufferedImage img, ImageView view );
}
