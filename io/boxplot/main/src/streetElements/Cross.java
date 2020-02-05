package streetElements;

import java.util.ArrayList;

public class Cross {
    String name = "";
    ArrayList<String> connessioni = new ArrayList<>();

    public Cross(String nameC){
        name = nameC;
    }

    public void insertConnection(String nameStreet){
        connessioni.add(nameStreet);

    }

    @Override
    public String toString(){
        return "Cross{" +
                "name='" + name + '\'' +
                ", connessioni=" + connessioni + '}';
    }
}
