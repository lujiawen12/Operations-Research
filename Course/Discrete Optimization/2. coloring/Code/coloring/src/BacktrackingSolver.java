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
        long keepBest = 0;
        long keepBestMax = V * 100;
        while(n >= 0 && keepBest < keepBestMax) {
            while (colorOfNodes[curNodeId] < nodesBound[curNodeId] && !isValid(curNodeId, colorOfNodes[curNodeId], colorOfNodes)) {
                colorOfNodes[curNodeId]++;
            }
            if (colorOfNodes[curNodeId] < nodesBound[curNodeId]) {
                if (n == V-1) {
                    int colorNum = calColorNum(colorOfNodes);
                    if (colorNum < obj) {
                        obj = colorNum;
                        copyArray(colorOfNodes, bestColorPlan);
                        initialBound(obj, nodesBound);
                        keepBest = 0;
                    }
                    else {
                        keepBest += 1;
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

    //Create the initial solution and mark it as bound
    private void initialColorPlan(int[] bestColorPlan) {
        for (int i = 0; i < V; i++) {
            bestColorPlan[i] = -1;
        }
        int curColorNum = 0;
        for (Integer nodeId : nodesDescendByDeg) {
            int colorId = 0;
            while (colorId <= curColorNum) {
                if (isValid(nodeId, colorId, bestColorPlan)) {
                    break;
                }
                colorId++;
            }
            bestColorPlan[nodeId] = colorId;
            if (colorId > curColorNum) {
                curColorNum++;
            }
        }
    }

    private void initialBound(int obj, int[] nodesBound) {
        for (int i = 0; i < V; i++) {
            nodesBound[i] = Math.min(i+1, obj-1);
        }
    }

    private boolean isValid(int nodeId, int colorId, int[] colorOfNodes) {
        for (Integer neighbor : adjList.get(nodeId)) {
            if (colorId == colorOfNodes[neighbor]) {
                return false;
            }
        }
        return true;
    }

    private void copyArray(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }

}