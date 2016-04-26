package client;

import library.Conversions;
import library.IOOperations;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenViewClient {
	
	private Timer timer;
	private String hostName;
	private int port, width, height;
	
	public ScreenViewClient(String name, int portNmb){
		this.hostName = name;
		this.port = portNmb;
	}
	
	
	
	public void startTimer(){
    	timer = new Timer();
        SendImageTask task = new SendImageTask( hostName, port );
        timer.schedule(task, 0, 10);
    }
	
    
    class SendImageTask extends TimerTask{
    	
    	private Socket refSock;
    	private DataOutputStream output;    	
    	
    	protected SendImageTask( String name, int port ){
    		try {
				this.refSock = new Socket( name, port );
			} catch (Exception e) {
				e.printStackTrace();
			}
    		this.output = IOOperations.initOutput(refSock);
            width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4;
            height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4;
    	}

    	
    	public DataOutputStream getOutput(){
    		return this.output;
    	}

        private BufferedImage getScreenshot() throws AWTException{
            Rectangle snapShot = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            BufferedImage buffimg = (new Robot())
                    .createScreenCapture(snapShot);

            BufferedImage tag= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(buffimg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            return tag;
        }
    	
		@Override
		public void run() {

			DataOutputStream outputLocal = null;
			try{
				outputLocal = this.getOutput();

				byte[] a = Conversions.imageToByteArray( getScreenshot() );
				outputLocal.writeInt(a.length);
				outputLocal.write(a);

			}catch(Exception e){
                e.printStackTrace();
                timer.cancel();
                try {
                    outputLocal.close();
                    refSock.close();
                } catch (Exception exc) {e.printStackTrace();}
			}
        }
    }
}