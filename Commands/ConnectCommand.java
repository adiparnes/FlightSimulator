package Commands;


import flight_sim.ParserMain;
import flight_sim.Var;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectCommand implements Command ,Observer{
	public static volatile boolean stop=false;
	public static PrintWriter out;
	@Override
	public void update(Observable o, Object arg) {
		if(arg.getClass()==String.class) {
			out.println("set " + o.toString() + " " + arg);
			out.flush();
			System.out.println("set " + o.toString() + " " + arg);
		}
	}

	@Override
	public void doCommand(String[] array) {
		stop=false;
		new Thread(()->{
			try {
				Socket socket= null;
				try {
					synchronized (OpenDataServer.wait) {
						OpenDataServer.wait.wait();
					}
					Thread.sleep(10000);
					socket = new Socket(array[1], Integer.parseInt(array[2]));
					System.out.println("Connect to server");
					out=new PrintWriter(socket.getOutputStream());
					/*
					for (int i=17;i<ParserMain.vars.size();i++) {
						String str=ParserMain.vars.get(i);
						if(!str.contains("rpm"))
						ParserMain.symTbl.get(str).addObserver(this);
					}
					 */
					while(!stop){


					}

					out.close();
					socket.close();


				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();


	}

}
