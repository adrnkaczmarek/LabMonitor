package observer;

import library.Conversions;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Listener extends Thread {

    private ServerSocket server;
    private Socket socket;

    public Listener( int port_number ){
    	try {
			this.server = new ServerSocket(port_number);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    private DataInputStream initInput(){
    	InputStream input_socket = null;
    	BufferedInputStream input_buffer = null;
		try {
			input_socket = this.socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		input_buffer = new BufferedInputStream( input_socket );
		return new DataInputStream ( input_buffer );
	}

    @Override
    public void run() {
      
        DataInputStream inputStream = null;
        int length;
        BufferedImage img;
		try {
            this.socket = this.server.accept();
            inputStream = this.initInput();

            JFrame frame = new JFrame("Lab Monitor");
            JLabel label = new JLabel();
            frame.setVisible(true);
            frame.add(label);
            frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while ((length=inputStream.readInt())!=-1){
                label.setIcon(new ImageIcon(Conversions.byteArrayToImage(length, inputStream)));
            }
        }
		catch(IOException ioExc){
			try {
				socket.close();
				server.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
            e.printStackTrace();
        }
    }
}
