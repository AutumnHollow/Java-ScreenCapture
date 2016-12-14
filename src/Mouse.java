import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mouse {
    
    public static int X = 0;
    public static int Y = 0;
    public static int X_CLICK = -1;
    public static int Y_CLICK = -1;
    public static boolean isSelecting = false;
    
    public static void click() {
        isSelecting = !isSelecting;
        if(isSelecting) {
            X_CLICK = X;
            Y_CLICK = Y;
        } else {
            int minX = Math.min(X_CLICK, X);
            int minY = Math.min(Y_CLICK, Y);
            int maxX = Math.max(X_CLICK, X);
            int maxY = Math.max(Y_CLICK, Y);
    
            Thread nowThread = new Thread(new NowThread() {
                public void function() {
                    BufferedImage tmp = new BufferedImage(maxX - minX, maxY - minY, BufferedImage.TYPE_INT_RGB);
    
                    for(int x = 0; x < tmp.getWidth(); x++) {
                        for(int y = 0; y < tmp.getHeight(); y++) {
                            tmp.setRGB(x, y, Screen.BUFF_SCREEN.getRGB(minX + x, minY + y));
                        }
                    }
                    Main.uploadImage(Main.getRandomImageFile(), tmp);
                }
            });
            nowThread.start();
            Main.isSelectingArea = false;
            Render.canvas.setVisible(false);
            Main.stage.setFullScreen(false);
        }
    }
    
    public static void updatePos() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        X = (int) Math.round(point.getX());
        Y = (int) Math.round(point.getY());
        
        if(Main.isSelectingArea) Render.render();
    }
    
    public static EventHandler<MouseEvent> onMouseMove() {
        return event -> Mouse.updatePos();
    }
    
    public static EventHandler<MouseEvent> onMouseClick() {
        return event -> Mouse.click();
    }
    
}
