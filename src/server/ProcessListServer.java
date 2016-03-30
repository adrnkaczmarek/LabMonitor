package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessListServer {
    private int port;
    ServerSocket listener;

    public ProcessListServer(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        listener = new ServerSocket(port);
        try {
            while (true) {
                System.out.println("[SERVER] Waiting for client on port: " + port + "...");
                new Thread(new ProcessList(listener.accept())).start();
            }
        } finally {
            listener.close();
        }
    }

    public void stopServer() {
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ProcessList implements Runnable {
        private Socket socket;

        public ProcessList(Socket socket) {
            System.out.println("[SERVER] Just connected to: " + socket.getRemoteSocketAddress());
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String input = null;
                do {
                    input = in.readLine();
                    if(input != null)
                        System.out.println(input);
                } while (input != null);

            } catch (IOException e) {
            } finally {
                try {
                    socket.close();
                    System.out.println("[SERVER] Socket closed.");
                } catch (IOException e) {
                    System.out.println("[SERVER] Unable to close socket.");
                }
            }
        }
    }
}
