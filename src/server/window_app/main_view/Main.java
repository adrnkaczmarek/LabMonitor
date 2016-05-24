package server.window_app.main_view;

import discoverLib.DiscoverServer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.screenLib.ScreenView;
import server.screenLib.ScreenViewServer;

public class Main extends Application
{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start( Stage primaryStage ) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("server_view.fxml"));

        DiscoverServer discoverThread = new DiscoverServer();
        discoverThread.start();

        primaryStage.setTitle("Monitorowanie sali laboratoryjnej");
        //primaryStage.setMaximized(true);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(800);
        primaryStage.setScene(new Scene(root));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ScreenView.isRunning = false;
                ScreenViewServer.isRunning = false;
                ScreenViewServer.closeServer();
                discoverThread.stopServer();
            }
        });

        primaryStage.show();
    }
}
