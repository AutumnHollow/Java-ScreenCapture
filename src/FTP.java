import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

public class FTP {
    
    private static FTPClient client;
    private static String hostname;
    private static String username;
    private static String password;
    public static String domain;
    private static boolean isConnected = false;
    
    public static void init() {
        client = new FTPClient();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("settings.txt")));
            
            hostname = reader.readLine();
            username = reader.readLine();
            password = reader.readLine();
            domain = reader.readLine();
            
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void connect() {
        try {
            client.connect(hostname);
            client.login(username, password);
            isConnected = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean delete(String pathname) {
        if(!isConnected) connect();
        
        try {
            return client.deleteFile(pathname);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static FTPFile[] listFiles(String path) {
        if(!isConnected) connect();
        
        try {
            return client.listFiles(path);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void disconnect() {
        try {
            client.disconnect();
            isConnected = false;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void store(String remote, File file) {
        if(!isConnected) connect();
        
        try {
            client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            client.storeFile(remote, new FileInputStream(file));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
