import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.net.ftp.FTPFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;


public class Main extends Application {
    
    private static final int APPLICATION_WIDTH = 72;
    private static final int APPLICATION_HEIGHT = 240;
    public static Stage stage;
    private static Scene scene;
    private static Button btnScreen, btnArea, btnDelete, btnClipboard;
    public static boolean isSelectingArea = false;
    
    private static Random random;
    private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    
    public static void main(String[] args) throws Exception {
        random = new Random();
        launch(args);
    }
    
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        
        Logger.init();
        Screen.init();
        Main.stageInit(stage);
        FTP.init();
        Render.init();
    
        Main.stage.setAlwaysOnTop(true);
        
        StackPane root = new StackPane();
        
        GridPane GUI = new GridPane();
        Main.btnArea = new Button();
        btnArea.setBackground(new Background(new BackgroundImage(new Image(new File("images/area.png").toURI().toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        btnArea.setPrefSize(48, 48);
        btnArea.setOnAction(Main.onButtonClickArea());
    
        Main.btnScreen = new Button();
        btnScreen.setBackground(new Background(new BackgroundImage(new Image(new File("images/screen.png").toURI().toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        btnScreen.setPrefSize(48, 48);
        btnScreen.setOnAction(Main.onButtonClickScreen());
        
        Main.btnDelete = new Button();
        btnDelete.setBackground(new Background(new BackgroundImage(new Image(new File("images/delete.png").toURI().toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        btnDelete.setPrefSize(48, 48);
        btnDelete.setOnAction(Main.onButtonClickDeleteAll());
        
        Main.btnClipboard = new Button();
        btnClipboard.setBackground(new Background(new BackgroundImage(new Image(new File("images/clipboard.png").toURI().toString()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        btnClipboard.setPrefSize(48, 48);
        btnClipboard.setOnAction(Main.onButtonClickClipboard());
        
        GUI.setAlignment(Pos.CENTER);
        GUI.setVgap(8);
        GUI.setHgap(8);
        GUI.add(btnScreen, 0, 0);
        GUI.add(btnArea, 0, 1);
        GUI.add(btnClipboard, 0, 2);
        GUI.add(btnDelete, 0, 3);
        
        root.getChildren().addAll(GUI, Render.canvas);
    
        Main.scene = new Scene(root, APPLICATION_WIDTH, APPLICATION_HEIGHT);
        scene.setOnMouseClicked(Mouse.onMouseClick());
        scene.setOnMouseMoved(Mouse.onMouseMove());
        stage.setScene(scene);
        stage.show();
    }
    
    public static void stageInit(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setTitle("Light Weight Java Screen Capture");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setX(Screen.W - APPLICATION_WIDTH);
        primaryStage.setY(Screen.H - (APPLICATION_HEIGHT + Screen.TASKBAR_HEIGHT));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
    }
    
    public static EventHandler<ActionEvent> onButtonClickClipboard() {
        return event -> {
            clipboard(Logger.strings.get(Logger.strings.size() - 1));
        };
    }
    
    public static EventHandler<ActionEvent> onButtonClickArea() {
        return event -> {
            Screen.BUFF_SCREEN = Screen.getBufferedImage();
            Screen.IMAGE_SCREEN = SwingFXUtils.toFXImage(Screen.BUFF_SCREEN, new WritableImage(Screen.BUFF_SCREEN.getWidth(), Screen.BUFF_SCREEN.getHeight()));
            
            Mouse.isSelecting = false;
            Render.canvas.setVisible(true);
            Main.isSelectingArea = true;
            Main.stage.setFullScreen(true);
        };
    }
    
    public static EventHandler<ActionEvent> onButtonClickDeleteAll() {
        return event -> {
            Thread nowThread = new Thread(new NowThread() {
                public void function() {
                    FTPFile[] files = FTP.listFiles("wwwhome/");
                    
                    for(FTPFile file : files) {
                        if(!file.getName().contains(".png")) continue;
                        FTP.delete("wwwhome/" + file.getName());
                    }
                    
                    Logger.strings.clear();
                    Logger.flush();
                }
            });
            nowThread.start();
        };
    }
    
    public static File getRandomImageFile() {
        return new File(getRandomString(10) + ".png");
    }
    
    public static EventHandler<ActionEvent> onButtonClickScreen() {
        return event -> {
            Thread nowThread = new Thread(new NowThread() {
                public void function() {
                    uploadImage(getRandomImageFile(), Screen.getBufferedImage());
                }
            });
            nowThread.start();
        };
    }
    
    public static void clipboard(String string) {
        StringSelection selection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    
    public static void uploadImage(File file, BufferedImage image) {
        try {
            ImageIO.write(image, "png", file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    
        FTP.connect();
        FTP.store("wwwhome/" + file.getName(), file);
        FTP.disconnect();
    
        String URL = FTP.domain + file.getName();
        Logger.log(URL);
        Logger.flush();
    
        clipboard(URL);
    
        delete(file);
    }
    
    private static void delete(File file) {
        System.gc();
        Thread thread = new Thread(new NowThread() {
            public void function() {
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                System.gc();
                file.delete();
            }
        });
        thread.start();
    }
    
    private static String getRandomString(int length) {
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < length; i++) {
            builder.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }
        
        return builder.toString();
    }
    
}
