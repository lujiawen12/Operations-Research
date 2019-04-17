import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BacktrackingSolver extends ColoringSolver {
    public BacktrackingSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
    }


    @Override
    public ColoringSolution solve() throws IOException {
        ColoringSolution backTrackingSolution = new ColoringSolution();
        backTrackingSolution.setApproach("Backtracking");

//        /* 写入Txt文件 */
//        File writeName = new File("btDebug.txt"); // 相对路径，如果没有则要建立一个新文件
//        writeName.createNewFile();
//        BufferedWriter out = new BufferedWriter(new FileWriter(writeName, false));
        int[] bestColorPlan = new int[V];
        int[] nodesBound = new int[V];
        int obj = initial(bestColorPlan, nodesBound);
        int[] nodesOrder = new int[V];
        for (int i = 0; i < obj; i++) {
            nodesOrder[i] = i;
        }
        int index = 0;
        for (int i = obj; i < V; i++) {
            while(this.nodesOrder.get(index) < obj) {
                index ++;
            }
            nodesOrder[i] = this.nodesOrder.get(index);
            index++;
        }
        int[] colorOfNodes = new int[V];
        for (int i = 1; i < V; i++) {
            colorOfNodes[i] = -1;
        }

//        out.write(obj + "\n");
//        for (int i = 0; i < V; i++) {
//            out.write(bestColorPlan[i] + " ");
//        }
//        out.write("\n");
//        for (int i = 0; i < V; i++) {
//            out.write(nodesBound[i] + " ");
//        }
//        out.write("\n");

        int n = 0;
        int curNodeId = nodesOrder[n];
        colorOfNodes[curNodeId] = 0;
        long keepBest = 0;
        long keepBestMax = V * V * 600;
        while(n >= 0 && keepBest < keepBestMax) {
            //System.out.println(n);
            while (colorOfNodes[curNodeId] < nodesBound[curNodeId] && !isValid(curNodeId, colorOfNodes[curNodeId], colorOfNodes)) {
                colorOfNodes[curNodeId]++;
            }
            if (colorOfNodes[curNodeId] < nodesBound[curNodeId]) {
                if (n == V-1) {
                    int colorNum = calColorNum(colorOfNodes);
                    //System.out.println(obj + "   " + colorNum);
                    if (colorNum < obj) {
                        obj = colorNum;
                        copyArray(colorOfNodes, bestColorPlan);
                        for (int i = Math.max(0, obj); i < V; i++) {
                            nodesBound[i] = obj;
                        }
                        keepBest = 0;
                    }
                    else {
                        keepBest += 1;
                    }
                    colorOfNodes[curNodeId] += 1;
                    //System.out.println(keepBest);
                }
                else {
                    n++;
                    curNodeId = nodesOrder[n];
                    colorOfNodes[curNodeId] = 0;
                }
            }
            else {//Backtracking
                n--;
                if (n >= 0) {
                    curNodeId = nodesOrder[n];
                    colorOfNodes[curNodeId] += 1;
                }
            }
        }

        //out.close();
        backTrackingSolution.setColorOfNodes(bestColorPlan);
        backTrackingSolution.setObj(obj);
        return backTrackingSolution;
    }

    //Initial the bound
    private int initial(int[] bestColorPlan, int[] nodesBound) {
        int obj = -1;
        for (int i = 1; i < V; i++) {
            bestColorPlan[i] = -1;
        }
        bestColorPlan[0] = 0;
        int n = 0;
        while(n >= 0) {
            while (bestColorPlan[n] < n+1 && !isValid(n, bestColorPlan[n], bestColorPlan)) {
                bestColorPlan[n]++;
            }
            if (bestColorPlan[n] < n+1) {
                if (n == V-1) {
                    obj = calColorNum(bestColorPlan);
                    break;
                }
                else {
                    n++;
                    bestColorPlan[n] = 0;
                }
            }
            else {//Backtracking
                n--;
                if (n >= 0) {
                    bestColorPlan[n]++;
                }
            }
        }

        for (int i = 0; i < obj; i++) {
            nodesBound[i] = i+1;
        }
        for (int i = obj; i < V; i++) {
            nodesBound[i] = obj;
        }

        return obj;
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


