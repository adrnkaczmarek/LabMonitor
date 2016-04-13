package server.window_app.main_view;

import discoverLib.DiscoverServer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import server.screenLib.OnAcceptInterface;
import server.screenLib.ScreenViewServer;
import server.window_app.selectedView.ViewStage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, OnAcceptInterface
{
    @FXML
    private FlowPane videoPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new DiscoverServer()).start();

        ScreenViewServer listenImage = new ScreenViewServer(this);
        new Thread(listenImage).start();
    }

    @Override
    public ImageView createView( BufferedImage img ) {
        final ImageView view = new ImageView();
        final Image buffimg = SwingFXUtils.toFXImage(img, null);

        view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new ViewStage();
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.setImage(buffimg);
                videoPanel.getChildren().add(view);
            }
        });
        return view;
    }

    @Override
    public void onReceive(BufferedImage img, ImageView view) {
        final Image screenshot = SwingFXUtils.toFXImage(img, null);
        Platform.runLater(new ChangeImage(view) {
                @Override
                public void run() {
                    this.setImage(screenshot);
                }
            });
    }
}