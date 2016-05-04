package discoverLib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by Krzys on 2016-04-11.
 */
public class DiscoverClient {
    final static String multicastAddress = "224.0.0.3";
    final static int port = 8888;

    public String getServerAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName(multicastAddress);

            try (MulticastSocket clientSocket = new MulticastSocket(port)) {
                clientSocket.joinGroup(inetAddress);
                byte[] buf = new byte[10];

                DatagramPacket discoverPacket = new DatagramPacket(buf, buf.length);
                //System.out.println("[DISCOVER CLIENT] Listening...");
                clientSocket.receive(discoverPacket);

                String msg = new String(buf, 0, buf.length);
                //System.out.println("[DISCOVER CLIENT] Message received - " + msg);
                if(msg.startsWith("myDISCOVER")) {
                    return (discoverPacket.getAddress().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return("");
    }
}