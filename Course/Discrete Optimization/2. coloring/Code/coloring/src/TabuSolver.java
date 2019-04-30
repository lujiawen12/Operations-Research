import java.util.List;

public class TabuSolver extends ColoringSolver{

    private int[][] tabuTable;
    private int tableRowNum;
    private int tableColNum;
    private int tabuLen;

    public TabuSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
    }

    @Override
    public ColoringSolution solve() {
        ColoringSolution tabuSolution = new ColoringSolution();
        tabuSolution.setApproach("Tabu Search");
        int obj = initial();


        tabuSolution.setColorOfNodes(bestColorPlan);
        tabuSolution.setObj(obj);
        return tabuSolution;
    }

    private void updateTabuTable() {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < tableColNum; j++) {
                tabuTable[i][j] = Math.max(0, tabuTable[i][j]-1);
            }
        }
    }

    private int initial() {
        initialColorPlan(bestColorPlan);
        int obj = calColorNum(bestColorPlan);
        initialBound(obj, nodesBound);
        intialTabuTable(obj);
        tabuLen = 10;
        return obj;
    }

    private void intialTabuTable(int obj) {
        tableRowNum = V;
        tableColNum = obj;
        tabuTable = new int[tableRowNum][tableColNum];
        for (int i = 0; i < tableRowNum; i++) {
            for (int j = 0; j < tableColNum; j++) {
                if (j >= nodesBound[i]) {
                    tabuTable[i][j] = Integer.MAX_VALUE;
                }
            }
        }
    }

    private void initialBound(int obj, int[] nodesBound) {
        for (int i = 0; i < V; i++) {
            nodesBound[i] = Math.min(i+1, obj);
        }
    }
}
