import java.util.List;

public class BruteForceSolver extends ColoringSolver{
    public BruteForceSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
    }

    @Override
    public ColoringSolution solve() {
        ColoringSolution bruteForceSolution = new ColoringSolution();
        bruteForceSolution.setApproach("Brute Force");
        int[] colorOfNodes = new int[V];
        int obj = Integer.MAX_VALUE;
        int[] bestColorPlan = new int[V];

        //Main body  Brute Force
        for (int i = 0; i < V; i++) {
            colorOfNodes[i] = 0;
        }
        while(addOne(colorOfNodes, V)) {
            print(colorOfNodes);
            if (isMeetConstraint(colorOfNodes, adjList)) {
                int colorNum = calColorNum(colorOfNodes);
                if (colorNum < obj) {
                    obj = colorNum;
                    copyArray(colorOfNodes, bestColorPlan);
                }
            }
        }

        bruteForceSolution.setColorOfNodes(bestColorPlan);
        bruteForceSolution.setObj(obj);
        return bruteForceSolution;
    }

    private boolean addOne(int[] array, int V) {
        int carry = 1;
        for (int i = array.length-1; i >=0; i--) {
            int sum = array[i] + carry;
            carry = sum / V;
            array[i] = sum % V;

        }
        return carry == 0;
    }

    private void copyArray(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }

    private void print(int[] colorOfNodes) {
        for (int item : colorOfNodes) {
            System.out.print(item + " ");
        }
        System.out.println();
    }


}
