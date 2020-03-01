package Model;

import com.sun.org.apache.xpath.internal.operations.Mod;
import flight_sim.Parser;
import flight_sim.ParserAutoPilot;
import flight_sim.ParserMain;
import server_side.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    private ClientSim clientSim;
    public static volatile boolean stop=false;
    public static volatile boolean turn=false;
    private Interpter interpter;
    private static Socket socketpath;
    private  static PrintWriter outpath;
    private  static BufferedReader in;
    double startX;
    double startY;
    double planeX;
    double planeY;
    double markX;
    double markY;
    int[][] data;
    String[] solution;
    double offset;
    double currentlocationX;
    double currentlocationY;
    double currentHeading;

    public Model() {
        clientSim=new ClientSim();
        interpter=new Interpter();
    }

    public void GetPlane(double startX,double startY, double offset){
        this.offset=offset;
        this.startX=startX;
        this.startY=startY;
            new Thread(()->{
                Socket socket = null;
                try {
                    socket = new Socket("127.0.0.1", 5402);
                    System.out.println("Connect to server");
                    PrintWriter out=new PrintWriter(socket.getOutputStream());
                    BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(!stop){
                        out.println("dump /position");
                        out.flush();
                        String line;
                        ArrayList<String> lines = new ArrayList<>();
                        while (!(line = br.readLine()).equals("</PropertyList>")){
                            if(!line.equals(""))
                                lines.add(line);
                        }
                        String longtitude = lines.get(2);
                        String latitude = lines.get(3);
                        String[] x=longtitude.split("[<>]");
                        String[] y=latitude.split("[<>]");
                        br.readLine();
                        out.println("get /instrumentation/heading-indicator/indicated-heading-deg");
                        out.flush();
                        String[] h=br.readLine().split(" ");
                        int tmp=h[3].length();
                        currentlocationX=Double.parseDouble(x[2]);
                        currentlocationY=Double.parseDouble(y[2]);
                        currentHeading=Double.parseDouble(h[3].substring(1,tmp-1));
                        String[] data={"plane",x[2],y[2],h[3].substring(1,tmp-1)};
                        this.setChanged();
                        this.notifyObservers(data);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }).start();
        }

    public void parse(String[] script){
        interpter.interpet(script);
    }

    public void execute()
    {
        interpter.execute();
    }

    public void stopAutoPilot()
    {
        interpter.stop();
    }

    public void connectPath(String ip,int port){
        try {
            socketpath=new Socket(ip,port);
            outpath=new PrintWriter(socketpath.getOutputStream());
            in=new BufferedReader(new InputStreamReader(socketpath.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectManual(String ip,int port){
        clientSim.Connect(ip,port);
    }

    public void send(String[] data)
    {
        clientSim.Send(data);
    }

    public void findPath(int planeX,int planeY,int markX,int markY,int[][] data)
    {
        this.planeX=planeX;
        this.planeY=planeY;
        this.markX=markX;
        this.markY=markY;
        this.data=data;
        new Thread(()->{

                int j,i;
                System.out.println("\tsending problem...");
                for (i = 0; i < data.length; i++) {
                    System.out.print("\t");
                    for (j = 0; j < data[i].length - 1; j++) {
                        outpath.print(data[i][j] + ",");
                    }
                    outpath.println(data[i][j]);
                }
                outpath.println("end");
                outpath.println(planeX+","+planeY);
                outpath.println(markX+","+markY);
                outpath.flush();
                String usol = null;
                try {
                    usol = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\tsolution received");
                System.out.println(usol);
                String[]tmp=usol.split(",");
                this.solution=tmp;
                String[] notfiy=new String[tmp.length+1];
                notfiy[0]="path";
                for(i=0;i<tmp.length;i++)
                    notfiy[i+1]=tmp[i];
                this.setChanged();
                this.notifyObservers(notfiy);
                Model.turn=true;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.route();
        }).start();
    }

    public void route()
    {
        new Thread(()->{
            ArrayList<String[]> intersection=new ArrayList<>();
            int count=0;
            double intersectionX=0;
            double intersectionY=0;
            double alt=2000;
            int x=(int)planeX,y=(int)planeY;
            double longtitude=startY+(planeX-1)*offset*(-1);
            double latitude=startX+(planeY-1)*offset;
            markX=startY+(markX-1)*offset;
            markY=startX+(markY-1)*offset;
            double heading = 0;
            boolean flag=true;
            int destheading=0;
            Model.turn=false;
            for(int i=0;i<solution.length-1;i++)
            {
                if(solution[i].equals(solution[i+1]))
                {
                    count++;
                }
                else
                {
                    String[] tmp=new String[2];
                    tmp[0]=solution[i];
                    tmp[1]=count+1+"";
                    intersection.add(tmp);
                    count=0;

                }
            }
            if(count!=0)
            {
                String[] tmp=new String[2];
                tmp[0]=solution[solution.length-1];
                tmp[1]=count+1+"";
                intersection.add(tmp);
            }
            ParserMain.symTbl.get("goal").setV(0);
            int i=0;
            while(!Model.turn&&i<intersection.size())
            {
                if(flag) {
                    switch (intersection.get(i)[0]) {
                        case "Down":
                            heading = 180;
                            intersectionY = latitude - Double.parseDouble(intersection.get(i)[1]) * offset;
                            intersectionX=longtitude;
                            alt=data[(y- Integer.parseInt(intersection.get(i)[1]))][x];
                            destheading=3;
                            break;
                        case "Up":
                            heading = 0;
                            intersectionY = latitude + Double.parseDouble(intersection.get(i)[1]) * offset;
                            intersectionX=longtitude;
                            destheading=1;
                            alt=data[(y+ Integer.parseInt(intersection.get(i)[1]))][x];
                            break;
                        case "Left":
                            heading = 270;
                            intersectionX = longtitude - Double.parseDouble(intersection.get(i)[1]) * offset;
                            intersectionY=latitude;
                            destheading=4;
                            alt=data[y][(x+ Integer.parseInt(intersection.get(i)[1]))];
                            break;
                        case "Right":
                            heading = 90;
                            intersectionX = longtitude + Double.parseDouble(intersection.get(i)[1]) * offset;
                            intersectionY=latitude;
                            destheading=2;
                            alt=data[y][(x- Integer.parseInt(intersection.get(i)[1]))];
                            break;
                    }
                    if(Math.abs(alt-ParserMain.symTbl.get("alt").getV())>500)
                    {
                        double tmp=15/100;
                        if (ParserMain.symTbl.get("alt").getV() < alt )
                            ParserMain.symTbl.get("p").setV(tmp);
                        else
                            ParserMain.symTbl.get("p").setV(-tmp);
                    }
                }


                if(currentHeading>=0 && currentHeading<90)
                {

                    if(destheading==1 ||destheading==4){
                        makeTurn(this::turnMinus,heading,currentHeading);

                    }
                    else
                        makeTurn(this::turnPlus,heading,currentHeading);

                }
                else if(currentHeading>=90 && currentHeading<180)
                {

                    if(destheading==1 ||destheading==2){
                        makeTurn(this::turnMinus,heading,currentHeading);

                    }
                    else
                        makeTurn(this::turnPlus,heading,currentHeading);

                }
                else if(currentHeading>=180 && currentHeading<270)
                {

                    if(destheading==2 ||destheading==3){
                        makeTurn(this::turnMinus,heading,currentHeading);

                    }
                    else
                        makeTurn(this::turnPlus,heading,currentHeading);

                }
                else if(currentHeading>=270 && currentHeading<360)
                {

                    if(destheading==3 ||destheading==4){
                        makeTurn(this::turnMinus,heading,currentHeading);

                    }
                    else
                        makeTurn(this::turnPlus,heading,currentHeading);

                }




                flag=false;
                while(Math.abs(currentlocationX-intersectionY)>offset*5 &&Math.abs(currentlocationY-intersectionX)>offset*5&&!Model.turn)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                flag=true;
                i++;
                latitude=currentlocationY;
                longtitude=currentlocationX;

            }
            ParserMain.symTbl.get("goal").setV(1);

    }).start();
    }

    public interface Turn {
     double Do(double x);
    }

    public void makeTurn(Turn t,double heading,double currentHeading)
    {
        double minus=Math.abs(heading-currentHeading);
        while(minus>30 && minus < 335 &&!Model.turn)
        {
            double tmp=t.Do(currentHeading);
            ParserMain.symTbl.get("hroute").setV(tmp);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            minus=Math.abs(heading-currentHeading);
        }
        ParserMain.symTbl.get("hroute").setV(heading);
    }
    public double turnPlus(double currentHeading)
    {
        int tmp=(int)currentHeading+25;
        if(tmp>360)
            tmp-=360;
        return tmp;
    }
    public double turnMinus(double currentHeading)
    {
        int tmp=(int)currentHeading-25;
        if(tmp<0)
            tmp=360+tmp;
        return  tmp;
    }

    public void stopAll()
    {
        Model.stop=true;
        if (outpath!=null)
         outpath.close();
        try {
            if (in!=null)
                in.close();
            if (socketpath!=null)
                socketpath.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientSim.stop();
        ParserAutoPilot.close=true;
        Model.turn=true;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
