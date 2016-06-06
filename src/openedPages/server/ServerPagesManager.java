package openedPages.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServerPagesManager {
    public static ArrayList<ClientPages> clientPagesList = new ArrayList<>();

    public static ArrayList<String> GetPages(String clientIp)
    {
        for (ClientPages cp : clientPagesList) {
            if(cp.getClientIp().equals(clientIp)) return cp.getPages();
        }
        return new ArrayList<String>();
    }
    public static int getListLength()
    {
        return clientPagesList.size();
    }
    public static void AddPage(String clientIp, String page)
    {
        boolean found = false;
        for (ClientPages cp : clientPagesList) {
            if(cp.getClientIp().equals(clientIp) ) {
                cp.AddPage(page);
                found = true;
            }
        }
        if(found == false){
            clientPagesList.add(new ClientPages(clientIp, page));
        }
    }

    public static void StartServer()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        OpenedPagesServer server = new OpenedPagesServer();
                        while (true) {
                            server.Connect(11938);
                            BufferedReader in = server.GetOpenedPagesStream();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                AddPage(server.GetClientIp(), inputLine);
                                System.out.println("Server; Received line: " + inputLine);
                            }
                            server.StopServer();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
