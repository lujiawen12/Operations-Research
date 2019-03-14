import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedySolver extends KnapsackSolver {

    public GreedySolver(List<Item> items, int capacity) {
        super(items, capacity);
    }

    @Override
    public KnapsackSolution solve() {
        KnapsackSolution greedySolution = new KnapsackSolution();
        greedySolution.setApproach("Greedy");
        greedySolution.setItemsNum(items.size());
        List<Item> greedyItems = new ArrayList<>();
        for (Item item : items) {
            try {
                greedyItems.add((Item) item.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(greedyItems, Item.byRatio());
        int choseItemNum = 0;
        int usedCap = 0;
        for (Item item : greedyItems) {
            usedCap += item.getWeight();
            if (usedCap > capacity) {
                break;
            }
            choseItemNum += 1;
        }
        greedySolution.setChoseItems(greedyItems.subList(0,choseItemNum));
        return greedySolution;
    }

}
