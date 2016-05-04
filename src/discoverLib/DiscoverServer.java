package discoverLib;

import java.io.IOException;
import java.net.*;

public class DiscoverServer implements Runnable {

    final static String multicastAddress = "224.0.0.3";
    final static int port = 8888;

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(multicastAddress);
            try (DatagramSocket serverSocket = new DatagramSocket()) {
                //System.out.println("[DISCOVER SERVER] Socket created.");
                String msg = "myDISCOVER";
                while(true) {
                    DatagramPacket discoverPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, inetAddress, port);
                    serverSocket.send(discoverPacket);
                    //System.out.println("[DISCOVER SERVER] Message sent. Size = " + discoverPacket.getLength());
                    Thread.sleep(5000);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}