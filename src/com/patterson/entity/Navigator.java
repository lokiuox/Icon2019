package com.patterson.entity;

import com.patterson.algorithms.AStarGraph;
import com.patterson.algorithms.Graph;
import com.patterson.utility.NavigatorUtility;

import java.util.*;

import static com.patterson.algorithms.Graph.TYPE.DIRECTED;

public class Navigator {

    Graph<String> g;
    AStarGraph<String> instance;
    ArrayList<Map<String, String>> strade;
    String startStreet,endStreet; //forse da cambiare in Map<String,Integer>
    HashMap<String, Graph.Vertex<String>> v;
    int low = 0, high;


    public Navigator (){
        makeGraph();
        randomStart();
    }

    public ArrayList<String> getFirstPath(){
        return calculatePath();
    }

    public ArrayList<String> setNewEnd(String streetName){
        startStreet = endStreet;
        if(v.containsKey(streetName)){
            endStreet = streetName;
        }else{
            System.err.println("Strada non esistente");
        }
        return calculatePath();

    }
    private void makeGraph(){
        //conversione delle strade ed incroci per aStar
        v = new HashMap<>();
        HashMap<String,Graph.Edge<String>> a = new HashMap<>();

        NavigatorUtility nu = new NavigatorUtility();

        strade = nu.getNodes();

        for (Map<String, String> s : strade) {
            Graph.Vertex<String> nuovo = new Graph.Vertex<>(s.get("id"), Integer.parseInt(s.get("lunghezza")));
            v.put(s.get("id"),nuovo);

        }


        for (Map<String,String> sname : strade) {
            ArrayList<String> nomiStrade = nu.getConnectionStreet(sname.get("id"));
            for (String s: nomiStrade ) {
                Map<String, String> to = nu.getStreet(s);
                Graph.Edge<String> gEdge = new Graph.Edge<>(Integer.parseInt(sname.get("lunghezza")), v.get(sname.get("id")), v.get(to.get("id")));
                v.get(sname.get("id")).addEdge(gEdge);
                a.put(sname.get("id")+" "+to.get("id"),gEdge);

                 
            }
        }

        HashSet<Graph.Vertex<String>> v1 = new HashSet<>(v.values());
        HashSet<Graph.Edge<String>> a1 = new HashSet<>(a.values());

       g = new Graph<String>(DIRECTED,v1,a1);
       instance = new AStarGraph<>();


    }
    private void randomStart(){
        Random r = new Random();
        high = strade.size();

         int start = r.nextInt(high-low);
         int end = r.nextInt(high-low);

        //scelta randomica civico
        low = 1;
        high = Integer.parseInt(strade.get(start).get("lunghezza"));
        int bound = ((high -low) == 0) ? 1 : high -low;
        Integer nStart = r.nextInt(bound) + low; //numero civico non utilizzato


        low = 1;
        high = Integer.parseInt(strade.get(end).get("lunghezza"));
        bound = ((high -low) == 0) ? 1 : high -low;
        Integer nEnd = r.nextInt(bound) + low; //numero civico non utilizzato


        startStreet = strade.get(start).get("id");
        endStreet = strade.get(end).get("id");

    }
    private ArrayList<String> calculatePath(){
        ArrayList<String> seqStreets = new ArrayList<>();
        seqStreets.add(startStreet);
        List<Graph.Edge<String>> path = instance.aStar(g,v.get(startStreet),v.get(endStreet));
        if(path != null){
            for (Graph.Edge<String> node : path) {
                Graph.Vertex<String> v = node.getToVertex();
                seqStreets.add(v.getValue());

            }
        }


        return seqStreets;

    }

}
