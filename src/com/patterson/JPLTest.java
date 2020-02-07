package com.patterson;
import org.jpl7.Query;
import org.jpl7.Term;

import java.util.*;

public class JPLTest {

    public static void main(String[] args) {

        Query q1;
        Map<String,Term> m;
        /*
        q1 = new Query("consult('resources/KB.pl').");
        q1.hasMoreSolutions();
        q1.close();

        q1 = new Query("consult('resources/KB2.pl').");
        q1.hasMoreSolutions();
        q1.close();

        q1 = new Query("precedenza(X,Y)");
        while (q1.hasMoreSolutions()) {
            m = q1.nextSolution();
            System.out.println(m.get("X"));
            System.out.println(m.get("Y"));
        }
        */


        q1 = new Query("assert(prova(a)).");
        System.out.println(q1.hasSolution());
        q1.close();

        q1 = new Query("prova(b).");
        System.out.println(q1.hasSolution());
        q1.close();

        /*
        q1 = new Query("consult('resources/KB.pl').");
        q1.hasSolution();
        q1.close();

        q1 = new Query("make.");
        q1.hasSolution();
        q1.close();

        q1 = new Query("prova(a).");
        System.out.println(q1.hasSolution());
        q1.close();
         */

    }
}
