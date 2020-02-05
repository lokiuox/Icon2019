package knowledgeBase;

import algorithms.AlgoInterface;
import algorithms.Node;
import helper.StringUtils;
import org.jpl7.Term;
import query.QueryCreator;
import streetElements.Cross;
import streetElements.Street;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Map<String, Object> getStart(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("street", this.getStreet(streetStart));
        map.put("number", nStart.toString());
        return map;
    }


    public Map<String, Object> getEnd(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("street", this.getStreet(streetEnd));
        map.put("number", nStart.toString());
        return map;
    }





    public List<Node> calculatePath(AlgoInterface o){
        if(endSetted && startSetted){

            //recupera start se orizzontale o verticale il numero civico get corrisponde alla partenza

            Map<String,Object> point = getStart();
            Street s = (Street) point.get("street");


            if(s.getOrientation().compareTo("V") == 0){
                o.setInitialNode(new Node(s.getCoordinate(),nStart));
            }else{
                o.setInitialNode(new Node(nStart,s.getCoordinate()));
            }

            point = getEnd();
            s = (Street) point.get("street");

            if(s.getOrientation().compareTo("V") == 0){
                o.setFinalNode(new Node(s.getCoordinate(),nStart));
            }else{
                o.setFinalNode(new Node(nStart,s.getCoordinate()));
            }

            return o.findPath();


        }else{
            System.out.println("definire partenza e fine");
        }

        return new ArrayList<Node>();

    }

    public void setWeight(String nomeStrada,Integer peso){
        if(!StringUtils.isAllLowerCase(nomeStrada)){
            System.out.println("nome strada non valido");
        }else {
            query.setPredicate("retract(peso("+nomeStrada+","+KBmethods.getWeight(query,nomeStrada)+"))");
            query.setPredicate("assert(peso("+nomeStrada+","+peso+"))");
            query.getBoolean();
        }
    }

    public Street getStreet(String nameStreet){
        return KBmethods.getStreet(query,nameStreet);
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
