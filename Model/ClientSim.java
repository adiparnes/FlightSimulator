package Model;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientSim {
    public static volatile boolean stop=false;
    private static PrintWriter out;
    private static Socket socket;
    public void Connect(String ip,int port){
        try {
            socket = new Socket(ip, port);
            out=new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connect to server");

    }

    public void Send(String[] data){
        for (String s: data) {
            out.println(s);
            out.flush();
            System.out.println(s);
        }

    }
    public void stop()
    {
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
