package processLib;

import java.io.*;
import java.net.Socket;



public class ProcessClient implements Runnable {
    private String ipAdd;
    private int port;

    public ProcessClient(String ipAdd, int port) {
        this.ipAdd = ipAdd;
        this.port = port;
    }

    @Override
    public void run() {
        String osName = System.getProperty("os.name");

        //tmp, poki nie bedzie komunikacji
        while(true) {
            if (osName.startsWith("Windows")) {
                try {
                    connectAndSend(Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe"));
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            } else if (osName.startsWith("Linux")) {
                try {
                    connectAndSend(Runtime.getRuntime().exec("ps -e"));
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectAndSend(Process p) {
        try {
            Socket client = new Socket(ipAdd, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream output = new DataOutputStream(outToServer);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.writeUTF(line);
            }

            reader.close();
            client.close();
        } catch (Exception e) {
            System.out.println("[CLIENT] Unable to connect with server.");
        }
    }
}
