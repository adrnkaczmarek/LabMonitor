package server.window_app.selectedView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

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
    }
}