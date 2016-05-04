package procManageLib;

import java.io.IOException;
import java.net.*;

/**
 * Created by Krzys on 2016-05-02.
 */
public class ManageServer {
    final static String multicastAddress = "224.0.0.4";
    final static int port = 7777;

    public void sendSendMessage(String clientIpAddr) {
        try {
            InetAddress inetAddress = InetAddress.getByName(multicastAddress);
            try (DatagramSocket serverSocket = new DatagramSocket()) {
                System.out.println("[MANAGE SERVER] Socket created.");
                String msg = "SEND:" + clientIpAddr;
                System.out.println("MSG = " + msg);

                DatagramPacket discoverPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, inetAddress, port);
                serverSocket.send(discoverPacket);
                System.out.println("[MANAGE SERVER] Message sent. Size = " + discoverPacket.getLength());

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendStopMessage(String clientIpAddr) {
        try {
            InetAddress inetAddress = InetAddress.getByName(multicastAddress);
            try (DatagramSocket serverSocket = new DatagramSocket()) {
                System.out.println("[MANAGE SERVER] Socket created.");
                String msg = "STOPS:" + clientIpAddr;
                System.out.println("MSG = " + msg);

                DatagramPacket discoverPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, inetAddress, port);
                serverSocket.send(discoverPacket);
                System.out.println("[MANAGE SERVER] Message sent. Size = " + discoverPacket.getLength());

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
