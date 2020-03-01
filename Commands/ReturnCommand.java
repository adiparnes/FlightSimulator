package Commands;

import Experssions.ShuntingYard;
import flight_sim.ParserMain;

public class ReturnCommand implements Command {

    @Override
    public void doCommand(String[] array) {

        StringBuilder exp = new StringBuilder();
        for (int i = 1; i < array.length; i++)
            exp.append(array[i]);
        ParserMain.returnval = ShuntingYard.calc(exp.toString());
    }

}
