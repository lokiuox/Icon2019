package knowledgeBase;

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
        //check if kbfilename exist

        this.kbstart = "consult('"+kbfilename+"')";
        this.query.setPredicate(kbstart);
        query.getResult();




    }

    // static method to create instance of Singleton class
    public static KBimmutable getInstance(String kbfilename)
    {
        if (single_instance == null)
            single_instance = new KBimmutable(kbfilename);

        return single_instance;
    }


    public ArrayList<Street> getNodes(){
        this.query.setPredicate("incroci(X)");
        System.out.println(query.getResults());

        return new ArrayList<Street>(); }
    public Map<Integer,String> getCrosses(){ return new HashMap<>();}
    public Integer getLength(){ return 1;}
}
