package openedPages.server;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Jakub on 06.06.2016.
 */
public class Page {
    private final SimpleStringProperty clientIp;
    private final SimpleStringProperty page;

    public Page(String clientIp, String page)
    {
        this.clientIp = new SimpleStringProperty(clientIp);
        this.page = new SimpleStringProperty(page);
    }
    public String getClientIp() {
        return clientIp.get();
    }
    public void setClientIp(String fName) {
        clientIp.set(fName);
    }

    public String getPage() {
        return page.get();
    }
    public void setPage(String fName) {
        page.set(fName);
    }
}
