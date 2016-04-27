package server.window_app.selectedView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import library.Conversions;
import library.IOOperations;
import processLib.ProcessModel;
import processLib.ProcessParse;
import processLib.ProcessServer;
import server.screenLib.OnAcceptInterface;
import server.screenLib.ScreenView;
import server.window_app.main_view.ChangeImage;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
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

        setupProcessTable();
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

    private void setupProcessTable() {
        table.setEditable(true);

        TableColumn processName = new TableColumn("Process");
        processName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("imageName"));

        TableColumn sessionName = new TableColumn("Session");
        sessionName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("sessionName"));

        table.getColumns().addAll(processName, sessionName);
        table.setPadding(new Insets(10, 10, 10, 10));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(data);

        gridPane.add(table, 1, 1);
        listenForProcesses();
    }

    private void listenForProcesses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runProcessServer();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void runProcessServer() throws ExecutionException, InterruptedException {
        isProcessServerOn = true;
        while (isProcessServerOn == true) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<List<String>> future = executorService.submit(new ProcessServer(6066));

            ProcessParse dataParser = new ProcessParse(future.get());
            data = dataParser.getOutputList();

            Platform.runLater(() -> {
                table.setItems(data);
            });

            executorService.shutdown();
        }
    }

    private void stopServer() {
        isProcessServerOn = false;
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
                gridPane.add( view, 0, 0, 1, 2);
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
