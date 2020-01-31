package streetElements;

public class Street {
    String name;
    Integer weight,length;

    public Street (String n, Integer l){
        name = n;
        length = l;
        weight = null;
    }
    public Street (String n, Integer l, Integer w){
        name = n;
        length = l;
        weight = w;
    }

    public String getName() {
        return name;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Street{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", length=" + length +
                '}';
    }
}
