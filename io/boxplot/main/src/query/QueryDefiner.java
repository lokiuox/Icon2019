package query;

import org.jpl7.Term;

import java.util.Map;

public interface QueryDefiner {

    void setPredicate(String predicate);
    Boolean getBoolean();
    Map<String, Term> getResult();
    Map<String, Term>[] getResults();
}
