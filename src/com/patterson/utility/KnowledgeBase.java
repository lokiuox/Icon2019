package com.patterson.utility;

import org.jpl7.Compound;
import org.jpl7.Query;
import org.jpl7.Term;

import java.util.*;

public class KnowledgeBase {
    Set<String> assertions = new HashSet<>();

    public static void init(String file) {
        suppressWarnings();
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

    public static boolean boolQuery(String q) {
        return (new Query(q)).hasSolution();
    }

    public void list() {
        for (String s: assertions)
            System.out.println(s);
    }

    public static Set<Map<String, String>> stringQuery(String q) {
        Set<Map<String, String>> set = new HashSet<>();

        for (Map<String,Term> res : new Query(q)) {
            Map<String, String> temp = prologToString(res);
            if(!temp.isEmpty())
                set.add(prologToString(res));
        }

        return set;
    }

    public static Map<String,List<String>> listQuery(String q) {
        Map<String,List<String>> set = new HashMap<>();

        Map<String,Term> res = (new Query(q)).oneSolution();
        Set<String> keys = res.keySet();
        for (String k : keys) {
            List<String> stringList = new ArrayList<>();
            if (res.get(k).typeName().equals("Compound")) {
                Term[] termList = res.get(k).toTermArray();
                for (Term t : termList) {
                    stringList.add(t.name());
                }
            } else if (res.get(k).typeName().equals("Atom")) {
                stringList.add(res.get(k).name());
            } else if (res.get(k).typeName().equals("Float") || res.get(k).typeName().equals("Integer")) {
                stringList.add(String.valueOf(res.get(k)));
            }
            set.put(k,stringList);
        }

        return set;
    }

    private static Map<String, String> prologToString (Map<String, Term> prolog) {
        Map<String,String> res = new HashMap<>();

        Set<String> keys = prolog.keySet();
        for(String k: keys) {
            String temp = "";
            try{
                temp = prolog.get(k).name();
                if(!temp.matches("_[0-9]+")){
                    res.put(k, temp);
                }
            }catch (Exception e){
                temp = String.valueOf(prolog.get(k));
                res.put(k, temp);
                }
            }



        return res;
    }

    private static void suppressWarnings() {
        Query q = new Query("style_check(-discontiguous), style_check(-singleton).");
        q.hasSolution();
        q.close();
    }
}
