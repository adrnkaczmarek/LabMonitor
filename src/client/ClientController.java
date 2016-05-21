package client;

import discoverLib.DiscoverClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import procManageLib.ManageListener;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable{

    @FXML
    protected Button start, stop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    protected void start(){
        stop.setDisable(false);
        start.setDisable(true);
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

    @FXML
    protected void stop(){
        stop.setDisable(true);
        start.setDisable(false);
    }
}
