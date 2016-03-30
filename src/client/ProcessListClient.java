package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ProcessListClient {

    public void connectAndSend(String ipAdd, int port) {
        try {
            // uncomment for Linux
            //Process p = Runtime.getRuntime().exec("ps -e");
            // uncomment for Windows
            Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

            Socket client = new Socket(ipAdd, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream output = new DataOutputStream(outToServer);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.writeUTF(line + '\n');
                //System.out.println(line);
            }

            reader.close();
            client.close();

        } catch (Exception e) {
            System.out.println("[CLIENT] Unable to connect with server.");
        }
    }
}
