package Commands;

import Experssions.ShuntingYard;

public class SleepCommand implements Command {
    @Override
    public void doCommand(String[] array) {
        try {
            Thread.sleep((long)ShuntingYard.calc(array[1]));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
