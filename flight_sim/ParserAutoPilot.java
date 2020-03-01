package flight_sim;


import java.util.ArrayList;

public class ParserAutoPilot {
    ParserMain p;
    public static volatile boolean stop=true;
    public static volatile boolean close=false;
    public static Thread  exe;
    public int i = 0;
    public ParserAutoPilot(ParserMain p) {
        this.p = p;


    }
    public void parse() {
        p.parse();
        i=0;
    }

    public void execute(){

        exe=new Thread(()->{
            while(!close) {
                while (!stop && i < p.comds.size()) {
                    p.comds.get(i).calculate();
                    i++;
                }
            }
        });

        exe.start();

    }

    public void add(ArrayList<String[]> lines){
        p.lines.clear();
        p.lines.addAll(lines);
        /*
        Planted value in the symbol table and the while condition which allows to stop loops when the auto pilot is off
         */
        ParserMain.symTbl.put("stop",new Var(1));
        for (String[] s:p.lines) {
            if (s[0].equals("while"))
            {
                StringBuilder tmp=new StringBuilder(s[s.length-2]);
                tmp.append("&&stop!=0");
                s[s.length-2]=tmp.toString();
            }
        }
    }
    public void stop(){
        Var v= ParserMain.symTbl.get("stop");
        if(v!=null)
            v.setV(0);
        ParserAutoPilot.stop=true;
    }
    public void Continue()
    {
        ParserMain.symTbl.get("stop").setV(1);
    }
}
