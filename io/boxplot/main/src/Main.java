import knowledgeBase.KBimmutable;

public class Main {

    public static void main(String[] args){
        //da aggiungere definizione del grafico in maniera dinamica
        KBimmutable mainKB = KBimmutable.getInstance("/home/arcangelo/Documents/Icon2019/io/boxplot/main/res/kbBasic.pl");
        System.out.println(mainKB.getNodes().toString());
        System.out.println(mainKB.getCrosses().toString());
        //creazione auto -> per ogni auto definire inizio e fine -> calcolare a*
        //interazione fra auto
    }

}
