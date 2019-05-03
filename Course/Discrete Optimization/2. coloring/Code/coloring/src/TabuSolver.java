import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TabuSolver extends ColoringSolver{

    private int[][] tabuTable;
    private int tableRowNum;
    private int tableColNum;
    private int tabuLen;

    private int bestObj;
    private int[] bestColorPlan;

    public TabuSolver(int V, int E, List<List<Integer>> adjList) {
        super(V, E, adjList);
        bestObj = Integer.MAX_VALUE;
        bestColorPlan = new int[V];
    }

    @Override
    public ColoringSolution solve() {
        ColoringSolution tabuSolution = new ColoringSolution();
        tabuSolution.setApproach("Tabu Search");

        initialBestColorPlan();
        int needColorNum = bestObj;
        int[] colorPlan = new int[V];
        boolean isUnfinished = true;
        while (isUnfinished) {
            needColorNum --;
            localSearch(colorPlan, needColorNum);
            if (isColorPlanFeasible(colorPlan)) {
                copyArray(colorPlan, bestColorPlan);
                bestObj = needColorNum;
            }
            else {
                isUnfinished = false;
            }
        }

        tabuSolution.setColorOfNodes(bestColorPlan);
        tabuSolution.setObj(bestObj);
        return tabuSolution;
    }


    public void localSearch(int[] colorPlan, int colorNum) {
        randomInitial(colorPlan, colorNum);
        int[][] conflictRecord = initialConflictRecord(colorPlan, colorNum);
        int delta;
        do {
            List<Integer> conflictNode = findConflictNode(colorPlan);
            delta = findBestMove(colorPlan, conflictRecord, conflictNode);
        } while (delta < 0);
    }

    private List<Integer> findConflictNode(int[] colorPlan) {
        List<Integer> conflictNode = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            for (Integer neigh : adjList.get(i)) {
                if (colorPlan[neigh] == colorPlan[i]) {
                    conflictNode.add(i);
                    break;
                }
            }
        }
        return conflictNode;
    }

    private int findBestMove(int[] colorPlan, int[][] conflictRecord, List<Integer> cofilictNode) {
        int colNum = conflictRecord[0].length;
        int moveNode = -1;
        int moveToColor = -1;
        int minDelta = 0;
        for (Integer nodeId : cofilictNode) {
            int curColor = colorPlan[nodeId];
            int curConflict = conflictRecord[nodeId][curColor];
            int delta = 0;
            int selectColor = curColor;
            for (int j = 0; j < colNum; j++) {
                int nextConflict = conflictRecord[nodeId][j];
                if (nextConflict - curConflict < delta) {
                    delta = nextConflict - curConflict;
                    selectColor = j;
                }
            }
            if (delta < minDelta) {
                minDelta = delta;
                moveNode = nodeId;
                moveToColor = selectColor;
            }
        }

        System.out.println(moveNode + "\t" + moveToColor);

        if (moveNode != -1) {
            updateConflictRecord(conflictRecord, moveNode, colorPlan[moveNode], moveToColor);
            colorPlan[moveNode] = moveToColor;
            return minDelta;
        }
        else {
            return Integer.MAX_VALUE;
        }

    }

    private void updateConflictRecord(int[][] conflictRecord, int nodeId, int curColor, int nextColor) {
        for (Integer neigh : adjList.get(nodeId)) {
            conflictRecord[neigh][curColor] --;
            conflictRecord[neigh][nextColor] ++;
        }
    }

    private int[][] initialConflictRecord(int[] colorPlan, int colorNum) {
        int[][] conflictRecord = new int[V][colorNum];
        for (int nodeId = 0; nodeId < V; nodeId++) {
            for (int colorId = 0; colorId < colorNum; colorId++) {
                conflictRecord[nodeId][colorId] = calConflictNum(colorPlan, nodeId, colorId);
            }
        }
        return conflictRecord;
    }

    private int calConflictNum(int[] colorPlan) {
        int conflictNum = 0;
        for (int i = 0; i < V; i++) {
            for (Integer neigh : adjList.get(i)) {
                if (colorPlan[neigh] == colorPlan[i]) {
                    conflictNum ++;
                }
            }
        }
        conflictNum /= 2;
        return conflictNum;
    }

    private int calConflictNum(int[] colorPlan, int nodeId, int colorId) {
        int conflictNum = 0;
        for(Integer neigh : adjList.get(nodeId)) {
            if (colorPlan[neigh] == colorId) {
                conflictNum++;
            }
        }
        return conflictNum;
    }



    public void randomInitial(int[] colorPlan, int colorNum) {
        //todo  去掉种子
        Random rand = new Random(0);
        for (int i = 0; i < V; i++) {
            colorPlan[i] = rand.nextInt(colorNum);
        }
    }

    private void initialBestColorPlan() {
        initialColorPlan(bestColorPlan);
        bestObj = calColorNum(bestColorPlan);
    }

}


//    private void intialTabuTable(int obj) {
//        tableRowNum = V;
//        tableColNum = obj;
//        tabuTable = new int[tableRowNum][tableColNum];
//        for (int i = 0; i < tableRowNum; i++) {
//            for (int j = 0; j < tableColNum; j++) {
//                if (j >= nodesBound[i]) {
//                    tabuTable[i][j] = Integer.MAX_VALUE;
//                }
//            }
//        }
//    }