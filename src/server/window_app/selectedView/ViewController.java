package server.window_app.selectedView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
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




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //=================DO WYWALENIA TESTY GRIDA==============
        Label testKuba = new Label("Miejsce dla Kuby");
        gridPane.add(testKuba, 1, 0);
        //=======================================================
        setupGridPaneConstraints();
    }



    public void setupRemoteHostConnection(String host, int port){
        Socket socket = null;
        try{
            System.out.println("[SERVER] host: " + host + " port: " + port);
            new ScreenView(new Socket(host.split(":")[0].substring(1), 11938), this, maximizedView).start();
        }catch (Exception e){
            e.printStackTrace();
            try{socket.close();} catch (Exception innerE){innerE.printStackTrace();}
        }
    }

    public void setupProcessTable(String clientIpAddr) {
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

    public void stopServer() {
        isProcessServerOn = false;
    }

    private void setupGridPaneConstraints() {
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        col1.setPercentWidth(80);
        col2.setPercentWidth(20);
        col2.setFillWidth(true);
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1,col2);
        gridPane.setHgap(80);
        //gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setStyle("-fx-background-color: #595959");
    }

    @Override
    public ImageView createView(BufferedImage img, Socket socket) {
        final ImageView view = new ImageView();
        final Image buffimg = SwingFXUtils.toFXImage(img, null);
        System.out.println("[SERVER]Image received");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.setImage(buffimg);
                view.setPreserveRatio(true);
                view.fitWidthProperty().bind(gridPane.widthProperty().divide(1.25));
                view.fitHeightProperty().bind(gridPane.widthProperty());
                gridPane.add(view, 0, 0, 1, 2);
            }
        });
        return view;
    }

    @Override
    public void deleteView(Object view) {

    }

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
