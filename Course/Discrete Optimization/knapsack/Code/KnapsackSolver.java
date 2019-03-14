import java.util.List;

public abstract class KnapsackSolver {
    protected List<Item> items;
    protected int capacity;

    protected KnapsackSolver(List<Item> items, int capacity){
        this.items = items;
        this.capacity = capacity;
    }

    public abstract KnapsackSolution solve();
}
