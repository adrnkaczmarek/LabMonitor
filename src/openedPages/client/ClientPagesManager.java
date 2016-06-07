package openedPages.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class ClientPagesManager {

    public static void StartClient(String serverIp)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder captured_pages = new StringBuilder("");
                OpenedPages opened_pages = new OpenedPages(captured_pages);
                int captured_pages_last_length = 0;
                opened_pages.StartCapturing();

                while (true)
                {
                    try {
                        OpenedPagesClient client = new OpenedPagesClient(serverIp, 19938);
                        while (true) {
                            Thread.sleep(5000);
                            if(captured_pages.length() > captured_pages_last_length) {
                                for(int i = 0; i < captured_pages_last_length; i++)
                                {
                                    captured_pages.deleteCharAt(0);
                                }
                                client.Connect();
                                PrintWriter out = client.GetOpenedPagesStream();
                                String pagesToSent = captured_pages.toString();
                                out.printf(pagesToSent);
                                /*test:*/System.out.printf("<OP_client> Send pages: \n" + pagesToSent);
                                client.CloseConnetion();
                                captured_pages_last_length = captured_pages.length();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
