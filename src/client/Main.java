package client;

import discoverLib.DiscoverClient;
import procManageLib.ManageListener;
import processLib.ProcessClient;

public class Main {
	
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String serverIpAddr = new DiscoverClient().getServerAddress();

                new Thread(new ManageListener(serverIpAddr.substring(1), 6066)).start();

                (new ScreenViewClient(serverIpAddr.substring(1), 11937)).sendingSmallScreen();

                (new ScreenViewClient(11938)).listenForMaximized();

                //new Thread(new ProcessClient(serverIpAddr.substring(1), 6066)).start();   //poki co wysyla na okraglo, pozniej dorobie komunikacje


            }
        }).start();
    }    
}