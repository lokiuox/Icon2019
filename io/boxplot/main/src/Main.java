import knowledgeBase.KBimmutable;

public class Main {

    public static void main(String[] args){
        KBimmutable mainKB = KBimmutable.getInstance("../res/test.pl");
        System.out.println(mainKB.getNodes());
    }

}
