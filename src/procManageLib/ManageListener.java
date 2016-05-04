package procManageLib;

import processLib.ProcessClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by Krzys on 2016-05-02.
 */
public class ManageListener implements Runnable{
    final static String multicastAddress = "224.0.0.4";
    final static int port = 7777;

    private String serverAddr;
    private int serverPort;

    private boolean isActive = true;
    private boolean startReceived = false;

    public ManageListener(String serverAddr, int serverPort) {
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        while (true) {
            try {
                InetAddress inetAddress = InetAddress.getByName(multicastAddress);

                try (MulticastSocket clientSocket = new MulticastSocket(port)) {
                    clientSocket.joinGroup(inetAddress);
                    byte[] buf = new byte[20];


                    DatagramPacket discoverPacket = new DatagramPacket(buf, buf.length);
                    System.out.println("[MANAGE LISTENER] Listening...");
                    clientSocket.receive(discoverPacket);

                    String msg = new String(buf, 0, buf.length);
                    System.out.println("[MANAGE LISTENER] Message received - " + msg);
                    System.out.println("[TEST] SEND:/" + InetAddress.getLocalHost().getHostAddress());

                    if (msg.startsWith("SEND:/" + InetAddress.getLocalHost().getHostAddress())) {
                        System.out.println("Is started");
                        Thread.sleep(1);
                        new Thread(new ProcessClient(serverAddr, serverPort)).start();
                        startReceived = true;
                        //Thread.sleep(6000);
                    } else if (startReceived == true && msg.startsWith("STOP:" + InetAddress.getLocalHost().getHostAddress())) {
                        System.out.println("Is stopped");
                        isActive = false;
                    }
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
}

