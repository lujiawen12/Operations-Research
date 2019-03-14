import java.util.Comparator;

public class Item implements Cloneable{
    private int id;
    private int value;
    private int weight;

    //Ascending Order
    public static Comparator<Item> byId() {
        return new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getId() - o2.getId();
            }
        };
    }

    //Descending Order
    public static Comparator<Item> byRatio() {
        return new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Double.compare(o2.getRatio(), o1.getRatio());
            }
        };
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getRatio(){
        return (value+0.0) / weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
