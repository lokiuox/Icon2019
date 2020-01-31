package knowledgeBase;

import org.jpl7.Query;
import org.jpl7.Term;
import query.QueryCreator;
import streetElements.Cross;
import streetElements.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KBmethods {

    public static ArrayList<Street> getNodes(QueryCreator query){
        String va[] = {"X"};
        String temp;
        query.setPredicate("strada",va);

        ArrayList<Street> strade = new ArrayList<>();
        ArrayList<String> nomiStrade = new ArrayList<>();


        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]+")){
                    nomiStrade.add(temp);
                }

            }
        }

        for (String n : nomiStrade){
            Street s = new Street(n, getLength(query,n),getWeight(query,n)); //lunghezza e peso sono uguali
            strade.add(s);
        }



        return strade;
    }
    public static ArrayList<Cross> getCrosses(QueryCreator query){
        ArrayList<Cross> crosses = new ArrayList<>();
        String va[] = {"X"};
        String temp;
        query.setPredicate("incrocio",va);

        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]+")){
                    Cross c = new Cross(temp);
                    ArrayList<String>  conn = getConnection(query,temp);
                    conn.forEach((n) -> c.insertConnection(n));
                    crosses.add(c);
                }

            }
        }
        return crosses;
    }
    public static Integer getLength(QueryCreator query,String nomeStrada){
        String[] va = new String[]{nomeStrada,"X"};
        String temp;
        Integer i = -1;
        query.setPredicate("lunghezza",va);

        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]+")){
                    i =  Integer.parseInt(temp);
                }
            }
        }

        return i;
    }

    public static Integer getWeight(QueryCreator query,String nomeStrada){
        String[] va = new String[]{nomeStrada,"X"};
        String temp;
        Integer i = -1;
        query.setPredicate("peso",va);

        query.getBoolean();

        for (HashMap<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]+")){
                    i =  Integer.parseInt(temp);
                }
            }
        }

        return i;
    }

    public static ArrayList<String> getConnection(QueryCreator query,String nomeIncrocio){


        String[] va = new String[]{nomeIncrocio,"X","Y"};
        ArrayList<String> connessioni = new ArrayList<>();
        String temp;
        query.setPredicate("collega",va);

        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            Term t = entry.get("X");
            temp = t.toString();
            if(!temp.matches("_[0-9]+")){
                if(!connessioni.contains(temp)){
                    connessioni.add(temp);
                }
            }
        }

        return connessioni;
    }
}
