package flight_sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class LexerMain<V> implements Lexer {
    private Scanner scan;
    private ArrayList<String[]> lines = new ArrayList<>();
    private String[] stringarray=null;

    public LexerMain(String v) {
        try {
            scan = new Scanner(new BufferedReader(new FileReader(v)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public LexerMain(String[] s)
    {
        stringarray=s;
    }
    public LexerMain(V v) {
        scan = new Scanner((Readable) v);

    }
    public ArrayList<String[]> lex() {
        if(stringarray!=null)
        {
            for (String s:stringarray) {

                lines.add(s.replaceFirst("=", " = ").replaceFirst("\t","").split("\\s+"));
            }

        }
        else
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine().replaceFirst("=", " = ").replaceFirst("\t","").split("\\s+"));
            }
        return lines;

    }
}
