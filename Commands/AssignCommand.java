package Commands;

import Experssions.ShuntingYard;
import flight_sim.ParserMain;

public class AssignCommand implements Command {
    @Override
    public void doCommand(String[] array) {
        if (array[2].equals("bind")) {
            if(ParserMain.symTbl.get(array[0]).getV()!=ParserMain.symTbl.get(array[3]).getV())
                ParserMain.symTbl.get(array[0]).setV(ParserMain.symTbl.get(array[3]).getV());
            ParserMain.symTbl.get(array[3]).addObserver(ParserMain.symTbl.get(array[0]));
            ParserMain.symTbl.get(array[0]).addObserver(ParserMain.symTbl.get(array[3]));

        }
        else {
            StringBuilder exp = new StringBuilder();
            for (int i = 2; i < array.length; i++)
                exp.append(array[i]);
            double tmp=ShuntingYard.calc(exp.toString());
            if(ParserMain.symTbl.get(array[0]).getLoc()!=null) {
                ConnectCommand.out.println("set " + ParserMain.symTbl.get(array[0]).getLoc() + " " + tmp);
                ConnectCommand.out.flush();
                System.out.println("set " + ParserMain.symTbl.get(array[0]).getLoc() + " " + tmp);
            }
            else
                ParserMain.symTbl.get(array[0]).setV(tmp);
        }
    }
}
