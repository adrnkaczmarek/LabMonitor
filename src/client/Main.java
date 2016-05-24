package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.window_app.selectedView.ViewController;

public class Main extends Application{
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client_view.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("client_view.fxml"));

        try {
            root = fxmlLoader.load();
        }catch (Exception e){e.printStackTrace();}

        primaryStage.setTitle("Monitorowanie");
        primaryStage.setHeight(150);
        primaryStage.setWidth(250);
        primaryStage.setScene(new Scene(root));

        ClientController controller = fxmlLoader.<ClientController>getController();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.stopManageServer();
            }
        });
        primaryStage.show();
    }
}