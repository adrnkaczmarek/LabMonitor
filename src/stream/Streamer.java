package stream;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class Streamer {
	
	private Timer timer;
	private String hostName;
	private int port;
	
	public Streamer( String name, int portNmb ){
		this.hostName = name;
		this.port = portNmb;
	}
	
	
	
	public void startTimer(){
    	timer = new Timer();
        SendImageTask task = new SendImageTask( hostName, port );
        timer.schedule( task, 0, 250);
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
    		this.output = this.initOutput();
    	}
    	
    	
    	
    	public DataOutputStream getOutput(){
    		return this.output;
    	}
    	
    	private DataOutputStream initOutput(){
			OutputStream output_socket = null;
			BufferedOutputStream output_buffer = null;
			try {
				output_socket = this.refSock.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			output_buffer = new BufferedOutputStream( output_socket );
			return new DataOutputStream ( output_buffer );
		}
    	
		@Override
		public void run() {
			
			DataOutputStream outputLocal = null;
			try{
				outputLocal = this.getOutput();
				Rectangle snapShot = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				
				BufferedImage buffimg = (new Robot())
		           		.createScreenCapture(snapShot);
				ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();		             
				ImageIO.write(buffimg, "jpg", arrayOutput);
				byte[] a = arrayOutput.toByteArray();
				outputLocal.writeInt(a.length);
				outputLocal.write( a );
			}
			catch(SocketException se){
				timer.cancel();
				try {
					refSock.close();
					outputLocal.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			catch(Exception e){ 
				e.printStackTrace();
			}
		}
    }
}