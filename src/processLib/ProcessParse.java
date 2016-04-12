package processLib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ProcessParse {
    private List<String> inputList;
    private ObservableList<ProcessModel> outputList;
    private List<ProcessModel> list = new ArrayList<ProcessModel>();

    public ProcessParse(List<String> inputList) {
        this.inputList = inputList;
        parse();
    }


    private void parse() {
        for (int i=4 ; i<inputList.size() ; i++) {
            String[] words = inputList.get(i).split("\\s+");

            list.add(new ProcessModel(words[0], words[2]));
        }
    }


    public ObservableList<ProcessModel> getOutputList() {
        outputList = FXCollections.observableList(list);
        return outputList;
    }
}
