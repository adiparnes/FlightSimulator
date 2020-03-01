package flight_sim;

import java.util.ArrayList;
import java.util.Scanner;

public class LexerSim<V> implements Lexer{

	private Scanner scan;
	private ArrayList<String[]> lines = new ArrayList<>();

	public LexerSim(String v) {
		scan = new Scanner(v);
	}
	public LexerSim(V v) {
		scan = new Scanner((Readable) v);
		
	}
	public ArrayList<String[]> lex() {
		while (scan.hasNextLine()) {
			lines.add(scan.nextLine().split(" "));
		}
		return lines;

	}

}
