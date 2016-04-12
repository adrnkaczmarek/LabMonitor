package client;

import discoverLib.DiscoverClient;

public class Main {
	
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String serverIpAddr = new DiscoverClient().getServerAddress();

                ScreenViewClient streamScreen = new ScreenViewClient(serverIpAddr.substring(1), 11937);
                streamScreen.startTimer();
            }
        }).start();
    }    
}