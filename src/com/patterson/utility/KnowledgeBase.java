package com.patterson.utility;

import org.jpl7.Query;
import org.jpl7.Term;

import java.util.*;

public class KnowledgeBase {
    Set<String> assertions = new HashSet<>();

    static public void init(String file) {
        Query q = new Query("consult('"+ file +"').");
        q.hasSolution();
        q.close();
    }

    public void addAssertion(String s) {
        assertions.add(s);
    }

    public void clearAssertions() {
        assertions.clear();
    }

    public void assertToKB() {
        for (String s: assertions) {
            (new Query("assert("+ s +").")).hasSolution();
        }
    }

    public void retractFromKB() {
        for (String s: assertions) {
            (new Query("retract("+ s +").")).hasSolution();
        }
    }

    public boolean boolQuery(String q) {
        return (new Query(q)).hasSolution();
    }

    public  void list() {
        for (String s: assertions)
            System.out.println(s);
    }

    public Set<Map<String, String>> stringQuery(String q) {
        Set<Map<String, String>> set = new HashSet<>();

        for (Map<String,Term> res : new Query(q)) {
            set.add(prologToString(res));
        }

        return set;
    }

    private Map<String, String> prologToString (Map<String, Term> prolog) {
        Map<String,String> res = new HashMap<>();

        Set<String> keys = prolog.keySet();
        for(String k: keys) {
            res.put(k, prolog.get(k).name());
        }

        return res;
    }
}
