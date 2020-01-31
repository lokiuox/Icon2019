package knowledgeBase;

import org.jpl7.Term;
import query.QueryCreator;
import streetElements.Cross;
import streetElements.Street;

import java.util.ArrayList;
import java.util.Map;

/**
 * Definisce la KB immutabile nel corso dell'esecuzione
 * che sarà di base per tutte le altre auto
 *
 * Tipo di classe Singleton
 */

public class KBimmutable  implements  KnowledgeBase{

    private static KBimmutable single_instance = null;

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
         return KBmethods.getNodes(query);
    } //ritorna mappa nome nodo, peso, lunghezza , si potrebbe trasformare in abstract
    public ArrayList<Cross> getCrosses(){
        return KBmethods.getCrosses(query);
    }
    public Integer getLength(String nomeStrada){
        return KBmethods.getLength(query,nomeStrada);
    }
    public ArrayList<String> getConnection(String nomeIncrocio){
        return KBmethods.getConnection(query,nomeIncrocio);
    }







}
