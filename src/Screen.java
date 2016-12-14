import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {
    
    public static int W = 1920;
    public static int H = 1080;
    public static int TASKBAR_HEIGHT = 0;
    private static Rectangle2D rect;
    private static Robot robot;
    
    public static BufferedImage BUFF_SCREEN;
    public static Image IMAGE_SCREEN;
    
    public static void init() {
        TASKBAR_HEIGHT = (int) Math.round(1.5 * Math.abs(H - javafx.stage.Screen.getPrimary().getVisualBounds().getHeight()));
        
        try {
            robot = new Robot();
        } catch(Exception e) {
            e.printStackTrace();
        }
        updateBounds();
    }
    
    public static void updateBounds() {
        rect = javafx.stage.Screen.getPrimary().getBounds();
        W = (int) Math.round(rect.getWidth());
        H = (int) Math.round(rect.getHeight());
    }
    
    public static BufferedImage getBufferedImage() {
        return robot.createScreenCapture(new Rectangle(0, 0, Screen.W, Screen.H));
    }
    
    public static Image getImage() {
        return SwingFXUtils.toFXImage(getBufferedImage(), new WritableImage(Screen.W, Screen.H));
    }
    
}
