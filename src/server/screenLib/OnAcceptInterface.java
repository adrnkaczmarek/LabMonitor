package server.screenLib;


import java.awt.image.BufferedImage;
import java.net.Socket;

public interface OnAcceptInterface
{
    public Object createView( BufferedImage img , Socket socket );
    public void deleteView( Object view );
    public void onReceive( BufferedImage img, Object view );
}
