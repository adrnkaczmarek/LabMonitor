package server.window_app.main_view;

import discoverLib.DiscoverServer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import server.screenLib.OnAcceptInterface;
import server.screenLib.ScreenViewServer;
import server.window_app.selectedView.ViewStage;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;

public class MainController implements Initializable, OnAcceptInterface
{
    @FXML
    private FlowPane videoPanel;
    private VBox waitingPane = new VBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //new Thread(new DiscoverServer()).start();
        new Thread(new ScreenViewServer(11937, this)).start();
        openedPages.server.ServerPagesManager.StartServer();

        setupWaitingPane();
        showWaitingPane();
    }

    @Override
    public Object createView( BufferedImage img , Socket socket ) {

        Platform.runLater(() -> {
            videoPanel.getChildren().remove(waitingPane);
            videoPanel.setAlignment(Pos.TOP_LEFT);
        });

        final ImageView view = new ImageView();
        final Image buffimg = SwingFXUtils.toFXImage(img, null);
        final GridPane pane = new GridPane();
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

                pane.add(view, 0, 0);
                pane.setMargin(view, new Insets(8, 8, 0, 8));
                pane.add(ipTxt,0,1);
                pane.setHalignment(ipTxt, HPos.CENTER);
                pane.setStyle("-fx-background-color: #1a1a1a");

                videoPanel.getChildren().add(pane);
            }
        });
        return pane;
    }

    @Override
    public void deleteView(Object view) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                videoPanel.getChildren().remove(view);
            }
        });

        Platform.runLater(() -> {
            if(videoPanel.getChildren().isEmpty()) {
                showWaitingPane();
            }
        });
    }

    @Override
    public void onReceive(BufferedImage img, Object view) {
        final Image screenshot = SwingFXUtils.toFXImage(img, null);
        ObservableList children = ((GridPane)view).getChildren();
        if(children.size()!=0){
            Object tmp = children.get(0);
            Platform.runLater(new ChangeImage((ImageView)tmp) {
                    @Override
                    public void run() {
                        this.setImage(screenshot);
                    }
                });
        }
    }

    private void setupWaitingPane() {
        ProgressIndicator indicator = new ProgressIndicator();

        Text waitingText = new Text("Oczekiwanie na połączenia...");
        waitingText.setStyle("-fx-font: 24 calibri;");
        waitingText.setFill(Color.WHITE);

        waitingPane.setSpacing(20);
        waitingPane.setMinSize(350,250);

        waitingPane.setAlignment(Pos.CENTER);
        waitingPane.getChildren().addAll(indicator, waitingText);
    }

    private void showWaitingPane() {
        videoPanel.setAlignment(Pos.CENTER);
        videoPanel.getChildren().add(waitingPane);
    }
}