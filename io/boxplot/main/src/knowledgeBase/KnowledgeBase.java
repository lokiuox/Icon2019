package knowledgeBase;

import streetElements.Cross;
import streetElements.Street;

import java.util.ArrayList;
import java.util.Map;

public interface KnowledgeBase {

    ArrayList<Street> getNodes(); //ritorna mappa nome nodo, peso, lunghezza , si potrebbe trasformare in abstract
    ArrayList<Cross> getCrosses();
    Integer getLength(String nomeStrada);
    ArrayList<String> getConnection(String nomeIncrocio);


    //qui ha senso se definiamo semafori e cartelli che oltre la lunghezza influenzano il peso
    //Integer getWeight();

    //non dovrebbero esistere in quella generica
    //Map<Integer,String> getStarts();
    //Map<Integer,String> getEnds();

}
