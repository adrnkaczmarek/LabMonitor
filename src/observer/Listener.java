package observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    int port;
    ServerSocket server;
    Socket socket;

    public Listener( int port_number ){
        this.port = port_number;
    }

        @Override
    public void run() {
        try {
            DataInputStream in;
            InputStream in_sock;
            BufferedInputStream in_buf;
            int length;
            byte[] a;
            BufferedImage img;

            server = new ServerSocket(port);
            socket = server.accept();
            in_sock = socket.getInputStream();
            in_buf = new BufferedInputStream(in_sock);
            in = new DataInputStream(in_buf);

            JFrame frame = new JFrame("Lab Monitor");
            JLabel label = new JLabel();
            frame.setVisible(true);
            frame.add(label);
            frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while ((length=in.readInt())!=-1){
                a = new byte[length];
                System.out.println(length);
                in.readFully(a, 0, length);
                img = ImageIO.read(new ByteArrayInputStream(a));
                label.setIcon(new ImageIcon(img));
            }
            socket.close();
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
