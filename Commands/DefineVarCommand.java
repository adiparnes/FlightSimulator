package Commands;

import Experssions.ShuntingYard;
import flight_sim.ParserMain;
import flight_sim.Var;

public class DefineVarCommand implements Command {

	@Override
	public void doCommand(String[] array) {
		Var v=new Var();
		if(array.length>2) {
			if (array[3].equals("bind")) {
				/*
				if(v.getV()!=ParserMain.symTbl.get(array[4]).getV())
					v.setV(ParserMain.symTbl.get(array[4]).getV());
				ParserMain.symTbl.get(array[4]).addObserver(v);
				//v.addObserver(ParserMain.symTbl.get(array[4]));
				v.setLoc(ParserMain.symTbl.get(array[4]).getLoc());
				 */
				//v.setLoc(ParserMain.symTbl.get(array[4]).getLoc());
				ParserMain.symTbl.put(array[1],ParserMain.symTbl.get(array[4]));
			}
			else {
				StringBuilder exp = new StringBuilder();
				for (int i = 3; i < array.length; i++)
					exp.append(array[i]);
				v.setV(ShuntingYard.calc(exp.toString()));
				ParserMain.symTbl.put(array[1],v);
			}
		}

	}

}
