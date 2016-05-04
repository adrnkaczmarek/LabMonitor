package server.window_app.selectedView;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ViewStage extends Stage {

    public ViewStage( String host, int port ){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selected_view.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        }catch (Exception e){e.printStackTrace();}
        this.setMinHeight(700);
        this.setMinWidth(1200);
        this.setTitle("Widok");
        this.setScene( new Scene(root) );
        this.show();

        ViewController controller = fxmlLoader.<ViewController>getController();
        controller.setupRemoteHostConnection(host, port);
        controller.setupProcessTable(host);

        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.stopServer();
            }
        });
    }


}