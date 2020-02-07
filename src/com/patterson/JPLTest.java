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

        /*
        q1 = new Query("assert(prova(a)).");
        q1.hasSolution();
        q1.close();

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
        (new RunThread("consult('resources/KB.pl').", 0)).start();
        (new RunThread("assert(test(a)).", 50)).start();
        (new RunThread("test(a).", 51)).start();

    }

    static class RunThread extends Thread {

        String query;
        int delay;

        public RunThread(String q, int d) {
            delay = d;
            query = q;
        }

        @Override
        public void run() {
            try {
                this.sleep(delay);
                Query q1 = new Query(query);
                System.out.println(q1.hasSolution());
                q1.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
