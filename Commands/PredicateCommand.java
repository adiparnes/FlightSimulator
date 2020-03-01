package Commands;

import Experssions.CoditionBulider;
import Experssions.ShuntinYardPre;
import flight_sim.ParserMain;
import flight_sim.Var;

import java.util.function.Predicate;

public class PredicateCommand implements Command {
    double bool;

    public double getBool() {
        return bool;
    }

    public void setBool(double bool) {
        this.bool = bool;
    }

    @Override
    public void doCommand(String[] array) {
        StringBuilder s=new StringBuilder();
        for(int i=1;i<array.length-1;i++){
            s.append(array[i]);
        }
        bool= CoditionBulider.calc(s.toString());
    }
}
