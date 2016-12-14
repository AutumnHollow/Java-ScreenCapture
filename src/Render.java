import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Render {
    
    public static Canvas canvas;
    private static long nextRender = 0;
    
    public static void init() {
        canvas = new Canvas(Screen.W, Screen.H);
        canvas.setVisible(false);
    }
    
    public static void render() {
        if(System.currentTimeMillis() < nextRender) return;
        nextRender = System.currentTimeMillis() + 24;
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.drawImage(Screen.IMAGE_SCREEN, 0, 0);
        if(Mouse.isSelecting) {
            context.setFill(Color.WHITE);
            int topX = Mouse.X > Mouse.X_CLICK ? Mouse.X_CLICK : Mouse.X;
            int topY = Mouse.Y > Mouse.Y_CLICK ? Mouse.Y_CLICK : Mouse.Y;
            int botX = Mouse.X > Mouse.X_CLICK ? Mouse.X : Mouse.X_CLICK;
            int botY = Mouse.Y > Mouse.Y_CLICK ? Mouse.Y : Mouse.Y_CLICK;
    
            context.setGlobalAlpha(0.75);
            context.fillRect(topX, topY, Math.abs(topX - botX), Math.abs(topY - botY));
            context.setGlobalAlpha(1);
            context.strokeRect(topX, topY, Math.abs(topX - botX), Math.abs(topY - botY));
        }
    }
    
}
