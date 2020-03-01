package server_side;

public class boot_server {
    public static void main(String[] args) {
        Server s=new MySerialServer(); // initialize
        CacheManager cm=new FileCacheManager();
        MyClientHandler ch=new MyClientHandler(cm);
        s.open(6688,new ClientHandlerPath(ch));
    }
}
