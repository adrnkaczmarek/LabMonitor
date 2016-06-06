package openedPages.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class OpenedPagesServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public OpenedPagesServer(int portNmb)
    {
        try {
            serverSocket = new ServerSocket(portNmb);
            clientSocket = serverSocket.accept();
            System.out.println("Connected!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public OpenedPagesServer()
    {}
    public String GetClientIp()
    {
        InetAddress ip = clientSocket.getInetAddress();
        return ip.getHostAddress();
    }


    public BufferedReader GetOpenedPagesStream () throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return in;

    }

    public void Connect(int portNmb)
    {
        System.out.println("<OP_Server> Connecting to client...");
        try {
            serverSocket = new ServerSocket(portNmb);
            clientSocket = serverSocket.accept();
            System.out.println("<OP_Server> Server connected to " + clientSocket.getInetAddress().getHostAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StopServer()
    {
        try {
            clientSocket.close();
            serverSocket.close();
            System.out.println("<OP_server> Server disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
