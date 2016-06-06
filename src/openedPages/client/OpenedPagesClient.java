package openedPages.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class OpenedPagesClient{
    private Socket socket;
    private String name;
    private int portNmb;
    public OpenedPagesClient(String name, int portNmb)
    {
        this.name = name;
        this.portNmb = portNmb;
    }

    public PrintWriter GetOpenedPagesStream() throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }


    public void Connect() throws IOException {
        System.out.println("<OP_Client> Connecting to server...");
        socket = new Socket(name, portNmb);
        System.out.println("<OP_Client> Connected!");

    }
    public void CloseConnetion()
    {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
