package observer;

public class Main
{
    public static void main(String[] args){
        ScreenViewServer listen = new ScreenViewServer(11937);
        listen.start();
    }
}
