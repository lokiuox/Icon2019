package knowledgeBase;

import algorithms.AlgoInterface;
import helper.StringUtils;
import query.QueryCreator;
import streetElements.Cross;
import streetElements.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KBenanched implements KnowledgeBase {

    private QueryCreator query;
    private boolean startSetted,endSetted;
    private Integer nStart,nEnd;
    private String streetStart,streetEnd;

    public KBenanched(String filename){
        query = new QueryCreator(filename);
    }

    public void setStart(String nomeStrada,Integer numeroCivico){
        if(!StringUtils.isAllLowerCase(nomeStrada)){
            System.out.println("nome strada non valido");
        }else {
            query.setPredicate("assert(partenza("+nomeStrada+","+numeroCivico.toString()+"))");
            startSetted =  query.getBoolean();
            nStart = numeroCivico;
            streetStart = nomeStrada;
        }

        //System.out.println(query.getBoolean());

    }

    public void setEnd(String nomeStrada,Integer numeroCivico){
        if(!StringUtils.isAllLowerCase(nomeStrada)){
            System.out.println("nome strada non valido");
        }else {
            query.setPredicate("assert(destinazione("+nomeStrada+","+numeroCivico.toString()+"))");
            nEnd = numeroCivico;
            streetEnd = nomeStrada;
            endSetted = query.getBoolean();
        }
    }

    public Map<String, String> getStart(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("street", streetStart);
        map.put("number", nStart.toString());
        return map;
    }


    public Map<String, String> getEnd(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("street", streetEnd);
        map.put("number", nEnd.toString());
        return map;
    }



    public void calculatePath(AlgoInterface o){
        if(endSetted && startSetted){
            //ora calcoli
            o.exec(streetStart,nStart.toString(),streetEnd,nEnd.toString());
        }else{
            System.out.println("definire partenza e fine");
        }

    }

    public void setWeight(String nomeStrada,Integer peso){
        if(!StringUtils.isAllLowerCase(nomeStrada)){
            System.out.println("nome strada non valido");
        }else {
            query.setPredicate("assert(peso("+nomeStrada+","+peso.toString()+"))");
            query.getBoolean();
        }
    }

    public ArrayList<Street> getNodes(){
        return KBmethods.getNodes(query);
    } //ritorna mappa nome nodo, peso, lunghezza , si potrebbe trasformare in abstract
    public ArrayList<Cross> getCrosses(){
        return KBmethods.getCrosses(query);
    }
    public Integer getLength(String nomeStrada){
        return KBmethods.getLength(query,nomeStrada);
    }
    public ArrayList<String> getConnection(String nomeIncrocio){
        return KBmethods.getConnection(query,nomeIncrocio);
    }


}
