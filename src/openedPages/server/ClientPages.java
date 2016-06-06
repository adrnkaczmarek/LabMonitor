package openedPages.server;
import java.util.ArrayList;
import java.util.List;


public class ClientPages {
    private String clientIp;
    private ArrayList<String> pages;

    public ClientPages(String clientIp)
    {
        this.clientIp = clientIp;
    }

    public ClientPages(String clientIp, String page)
    {
        this.clientIp = clientIp;
        pages = new ArrayList<>();
        pages.add(page);
    }

    public String getClientIp()
    {
        return this.clientIp;
    }
    public void AddPage(String page)
    {
        pages.add(page);
    }

    public ArrayList<String> getPages()
    {
        return this.pages;
    }
}
