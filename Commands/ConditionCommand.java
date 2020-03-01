package Commands;

import flight_sim.Var;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ConditionCommand implements Command {

	protected ArrayList<CommandExpression> commands;

	public ArrayList<CommandExpression> getCommands() {
		return commands;
	}
	public void setCommands(ArrayList<CommandExpression> commands) {
		this.commands = commands;
	}

	@Override
	public void doCommand(String[] array) {
		// TODO Auto-generated method stub
		//return 0;
	}

}
