package streetElements;

public class Street {
    String name,orientation;
    Integer weight,length,coordinate;





    public Street (String n, Integer l, Integer w,String orient,Integer coord){
        name = n;
        length = l;
        weight = w;
        orientation = orient;
        coordinate = coord;
    }

    public String getName() {
        return name;
    }

    public Integer getLength() {
        return length;
    }

    public String getOrientation(){
        return orientation;
    }

    public Integer getCoordinate(){
        return coordinate;
    }

    @Override
    public String toString() {
        return "Street{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", length=" + length +
                ", orientation=" + orientation +
                ", coordinate=" + coordinate +
                '}';
    }
}
