import java.util.List;

public class KnapsackSolution {
    private int itemsNum;
    private int weight;
    private int value;
    private List<Item> choseItems;
    private String approach;

    public void print() {
        System.out.println(value+" 0");
        int[] taken = new int[itemsNum];
        for (Item item : choseItems) {
            taken[item.getId()] = 1;
        }
        for (int i = 0; i < itemsNum; i++) {
            System.out.print(taken[i] + " ");
        }
        System.out.println();
    }

    public void setItemsNum(int itemsNum) {
        this.itemsNum = itemsNum;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public List<Item> getChoseItems() {
        return choseItems;
    }

    public void setChoseItems(List<Item> choseItems) {
        this.choseItems = choseItems;
        for (Item item : choseItems) {
            this.weight += item.getWeight();
            this.value += item.getValue();
        }
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }
}
