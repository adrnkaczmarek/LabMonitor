package client;

import library.Conversions;
import library.IOOperations;
import library.Screen;

import java.awt.*;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenViewClient {
	
	private Timer timer;
	private String hostName;
	private int port;
	
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
        private Dimension screenDimension;
    	
    	protected SendImageTask( String name, int port ){
    		try {
				this.refSock = new Socket( name, port );
			} catch (Exception e) {
				e.printStackTrace();
			}
    		this.output = IOOperations.initOutput(refSock);
            screenDimension = Screen.getScreenDimension(4);
    	}

    	
    	public DataOutputStream getOutput(){
    		return this.output;
    	}
    	
		@Override
		public void run() {

			DataOutputStream outputLocal = null;
			try{
				outputLocal = this.getOutput();

				byte[] a = Conversions.imageToByteArray( Screen.getScreenshot(screenDimension) );
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