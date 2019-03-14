import java.util.List;

public class DynamicProgrammingSolver extends KnapsackSolver{

    public DynamicProgrammingSolver(List<Item> items, int capacity) {
        super(items, capacity);
    }

    @Override
    public KnapsackSolution solve() {
        KnapsackSolution dpSolution = new KnapsackSolution();
        dpSolution.setApproach("Dynamic Programming");
        dpSolution.setItemsNum(items.size());
        


        return dpSolution;
    }
}
