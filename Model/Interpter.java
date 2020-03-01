package Model;

import flight_sim.*;

public class Interpter {
    ParserAutoPilot parser;
    Lexer lexer;

    public Interpter() {
        String[] start= {
                "openDataServer 5400 10",
                "connect 127.0.0.1 5402",
                "var breaks = bind /controls/flight/speedbrake",
                "var throttle = bind /controls/engines/current-engine/throttle",
                "var heading = bind /instrumentation/heading-indicator/indicated-heading-deg",
                "var airspeed = bind /instrumentation/airspeed-indicator/indicated-speed-kt",
                "var roll = bind /instrumentation/attitude-indicator/indicated-roll-deg",
                "var pitch = bind /instrumentation/attitude-indicator/internal-pitch-deg",
                "var rudder = bind /controls/flight/rudder",
                "var aileron = bind /controls/flight/aileron",
                "var elevator = bind /controls/flight/elevator",
                "var alt = bind /instrumentation/altimeter/indicated-altitude-ft",
                "var rpm = bind /engines/engine/rpm",
                "var hroute = 0",
                "var goal = 0",
                "var altr = 2000",
                "var p=0"

        };
        lexer=new LexerMain(start);
        parser=new ParserAutoPilot(new ParserMain(lexer.lex()));
        parser.parse();
        ParserAutoPilot.stop=false;
        parser.execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParserAutoPilot.stop=true;
    }

    public void interpet(String[] list){
       lexer=new LexerMain(list);
       parser.add(lexer.lex());
       parser.parse();
   }
   public void execute()
   {
       //when execute its checked if it isn't new script - return and continue executing from the last command
       if(parser.i!=0)
           parser.i--;
       parser.Continue();
       ParserAutoPilot.stop=false;

   }
   public void stop(){
        parser.stop();
   }
}
