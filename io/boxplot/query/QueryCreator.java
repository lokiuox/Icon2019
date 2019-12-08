package io.boxplot.query;

public class QueryCreator {

    int type;


    public QueryCreator(){

    }

    public QueryCreator(int type){
        this.setType(type);
    }


    private void setType(int type){
        //check if type is known
        if(type != 0 && type != 1){
            System.out.println("type not recognized");
        }else {
            this.type = type;
        }
    }


}
