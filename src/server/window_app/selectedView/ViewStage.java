package server.window_app.selectedView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewStage extends Stage {

    public ViewStage(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("selected_view.fxml"));
        }catch (Exception e){e.printStackTrace();}

        this.setTitle("Widok");
        this.setScene( new Scene(root) );
        this.show();
    }
}