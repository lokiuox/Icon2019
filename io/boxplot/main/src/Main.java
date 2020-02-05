import algorithms.AStar;
import algorithms.Node;
import knowledgeBase.KBenanched;
import knowledgeBase.KBimmutable;
import streetElements.Street;

import java.util.*;

public class Main {

    public static void main(String[] args){
        //da aggiungere definizione del grafico in maniera dinamica
        //definire la kb come file e prendere l'absolute path
        KBimmutable mainKB = KBimmutable.getInstance("/home/arcangelo/Documents/Icon2019/io/boxplot/main/res/kbBasic.pl");


        //System.out.println(mainKB.getNodes().toString());
        //System.out.println(mainKB.getCrosses().toString());


        HashMap<Integer,Integer> puntiBloccati = new HashMap<>();
        ArrayList<Street> strade = mainKB.getNodes();
        for(Street s : strade){
            if(s.getOrientation().compareTo("V") == 0){
                for (int i = 0; i <= s.getLength(); i++){
                    puntiBloccati.put(s.getCoordinate(),i);
                }
            }else{
                for (int i = 0; i <= s.getLength(); i++){
                    puntiBloccati.put(i,s.getCoordinate());
                }
            }
        }




        AStar singleIstance = AStar.getInstance(10,10);
        singleIstance.setBlocks(puntiBloccati);


        //il codice seguente sarà eseguito in un threads


        //scelta strada partenza e fine randomica
        Random r = new Random();
        int low = 0;
        int high = strade.size();

        int start = r.nextInt(high-low);
        int end = r.nextInt(high-low);

        //scelta randomica civico
        low = 1;
        high = strade.get(start).getLength();
        Integer nStart = r.nextInt(high -low) + low;


        low = 1;
        high = strade.get(end).getLength();
        Integer nEnd = r.nextInt(high -low) + low;

        //singola macchina
        KBenanched carKB = new KBenanched("/home/arcangelo/Documents/Icon2019/io/boxplot/main/res/test.pl");
        carKB.setStart(strade.get(start).getName(),nStart);
        carKB.setEnd(strade.get(end).getName(),nEnd);


        //ora calcoli
        List<Node> path = carKB.calculatePath(singleIstance);
        for (Node node : path) {
            System.out.println(node);
        }





        System.out.println(carKB.getNodes().toString());
        System.out.println(carKB.getCrosses().toString());

        //interazione fra auto
        //carKB.setWeight("b",6);






    }

}
