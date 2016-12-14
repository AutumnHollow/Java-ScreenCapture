public abstract class NowThread implements Runnable {
    
    public void run() {
        function();
    }
    
    public abstract void function();
    
}