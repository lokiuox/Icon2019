package com.patterson.entity;

import com.patterson.algorithms.AStarGraph;
import com.patterson.algorithms.Graph;
import com.patterson.algorithms.IDAStarGraph;
import com.patterson.utility.NavigatorUtility;

import java.util.*;

import static com.patterson.algorithms.Graph.TYPE.DIRECTED;

public class NavigatorIDAStar implements INavigator {

    Graph<String> g;
    IDAStarGraph<String> instance;
    ArrayList<Map<String, String>> strade;
    //String startStreet,endStreet; //forse da cambiare in Map<String,Integer>
    HashMap<String, Graph.Vertex<String>> v;
    NavigatorUtility nu = new NavigatorUtility();

    //int low = 0, high;


    public NavigatorIDAStar(){
        makeGraph();
    }

    private void makeGraph(){
        //conversione delle strade ed incroci per aStar
        v = new HashMap<>();
        HashMap<String,Graph.Edge<String>> a = new HashMap<>();


        strade = nu.getNodes();

        for (Map<String, String> s : strade) {
            Graph.Vertex<String> nuovo = new Graph.Vertex<>(s.get("id"), Float.parseFloat(s.get("peso")),Integer.parseInt(s.get("x")),Integer.parseInt(s.get("y")));
            v.put(s.get("id"),nuovo);

        }


        for (Map<String,String> sname : strade) {
            ArrayList<String> nomiStrade = nu.getConnectionStreet(sname.get("id"));
            for (String s: nomiStrade ) {
                Map<String, String> to = nu.getStreet(s);
                Graph.Edge<String> gEdge = new Graph.Edge<>(Float.parseFloat(sname.get("peso")), v.get(sname.get("id")), v.get(to.get("id")));
                v.get(sname.get("id")).addEdge(gEdge);
                a.put(sname.get("id")+" "+to.get("id"),gEdge);
            }
        }

        HashSet<Graph.Vertex<String>> v1 = new HashSet<>(v.values());
        HashSet<Graph.Edge<String>> a1 = new HashSet<>(a.values());

        g = new Graph<>(DIRECTED,v1,a1);
        //System.out.println(g);
        instance = new IDAStarGraph<>();


    }
    public List<String> calculatePath(String startRoad, String endRoad){
        LinkedList<String> seqStreets = new LinkedList<>();
        //seqStreets.add(startRoad);
        List<Graph.Vertex<String>> path = instance.idaStar(v.get(startRoad),v.get(endRoad));
        if(path != null){
            for (Graph.Vertex<String> node : path) {
                seqStreets.add(node.getValue());

            }
        }
        return seqStreets;
    }

}