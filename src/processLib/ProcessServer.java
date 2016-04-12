package processLib;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class ProcessServer implements Callable {
    private int port;
    private List<String> data = new ArrayList<String>();
    private Socket socket;
    private ServerSocket serverSocket;



    public ProcessServer(int port) {
        System.out.println("Process Server init");
        this.port = port;
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readData();
    }



    private void startServer() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(port);
        System.out.println("[SERVER] Waiting for client on port: " + port + "...");
        socket = serverSocket.accept();
        System.out.println("[SERVER] Just connected to: " + socket.getRemoteSocketAddress());
    }

    private void readData() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String input = null;
            do {
                input = in.readUTF();
                if(input != null)
                    data.add(input);
            } while (input != null);

        } catch (IOException e) {
        } finally {
            try {
                socket.close();
                serverSocket.close();
                System.out.println("[SERVER] Socket closed.");
            } catch (IOException e) {
                System.out.println("[SERVER] Unable to close socket.");
            }
        }
    }



    @Override
    public Object call() throws Exception {
        return (data);
    }
}