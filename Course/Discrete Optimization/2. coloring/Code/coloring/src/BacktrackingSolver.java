import java.util.List;

public class BacktrackingSolver extends ColoringSolver {
    public BacktrackingSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
    }

    @Override
    public ColoringSolution solve() {
        ColoringSolution backTrackingSolution = new ColoringSolution();
        backTrackingSolution.setApproach("Backtracking");

        int[] bestColorPlan = new int[V];
        int[] nodesBound = new int[V];
        int[] colorOfNodes = new int[V];
        for (int i = 1; i < V; i++) {
            colorOfNodes[i] = -1;
        }
        int[] nodesColorOrder = new int[V];
        int obj = initial(bestColorPlan, nodesBound, nodesColorOrder);

        int n = 0;
        int curNodeId = nodesColorOrder[n];
        colorOfNodes[curNodeId] = 0;
        long searchTime = 0;
        long searchTimeMax = V;
        while(n >= 0 && searchTime < searchTimeMax) {
            System.out.println(n);
            while (colorOfNodes[curNodeId] < nodesBound[curNodeId] && !isValid(curNodeId, colorOfNodes[curNodeId], colorOfNodes)) {
                colorOfNodes[curNodeId]++;
            }
            if (colorOfNodes[curNodeId] < nodesBound[curNodeId]) {
                if (n == V-1) {
                    searchTime++;
                    int colorNum = calColorNum(colorOfNodes);
                    System.out.println(colorNum + "\t" + obj);
                    if (colorNum < obj) {
                        obj = colorNum;
                        copyArray(colorOfNodes, bestColorPlan);
                        initialBound(obj, nodesBound);
                    }
                    colorOfNodes[curNodeId] += 1;
                }
                else {
                    n++;
                    curNodeId = nodesColorOrder[n];
                    colorOfNodes[curNodeId] = 0;
                }
            }
            else {//Backtracking
                n--;
                if (n >= 0) {
                    curNodeId = nodesColorOrder[n];
                    colorOfNodes[curNodeId] += 1;
                }
            }
        }

        backTrackingSolution.setColorOfNodes(bestColorPlan);
        backTrackingSolution.setObj(obj);
        return backTrackingSolution;
    }



    //initial the solution, bound and objective
    private int initial(int[] bestColorPlan, int[] nodesBound, int[] nodesColorOrder){
        initialColorPlan(bestColorPlan);
        int obj = calColorNum(bestColorPlan);
        initialBound(obj, nodesBound);
        initialColorOrderOfNodes(nodesColorOrder, obj);

        System.out.println(obj);
        for (int i = 0; i < V; i++) {
            System.out.print(bestColorPlan[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < V; i++) {
            System.out.print(nodesBound[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < V; i++) {
            System.out.print(nodesColorOrder[i] + " ");
        }
        System.out.println();

        return obj;
    }

    //Create the colorOrder of nodes
    private void initialColorOrderOfNodes(int[] nodesColorOrder, int obj){
        for (int i = 0; i < obj; i++) {
            nodesColorOrder[i] = i;
        }
        int index = 0;
        for (int i = obj; i < V; i++) {
            while(nodesDescendByDeg.get(index) < obj) {
                index ++;
            }
            nodesColorOrder[i] = nodesDescendByDeg.get(index);
            index++;
        }
    }


    private void initialBound(int obj, int[] nodesBound) {
        for (int i = 0; i < V; i++) {
            nodesBound[i] = Math.min(i+1, obj-1);
        }
    }

}