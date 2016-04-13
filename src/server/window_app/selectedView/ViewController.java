package server.window_app.selectedView;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import processLib.ProcessModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable{

    @FXML
    private GridPane gridPane;

    private TableView table = new TableView();
    private ObservableList<ProcessModel> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupProcessTable();
    }

    private void setupProcessTable() {
        table.setEditable(true);

        TableColumn processName = new TableColumn("Process");
        processName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("imageName"));

        TableColumn sessionName = new TableColumn("Session");
        sessionName.setCellValueFactory(new PropertyValueFactory<ProcessModel, String>("sessionName"));

        table.getColumns().addAll(processName, sessionName);
        table.setPadding(new Insets(0, 0, 0, 0));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(data);

        gridPane.add(table, 1, 0);
    }
}
