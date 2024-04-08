import java.util.List;

public class BacktrackingSolver extends ColoringSolver {
    private int[] bestColorPlan;
    private int[] nodesBound;
    private int[] colorPlan;

    public BacktrackingSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
        bestColorPlan = new int[V];
        nodesBound = new int[V];
        colorPlan = new int[V];
        for (int i = 1; i < V; i++) {
            colorPlan[i] = -1;
        }
    }



    @Override
    public ColoringSolution solve() {
        ColoringSolution backTrackingSolution = new ColoringSolution();
        backTrackingSolution.setApproach("Backtracking");

        int[] nodesColorOrder = new int[V];
        int obj = initial(nodesColorOrder);

        int n = 0;
        int curNodeId = nodesColorOrder[n];
        colorPlan[curNodeId] = 0;
        long searchTime = 0;
        long searchTimeMax = V;
        while(n >= 0 && searchTime < searchTimeMax) {
            while (colorPlan[curNodeId] < nodesBound[curNodeId] && !isValid(curNodeId, colorPlan[curNodeId], colorPlan)) {
                colorPlan[curNodeId]++;
            }
            if (colorPlan[curNodeId] < nodesBound[curNodeId]) {
                if (n == V-1) {
                    searchTime++;
                    int colorNum = calColorNum(colorPlan);
                    if (colorNum < obj) {
                        obj = colorNum;
                        copyArray(colorPlan, bestColorPlan);
                        calculateBound(obj);
                    }
                    colorPlan[curNodeId] += 1;
                }
                else {
                    n++;
                    curNodeId = nodesColorOrder[n];
                    colorPlan[curNodeId] = 0;
                }
            }
            else {//Backtracking
                n--;
                if (n >= 0) {
                    curNodeId = nodesColorOrder[n];
                    colorPlan[curNodeId] += 1;
                }
            }
        }

        backTrackingSolution.setColorOfNodes(bestColorPlan);
        backTrackingSolution.setObj(obj);
        return backTrackingSolution;
    }



    //initial the solution, bound and objective
    private int initial(int[] nodesColorOrder){
        initialColorPlan(bestColorPlan);
        int obj = calColorNum(bestColorPlan);
        calculateBound(obj);
        initialColorOrderOfNodes(nodesColorOrder, obj);
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


    private void calculateBound(int obj) {
        for (int i = 0; i < V; i++) {
            nodesBound[i] = Math.min(i+1, obj-1);
        }
    }

}