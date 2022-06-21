package rs.raf.demo;

import java.util.ArrayList;
import java.util.HashMap;

public class Skladiste {

    public static HashMap<String, Skladiste> skladiste = new HashMap<String, Skladiste>();
    public static int[] brojac = new int[15];
    private ArrayList<String> lista;

    public Skladiste() {
        lista = new ArrayList<>();
    }

    public ArrayList<String> getLista() {
        return lista;
    }
}
