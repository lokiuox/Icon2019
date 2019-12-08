import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

//javac -cp /usr/lib/swipl/lib/jpl.jar test.java

public class test{
    public static void main(String[] args){

            String s = new String("consult('./test.pl')");
            Query q1 =  new Query(s);
            //ricerca KB
            if(q1.hasSolution()){
                //start questions
                s = "child_of(joe, X)";
                q1 = new Query(s);
                Map<String, Term>[] childs_of_joe = q1.allSolutions(); //ritorna array di mappe

                System.out.println("who is child of joe ? ");

                for (Map<String, Term> entry : childs_of_joe) { //itero sulle mappe
                    for (Map.Entry<String,Term> val : entry.entrySet()) //singolo valore
                            System.out.println(val.getKey() + "/" + val.getValue());
                }

            }else{
                System.out.println("no kb provided");
            }
    }
}