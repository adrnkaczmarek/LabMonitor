package library;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {

    public static BufferedImage getScreenshot(Dimension dimension) throws AWTException {
        Rectangle snapShot = new Rectangle(new Dimension(1366,768));

        BufferedImage buffimg = (new Robot())
                .createScreenCapture(snapShot);

        BufferedImage tag= new BufferedImage((int)dimension.getWidth(),
                (int)dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

        tag.getGraphics().drawImage(buffimg.getScaledInstance((int)dimension.getWidth(),
                (int)dimension.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
        return tag;
    }

    public static Dimension getScreenDimension(double divider){
        Dimension toReturn = new Dimension();
        double width =  Toolkit.getDefaultToolkit().getScreenSize().getWidth()/divider;
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/divider;
        toReturn.setSize( width, height);

        return toReturn;
    }
}
