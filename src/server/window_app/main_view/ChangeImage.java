package server.window_app.main_view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChangeImage implements Runnable{

    private ImageView image;

    public ChangeImage( ImageView img ){
        this.image = img;
    }

    public void setImage(Image img){
        this.image.setImage(img);
    }

    @Override
    public void run() {}
}
