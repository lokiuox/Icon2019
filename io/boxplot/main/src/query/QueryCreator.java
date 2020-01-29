package query;

import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

public class QueryCreator implements QueryDefiner{

    int nSolutions = 0;
    String predicate = "";
    Query query;
    Boolean resultBoolean;
    private Object result;
    Boolean is_valid = false;

    public QueryCreator(){}


    public void setPredicate(String predicate){
        this.is_valid = false;
        this.predicate = predicate;
    }

    public Boolean getBoolean(){
        if(is_valid && !predicate.isEmpty()){ return resultBoolean;}
        query = new Query(predicate);
        this.is_valid = true;
        return resultBoolean = query.hasSolution();
    }

    public Map<String, Term> getResult(){
        if(is_valid && !predicate.isEmpty() && this.nSolutions == 1){ return (Map<String, Term>)  this.result;}
        query = new Query(predicate);
        this.is_valid = true;

        this.nSolutions = query.allSolutions().length;

        if(this.nSolutions > 1) {
            System.out.println("Attention one solution returned, but there are others");
            this.result = query.nextSolution();
            return (Map<String, Term>) this.result;

        }
        this.result = query.oneSolution();
        return (Map<String, Term>) this.result;
    }


    public Map<String, Term>[] getResults(){
        if(is_valid && !predicate.isEmpty() && this.nSolutions > 1){ return (Map<String, Term>[])  this.result;}
        query = new Query(predicate);
        this.is_valid = true;

        this.nSolutions = query.allSolutions().length;

        if(this.nSolutions == 1) {
            System.out.println("Attention only one solution available");
            this.result = query.oneSolution();
            return (Map<String, Term>[]) this.result;

        }
        this.result = query.allSolutions();
        return (Map<String, Term>[]) this.result;
    }
}
