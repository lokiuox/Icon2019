package query;

import helper.StringUtils;
import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isUpperCase;

public class QueryCreator implements QueryDefiner{

    int nSolutions = 0;
    String predicate = "",baseName = "";
    Query query;
    Boolean resultBoolean;
    private Object result;
    Boolean is_valid = false;

    public QueryCreator(String filename){
        baseName = filename;
        setKB();

    }

    private void setKB(){
        query = new Query(
                "consult",
                new Term[] {new Atom(baseName)}
        );

        System.out.println( "consult " + (query.hasSolution() ? "succeeded" : "failed"));

    }


    public void setPredicate(String predicate,String[] atoms) {
        this.is_valid = false;

        Term[] TermArray = new Term[atoms.length];
        int length = 0;

        for (String atom : atoms){
            if(StringUtils.isAllUpperCase(atom)){
                TermArray[length] = new Variable(atom);
                length ++;
            }else if(StringUtils.isAllLowerCase(atom)){
                TermArray[length] = new Atom(atom);
                length ++;
            }else{
                System.err.println("ne atomo ne variabile");
                //throw new Exception("ne atomo ne variabile");
            }
        }

        query = new Query(predicate, TermArray);
        this.predicate = predicate;
    }

    public void setPredicate(String predicate) {
        this.is_valid = false;
        query = new Query(predicate);
        this.predicate = predicate;
    }

    public Boolean getBoolean(){
        if(is_valid && !predicate.isEmpty()){ return resultBoolean;}
        this.is_valid = true;
        return resultBoolean = query.hasSolution();
    }

    public HashMap<String, Term> getResult(){
        if(is_valid && !predicate.isEmpty() && this.nSolutions == 1){ return (HashMap<String, Term>)  this.result;}
        this.is_valid = true;

        this.nSolutions = query.allSolutions().length;

        if(this.nSolutions > 1) {
            System.out.println("Attention one solution returned, but there are others");
            this.result = query.nextSolution();
            return (HashMap<String, Term>) this.result;

        }
        this.result = query.oneSolution();
        return (HashMap<String, Term>) this.result;
    }


    public HashMap<String, Term>[] getResults(){
        if(is_valid && !predicate.isEmpty() && this.nSolutions > 1){ return (HashMap<String, Term>[])  this.result;}
        this.is_valid = true;

        this.nSolutions = query.allSolutions().length;

        if(this.nSolutions == 1) {
            //System.out.println("Attention only one solution available");
            this.result = query.allSolutions();
            return (HashMap<String, Term>[]) this.result;

        }
        this.result = query.allSolutions();
        return (HashMap<String, Term>[]) this.result;
    }
}
