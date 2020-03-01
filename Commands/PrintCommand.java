package Commands;

import flight_sim.ParserMain;

public class PrintCommand implements Command {
    @Override
    public void doCommand(String[] array) {
       for (int i=1;i<array.length;i++)
       {
           if(ParserMain.symTbl.containsKey(array[i]))
                System.out.print(array[i]+ParserMain.symTbl.get(array[i]).getV());
           else
               System.out.print(array[i]);
       }
        System.out.println("");
    }
}
