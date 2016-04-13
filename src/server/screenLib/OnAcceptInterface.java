package server.screenLib;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public interface OnAcceptInterface
{
    public ImageView createView( );
    public void onReceive( BufferedImage img, ImageView view );
}
