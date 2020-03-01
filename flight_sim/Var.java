package flight_sim;

import java.util.Observable;
import java.util.Observer;

public class Var extends Observable implements Observer {
	double v;
	String name;
	String Loc;

	@Override
	public void update(Observable o, Object arg) {
		Double d=new Double(0);
		if(arg.getClass()==(d.getClass()))
			if(this.v!=(double)arg) {
				this.setV((double) arg);
				this.setChanged();
				this.notifyObservers(arg+"");
			}


	}

	@Override
	public String toString() {
		return this.Loc;
	}

	public Var(double v) {
		this.v=v;
		this.Loc=null;
	}
	public Var() {

	}
	public Var(String loc) {
		super();
		Loc = loc;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		if(this.v!=v) {
			this.v = v;
			setChanged();
			notifyObservers(v);
		}


	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoc() {
		return Loc;
	}
	public void setLoc(String loc) {
		Loc = loc;
	}
}
