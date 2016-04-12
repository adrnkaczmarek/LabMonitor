package processLib;

public class ProcessModel {
    private final String imageName;
    private final String sessionName;

    public ProcessModel (String iName, String sName) {
        this.imageName = new String(iName);
        this.sessionName = new String(sName);
    }

    public String getImageName() {
        return imageName;
    }

    public String getSessionName() {
        return sessionName;
    }
}
