package client;

import discoverLib.DiscoverClient;
import processLib.ProcessClient;

public class Main {
	
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String serverIpAddr = new DiscoverClient().getServerAddress();

                ScreenViewClient streamScreen = new ScreenViewClient(serverIpAddr.substring(1), 11937);
                streamScreen.startTimer();

                new Thread(new ProcessClient(serverIpAddr.substring(1), 6066)).start();   //poki co wysyla na okraglo, pozniej dorobie komunikacje
            }
        }).start();
    }    
}