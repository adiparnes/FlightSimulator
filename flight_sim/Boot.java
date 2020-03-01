package flight_sim;

import Commands.Command;
import Commands.DefineVarCommand;
import Experssions.ShuntingYard;

import java.util.ArrayList;
import java.util.Collections;

public class Boot {
	
	
	public static void main(String[] args) {
	/*String s="openDataServer 5400 10\r\n" + 
				"connect 127.0.0.1 5402\r\n" + 
				"var breaks = bind \"/controls/flight/speedbrake\"\r\n" + 
				"var throttle = bind \" /controls/engines/current-engine/throttle\"\r\n" + 
				"var heading = bind \"/instrumentation/heading-indicator/offset-deg\"\r\n" + 
				"var airspeed = bind \"/instrumentation/airspeed-indicator/indicated-speed-kt\"\r\n" + 
				"var roll = bind \"/instrumentation/attitude-indicator/indicated-roll-deg\"\r\n" + 
				"var pitch = bind \"/instrumentation/attitude-indicator/internal-pitch-deg\"\r\n" + 
				"var rudder = bind \"/controls/flight/rudder\"\r\n" + 
				"var aileron = bind \"/controls/flight/aileron\"\r\n" + 
				"var elevator = bind \"/controls/flight/elevator\"\r\n" + 
				"var alt = bind \"/instrumentation/altimeter/indicated-altitude-ft\"\r\n" + 
				"breaks = 0\r\n" + 
				"throttle = 1\r\n" + 
				"var h0 = heading\r\n" + 
				"while alt < 1000 {\r\n" + 
				"rudder = (h0 � heading)/20\r\n" + 
				"aileron = - roll / 70\r\n" + 
				"elevator = pitch / 50\r\n" + 
				"print alt\r\n" + 
				"sleep 250\r\n" + 
				"}\r\n" + 
				"print \"done\"";
				*/
		String s="openDataServer 5400 10";
		Lexer lex=new LexerSim(s);
		ArrayList<String[]> tmp=lex.lex();
		Parser p=new ParserMain(tmp);
		ParserMain.symTbl.put("x",new Var(3));
		Command c=new DefineVarCommand();
		String[] str={"var","y","=","bind","x"};
		c.doCommand(str);
		ParserMain.symTbl.get("x").setV(4);
		//p.parse();
		System.out.println(ShuntingYard.calc("x+y"));
		System.out.println("done");

	}

}
