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

    private ManageListener manageListener;

    ScreenViewClient clientSmallView, clientMaxView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    protected void start(){
        stop.setDisable(false);
        start.setDisable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("[Client]"+ Thread.currentThread().getName() +" started");
                String serverIpAddr = new DiscoverClient().getServerAddress();

                clientSmallView = new ScreenViewClient(serverIpAddr.substring(1), 11937);
                clientMaxView = new ScreenViewClient(11938);
                clientSmallView.sendingSmallScreen();
                clientMaxView.listenForMaximized();
                openedPages.client.ClientPagesManager.StartClient(serverIpAddr.substring(1));
                //new Thread(new ManageListener(serverIpAddr.substring(1), 6066)).start();
                manageListener = new ManageListener(serverIpAddr.substring(1), 6066);
                manageListener.start();

                System.out.println("[Client]"+ Thread.currentThread().getName() +" closed");
            }
        }).start();
    }

    @FXML
    protected void stop(){
        stop.setDisable(true);
        start.setDisable(false);
        clientSmallView.stopSmallClient();
        clientMaxView.stopMaxClient();

        stopManageServer();
    }

    public void stopManageServer() {
        if(manageListener != null)
            manageListener.stop();
        //if(manageListener != null)
            //manageListener.stopManageServer();
    }
}
