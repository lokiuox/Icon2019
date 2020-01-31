import knowledgeBase.KBenanched;
import knowledgeBase.KBimmutable;
import streetElements.Street;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args){
        //da aggiungere definizione del grafico in maniera dinamica
        //definire la kb come file e prendere l'absolute path
        KBimmutable mainKB = KBimmutable.getInstance("/home/arcangelo/Documents/Icon2019/io/boxplot/main/res/kbBasic.pl");


        //System.out.println(mainKB.getNodes().toString());
        //System.out.println(mainKB.getCrosses().toString());


        ArrayList<Street> strade = mainKB.getNodes();


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



        System.out.println(carKB.getNodes().toString());
        System.out.println(carKB.getCrosses().toString());

        carKB.setWeight("a",6);
        System.out.println(carKB.getNodes().toString());

        //creazione auto -> per ogni auto definire inizio e fine -> calcolare a*
        //interazione fra auto
    }

}
