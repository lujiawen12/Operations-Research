import java.util.ArrayList;
import java.util.Collections;
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

        int colNum = items.size()+1, rowNum = capacity+1;
        int[][] table = new int[rowNum][colNum];
        for (int j = 1; j < colNum; j++) {
            int w = items.get(j-1).getWeight();
            int v = items.get(j-1).getValue();
            for (int i = 0; i < rowNum; i++) {
                if (i < w) {
                    table[i][j] = table[i][j-1];
                }
                else {
                    table[i][j] = Math.max(table[i-w][j-1] + v, table[i][j-1]);
                }
            }
        }
        dpSolution.setChoseItems(backTrack(table));
        return dpSolution;
    }

    private List<Item> backTrack(int[][] table) {
        List<Item> choseItems = new ArrayList<>();
        int row = table.length-1;
        int col = table[0].length-1;
        while(col > 0) {
            if (table[row][col] != table[row][col-1]) {
                choseItems.add(items.get(col-1));
                row -= items.get(col-1).getWeight();
                col--;
            }
            else {
                col--;
            }
        }
        return choseItems;
    }
}
