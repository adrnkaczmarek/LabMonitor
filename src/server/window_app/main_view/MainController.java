package server.window_app.main_view;

import discoverLib.DiscoverServer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import server.screenLib.OnAcceptInterface;
import server.screenLib.ScreenViewServer;
import server.window_app.selectedView.ViewStage;
import javafx.scene.control.Label;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;

public class MainController implements Initializable, OnAcceptInterface
{
    @FXML
    private FlowPane videoPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new DiscoverServer()).start();
        new Thread(new ScreenViewServer(11937, this)).start();
    }

    @Override
    public ImageView createView( BufferedImage img , Socket socket ) {
        final ImageView view = new ImageView();
        final Image buffimg = SwingFXUtils.toFXImage(img, null);

        view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new ViewStage( socket.getRemoteSocketAddress().toString(), socket.getPort() );
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.setImage(buffimg);

                String[] addr = socket.getRemoteSocketAddress().toString().substring(1).split(":");

                Text ipTxt = new Text(addr[0]);
                ipTxt.setStyle("-fx-font: 24 arial;");
                ipTxt.setFill(Color.WHITE);

                GridPane gridPane = new GridPane();
                gridPane.add(view, 0, 0);
                gridPane.setMargin(view, new Insets(8, 8, 0, 8));
                gridPane.add(ipTxt,0,1);
                gridPane.setHalignment(ipTxt, HPos.CENTER);
                gridPane.setStyle("-fx-background-color: #262626");

                videoPanel.getChildren().add(gridPane);
            }
        });
        return view;
    }

    @Override
    public void deleteView(ImageView view) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                videoPanel.getChildren().remove(view);
            }
        });
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