package server.screenLib;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public interface OnAcceptInterface
{
    public ImageView createView( BufferedImage img );
    public void onReceive( BufferedImage img, ImageView view );
}
