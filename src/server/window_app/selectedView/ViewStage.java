package server.window_app.selectedView;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ViewStage extends Stage {

    public ViewStage( String host, int port, String hostIp ){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selected_view.fxml"));
        Parent root = null;

        try {
            root = fxmlLoader.load();
        }catch (Exception e){e.printStackTrace();}

        this.setMinHeight(700);
        this.setMinWidth(1300);
        this.setTitle("Widok z adresu: " + host.substring(1).split(":")[0]);
        this.setScene( new Scene(root) );
        this.show();

        ViewController controller = fxmlLoader.<ViewController>getController();
        controller.setupImageDisplay(host, port);
        controller.setupProcessTable(host);
        controller.setupPagesTable(hostIp);

        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.stopView();
            }
        });
    }


}