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
    private QueryCreator query = new QueryCreator();

    // private constructor restricted to this class itself
    private KBimmutable(String kbfilename)
    {
        //check if kbfilename exist

        kbstart = "consult('"+kbfilename+"')";
        query.setPredicate(kbstart);
        query.getResult();
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
        query.setPredicate("strada(X)");

        ArrayList<Street> strade = new ArrayList<>();
        ArrayList<String> nomiStrade = new ArrayList<>();


        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                if(val.getValue().toString().compareTo("_2") != 0){
                    nomiStrade.add(val.getValue().toString());
                }

            }
        }

        for (String n : nomiStrade){

            /*query.setPredicate("lunghezza("+n+",?)");
            HashMap<String, Term>[] res = (HashMap<String, Term>[]) query.getResults();
            HashMap<String, Term> resSingolo = (HashMap<String, Term>[]) res.entrySet();*/
            Street s = new Street(n, 1);
            strade.add(s);
        }



        return strade;
    }
    public Map<Integer,String> getCrosses(){ return new HashMap<>();}
    public Integer getLength(){ return 1;}
}
