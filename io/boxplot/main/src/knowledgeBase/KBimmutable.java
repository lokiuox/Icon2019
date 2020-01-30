package knowledgeBase;

import org.jpl7.Term;
import query.QueryCreator;
import streetElements.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Definisce la KB immutabile nel corso dell'esecuzione
 * che sarà di base per tutte le altre auto
 *
 * Tipo di classe Singleton
 */

public class KBimmutable implements KnowledgeBase {

    private static KBimmutable single_instance = null;

    // variable of type String
    private String kbstart;
    private QueryCreator query;

    // private constructor restricted to this class itself
    private KBimmutable(String kbfilename)
    {
        //TODO check if kbfilename exist

        query = new QueryCreator(kbfilename);

        //Check exception



    }

    // static method to create instance of Singleton class
    public static KBimmutable getInstance(String kbfilename)
    {
        if (single_instance == null)
            single_instance = new KBimmutable(kbfilename);

        return single_instance;
    }


    public ArrayList<Street> getNodes(){
        String va[] = {"X"};
        String temp;
        query.setPredicate("strada",va);

        ArrayList<Street> strade = new ArrayList<>();
        ArrayList<String> nomiStrade = new ArrayList<>();


        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]*")){
                    nomiStrade.add(temp);
                }

            }
        }

        for (String n : nomiStrade){
            Street s = new Street(n, getLength(n));
            strade.add(s);
        }



        return strade;
    }
    public Map<Integer,String> getCrosses(){
        String va[] = {"X"};
        String temp;
        query.setPredicate("incrocio",va);

        ArrayList<Street> strade = new ArrayList<>();
        ArrayList<String> nomiStrade = new ArrayList<>();


        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]*")){
                    nomiStrade.add(temp);
                }

            }
        }
        return new HashMap<>();
    }
    public Integer getLength(String nomeStrada){
        String[] va = new String[]{nomeStrada,"X"};
        String temp;
        Integer i = -1;
        query.setPredicate("lunghezza",va);

        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]*")){
                     i =  Integer.parseInt(temp);
                }
            }
        }

        return i;
    }
}
