package server.window_app.selectedView;

import com.sun.rowset.internal.Row;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import library.Screen;
import procManageLib.ManageServer;
import processLib.ProcessModel;
import processLib.ProcessParse;
import processLib.ProcessServer;
import server.screenLib.OnAcceptInterface;
import server.screenLib.ScreenView;
import server.window_app.main_view.ChangeImage;

import java.awt.image.BufferedImage;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


//Dobra mamy tutaj GridPane'a, Adus ma pierwsza kolumne, Krzychu i Kuba druga podzielona na dwa wiersze
//Jak cos ta lista procesow poki co to troche lipa, bo jak bedzie uruchomionych kilka klientow to bedzie co chwile zmienial ta tabele, musze dodac komunikacje zeby jeden tylko wysylal

public class ViewController implements Initializable, OnAcceptInterface{

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView maximizedView;

    private TableView table = new TableView();
    private ObservableList<ProcessModel> data;
    private Boolean isProcessServerOn = false;
    private ScreenView screenView;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupGridPaneConstraints();
    }



    public void setupImageDisplay(String host, int port){
        try{
            System.out.println("[SERVER] host: " + host + " port: " + port);
            screenView = new ScreenView(new Socket(host.split(":")[0].substring(1), 11938), this, maximizedView);
            screenView.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setupProcessTable(String clientIpAddr) {
        Text contentText = new Text("Aktywne procesy");
        contentText.setStyle("-fx-font: 20 arial;");
        contentText.setFill(Color.WHITE);
        gridPane.add(contentText, 1, 0);


        table.setEditable(true);

        TableColumn processName = new TableColumn("Process");
        processName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("imageName"));

        TableColumn sessionName = new TableColumn("Session");
        sessionName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("sessionName"));

        table.getColumns().addAll(processName, sessionName);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(data);
        gridPane.add(table, 1, 1);

        listenForProcesses(clientIpAddr);
    }

    private void listenForProcesses(String clientIpAddr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runProcessServer(clientIpAddr);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void runProcessServer(String clientIpAddr) throws ExecutionException, InterruptedException {
        isProcessServerOn = true;

        new ManageServer().sendSendMessage(clientIpAddr);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> future = executorService.submit(new ProcessServer(6066));

        ProcessParse dataParser = new ProcessParse(future.get());
        data = dataParser.getOutputList();

        Platform.runLater(() -> {
            table.setItems(data);
        });

        executorService.shutdown();

    }

    public void stopView() {
        isProcessServerOn = false;
        screenView.closeSocket();
    }

    private void setupGridPaneConstraints() {
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(80);
        col2.setPercentWidth(20);
        col2.setFillWidth(true);
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1,col2);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        //row1.setPercentHeight(5);
        row2.setPercentHeight(90);
        row2.setVgrow(Priority.ALWAYS);


       gridPane.getRowConstraints().addAll(row1,row2);

        gridPane.setHgap(35);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        Text videoText = new Text("Pogląd monitora");
        videoText.setStyle("-fx-font: 20 arial;");
        videoText.setFill(Color.WHITE);
        gridPane.add(videoText,0, 0);
    }

    @Override
    public ImageView createView(BufferedImage img, Socket socket) {
        StackPane ipane = new StackPane();
        ipane.setAlignment(Pos.CENTER);
        ipane.setStyle("-fx-background-color: #000000");

        final ImageView view = new ImageView();
        final Image buffimg = SwingFXUtils.toFXImage(img, null);
        System.out.println("[SERVER]Image received");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.setImage(buffimg);
                view.setPreserveRatio(true);

                //view.fitWidthProperty().bind(gridPane.widthProperty().divide(1.25));
                view.fitWidthProperty().bind(gridPane.widthProperty().divide(1.4));
                view.fitHeightProperty().bind(gridPane.heightProperty().divide(1.1));

                ipane.getChildren().add(view);
                gridPane.add(ipane, 0, 1, 1, 2);
            }
        });
        return view;
    }

    @Override
    public void deleteView(Object view) {}

    @Override
    public void onReceive(BufferedImage img, Object view) {
        final Image screenshot = SwingFXUtils.toFXImage(img, null);
        Platform.runLater(new ChangeImage((ImageView)view) {
            @Override
            public void run() {
                this.setImage(screenshot);
            }
        });
    }
}
