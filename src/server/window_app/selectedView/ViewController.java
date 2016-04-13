package server.window_app.selectedView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import processLib.ProcessModel;
import processLib.ProcessParse;
import processLib.ProcessServer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


//Dobra mamy tutaj GridPane'a, Adus ma pierwsza kolumne, Krzychu i Kuba druga podzielona na dwa wiersze
//Jak cos ta lista procesow poki co to troche lipa, bo jak bedzie uruchomionych kilka klientow to bedzie co chwile zmienial ta tabele, musze dodac komunikacje zeby jeden tylko wysylal

public class ViewController implements Initializable{

    @FXML
    private GridPane gridPane;

    private TableView table = new TableView();
    private ObservableList<ProcessModel> data;
    private Boolean isProcessServerOn = false;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //=================DO WYWALENIA TESTY GRIDA==============
        Label testAdus = new Label("Miejsce dla Marka");
        Label testKuba = new Label("Miejsce dla Kuby");
        gridPane.add(testAdus, 0, 0);
        gridPane.add(testKuba, 1, 0);
        //=======================================================


        setupProcessTable();
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
}
