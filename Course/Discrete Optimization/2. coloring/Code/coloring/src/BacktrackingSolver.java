import java.util.List;

public class BacktrackingSolver extends ColoringSolver {
    public BacktrackingSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
    }


    @Override
    public ColoringSolution solve() {
        ColoringSolution backTrackingSolution = new ColoringSolution();
        backTrackingSolution.setApproach("Backtracking");
        int obj = Integer.MAX_VALUE;
        int[] bestColorPlan = new int[V];
        int[] colorOfNodes = new int[V];
        for (int i = 0; i < V; i++) {
            colorOfNodes[i] = -1;
        }
        colorOfNodes[0] = 0;


        //TOdo
        //增加剪枝
        int n = 0;
        int curNodeId = nodesIdDescendByDegree.get(n);
        colorOfNodes[curNodeId] = 0;
        while(n >= 0) {
//            int bound = calColorNum(colorOfNodes);
//            if (bound != -1 && bound >= obj) {
//                if (colorOfNodes[curNodeId] > curNodeId) {
//                    n--;
//                    if (n >= 0) {
//                        curNodeId = nodesIdDescendByDegree.get(n);
//                        colorOfNodes[curNodeId] += 1;
//                    }
//                }
//            }
            while (colorOfNodes[curNodeId] <= curNodeId && !isValid(curNodeId, colorOfNodes[curNodeId], colorOfNodes)) {
                colorOfNodes[curNodeId]++;
            }
            if (colorOfNodes[curNodeId] <= curNodeId) {
                if (n == V-1) {
                    int colorNum = calColorNum(colorOfNodes);
                    if (colorNum < obj) {
                        obj = colorNum;
                        copyArray(colorOfNodes, bestColorPlan);
                    }
                    colorOfNodes[curNodeId] += 1;
                    System.out.println(colorNum + "   " + obj);
                }
                else {
                    n++;
                    curNodeId = nodesIdDescendByDegree.get(n);
                    colorOfNodes[curNodeId] = 0;
                }
            }
            else {         //Backtracking
                n--;
                if (n >= 0) {
                    curNodeId = nodesIdDescendByDegree.get(n);
                    colorOfNodes[curNodeId] += 1;
                }
            }
        }

        backTrackingSolution.setColorOfNodes(bestColorPlan);
        backTrackingSolution.setObj(obj);
        return backTrackingSolution;
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


