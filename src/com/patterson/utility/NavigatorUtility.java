package com.patterson.utility;


import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class NavigatorUtility {

    private  KnowledgeBase k = new KnowledgeBase();

    public Map<String,String> getStreet(String s){
        Map<String,String> strada = new HashMap<>();
        strada.put("id",s);
        strada.put("direzione",getStreetOrientation(s));
        strada.put("peso",getWeight(s).toString());
        strada.put("lunghezza",getLength(s).toString());

        ArrayList<String> coords = getCoords(s);
        if(coords.size() > 0){
            strada.put("x",coords.get(0));
            strada.put("y",coords.get(1));
        }else{
            strada.put("x","0");
            strada.put("y","0");
        }

        return strada;
    }

    public ArrayList<Map<String,String>> getNodes(){
        Set<Map<String, String>> Roads = k.stringQuery("strada(X).");

        ArrayList<Map<String,String>> strade = new ArrayList<>();

        Roads.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        Map<String,String> strada = new HashMap<>();
                        strada.put("id",s);
                        strada.put("direzione",getStreetOrientation(s));
                        strada.put("peso",getWeight(s).toString());
                        strada.put("lunghezza",getLength(s).toString());

                        ArrayList<String> coords = getCoords(s);
                        if(coords.size() > 0){
                            strada.put("x",coords.get(0));
                            strada.put("y",coords.get(1));
                        }else{
                            strada.put("x","0");
                            strada.put("y","0");
                        }
                        strade.add(strada);
                    }
                }
        );

        return strade;
    }

    /*
    public static ArrayList<Cross> getCrosses(QueryCreator query){
        ArrayList<Cross> crosses = new ArrayList<>();
        String va[] = {"X"};
        String temp;
        query.setPredicate("incrocio",va);

        for (Map<String, Term> entry : query.getResults()) { //itero sulle mappe
            for (Map.Entry<String,Term> val : entry.entrySet()) { //singolo valore
                temp = val.getValue().toString();
                if(!temp.matches("_[0-9]+")){
                    Cross c = new Cross(temp);
                    ArrayList<String>  conn = getConnection(query,temp);
                    conn.forEach((n) -> c.insertConnection(n));
                    crosses.add(c);
                }

            }
        }
        return crosses;
    }

     */
    public Integer getLength(String nomeStrada){
        Set<Map<String, String>> Coords = k.stringQuery("lunghezza("+nomeStrada+",V).");
        AtomicReference<Integer> temp = new AtomicReference<>(1); //1 perchè senno il random di calculate path va a puttane e se una strada esiste è almeno di uno
        Coords.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        temp.set(Integer.valueOf(s));
                    }
                }
        );
        return temp.get();
    }

    public Integer getWeight(String nomeStrada){
        Set<Map<String, String>> Coords = k.stringQuery("peso("+nomeStrada+",V).");
        AtomicReference<Integer> temp = new AtomicReference<>(0);
        Coords.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        temp.set(Integer.valueOf(s));
                    }
                }
        );
        return temp.get();
    }

    public ArrayList<String> getConnectionStreet(String nomeStrada){
        Set<Map<String, String>> Coords = k.stringQuery("collega(X,"+nomeStrada+",V).");
        ArrayList<String> coords = new ArrayList<>();
        Coords.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        ArrayList<String> tmp = getConnection(s);
                        tmp.remove(nomeStrada);
                        coords.addAll(tmp);
                    }
                }
        );
        return coords;
    }

    public  ArrayList<String> getConnection(String nomeIncrocio){
        Set<Map<String, String>> Coords = k.stringQuery("collega("+nomeIncrocio+",V,D).");
        ArrayList<String> coords = new ArrayList<>();
        Coords.forEach(
                r -> {
                    for ( String s : r.keySet()){ //sempre un solo valore
                        if(s.equals("V"))
                            coords.add(r.get("V"));
                    }
                }
        );
        return coords;
    }

    public String getStreetOrientation(String nomeStrada){
        Set<Map<String, String>> Coords = k.stringQuery("angolo("+nomeStrada+",V).");
        AtomicReference<String> temp = new AtomicReference<>("");
        Coords.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        temp.set(s);
                    }
                }
        );
        return temp.get();
    }

    public ArrayList<String> getCoords(String nomeStrada){
        Set<Map<String, String>> Coords = k.stringQuery("coordinata("+nomeStrada+",X,V).");
        ArrayList<String> coords = new ArrayList<>();
        Coords.forEach(
                r -> {
                    for ( String s : r.values()){ //sempre un solo valore
                        coords.add(s);
                    }
                }
        );
        return coords;
    }
}