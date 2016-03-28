package stream;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
        timer.schedule( task, 0, 50);
    }
    
    class SendImageTask extends TimerTask{
    	private Socket refSock;
    	String name;
    	int port;
    	
    	protected SendImageTask( String name, int port ){
    		try {
				this.refSock = new Socket( name, port );
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
		@Override
		public void run() {
			try{
				DataOutputStream out;
				OutputStream out_sock;
				BufferedOutputStream out_buf;
				out_sock = this.refSock.getOutputStream();
				out_buf = new BufferedOutputStream( out_sock );
				out = new DataOutputStream ( out_buf );
				Rectangle shot = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				
				BufferedImage buffimg = (new Robot())
		           		.createScreenCapture(shot);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();		             
				ImageIO.write(buffimg, "jpg", baos);
				byte[] a = baos.toByteArray();
				out.writeInt(a.length);
				out.write( a );
				System.out.println("Wys³ano");	
			}
			catch(SocketException se){
				timer.cancel();
				try {
					refSock.close();
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