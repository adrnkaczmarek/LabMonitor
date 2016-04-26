package library;

import java.io.*;
import java.net.Socket;

public class IOOperations {

    public static DataOutputStream initOutput(Socket socket){
        OutputStream output_socket = null;
        BufferedOutputStream output_buffer = null;
        try {
            output_socket = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output_buffer = new BufferedOutputStream( output_socket );
        return new DataOutputStream ( output_buffer );
    }

    public static DataInputStream initInput(Socket socket){
        InputStream input_socket = null;
        BufferedInputStream input_buffer = null;
        try {
            input_socket = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        input_buffer = new BufferedInputStream( input_socket );
        return new DataInputStream ( input_buffer );
    }

}
