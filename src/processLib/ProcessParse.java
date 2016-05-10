package processLib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
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

            if(words[2].startsWith("Services") && !Arrays.asList(defaultProcesses).contains(words[0]))
                list.add(new ProcessModel(words[0], words[2]));
        }
    }


    public ObservableList<ProcessModel> getOutputList() {
        outputList = FXCollections.observableList(list);
        return outputList;
    }

    private String[] defaultProcesses = {
            "audiodg.exe",
            "conhost.exe",
            "csrss.exe",
            "lsass.exe",
            "lsm.exe",
            "MSCamS64.exe",
            "naPrdMgr.exe",
            "OSPPSVC.EXE",
            "PresentationFontCache.exe",
            "SearchIndexer.exe",
            "services.exe",
            "smss.exe",
            "spoolsv.exe",
            "svchost.exe",
            "svchost.exe",
            "svchost.exe ",
            "System",
            "UNS.exe",
            "wininit.exe",
            "WmiApSrv.exe",
            "WmiPrvSE.exe",
            "wmpnetwk.exe",
            "WUDFHost.exe",
    };
}
