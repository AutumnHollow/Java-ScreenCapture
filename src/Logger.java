import java.io.*;
import java.util.ArrayList;

public class Logger {
    
    public static ArrayList<String> strings;
    
    public static void init() {
        strings = new ArrayList<>();
        try {
            File file = new File("log.txt");
            if(!file.exists()) file.createNewFile();
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) break;
                if(line.isEmpty()) continue;
                strings.add(line);
            }
            
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void log(String string) {
        System.out.println(string);
        strings.add(string);
    }
    
    public static void flush() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("log.txt")));
            
            for(String string : strings) {
                writer.append(string);
                writer.newLine();
            }
            
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void close() {
        flush();
    }
    
}
