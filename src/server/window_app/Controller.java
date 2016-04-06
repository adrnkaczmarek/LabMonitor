package server.window_app;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import server.ScreenViewServer;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, OnAcceptInterface
{
    @FXML
    private FlowPane videoPanel;

    private ImageView video;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScreenViewServer listenImage = new ScreenViewServer(11937, this);
        new Thread(listenImage).start();
    }

    @Override
    public void onAccept() {}

    @Override
    public void onReceive(BufferedImage img) {
        final Image screenshot = SwingFXUtils.toFXImage(img, null);
        if(video==null){
            video = new ImageView();
            video.setImage(screenshot);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    videoPanel.getChildren().add( video );
                }
            });
        }
        else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    video.setImage(screenshot);
                }
            });
        }
    }
}