import java.util.*;

public abstract class ColoringSolver {
    private class Node {
        int id;
        int nodeDeg;
        Node(int id) {
            this.id = id;
            this.nodeDeg = 0;
        }
    }

    protected int V;
    protected int E;
    protected List<List<Integer>> adjList;
    protected Map<Integer, Integer> nodesDegreeMap;
    protected List<Integer> nodesDescendByDeg;


    public abstract ColoringSolution solve();

    protected ColoringSolver(int V, int E, List<List<Integer>> adjList) {
        this.V = V;
        this.E = E;
        this.adjList = adjList;
        buildNodesDegreeMap();
        buildNodesOrder();
    }

    private void buildNodesDegreeMap() {
        nodesDegreeMap = new HashMap<>(V);
        int[] nodesDeg = new int[V];
        for(int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).isEmpty())
                continue;
            nodesDeg[i] += adjList.get(i).size();
        }
        for (int i = 0; i < V; i++) {
            nodesDegreeMap.put(i, nodesDeg[i]);
        }
    }

    private void buildNodesOrder() {
        nodesDescendByDeg = new ArrayList<>(V);
        List<Node> nodes = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            nodes.add(new Node(i));
        }
        for(int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).isEmpty())
                continue;
            for (Integer neighbor : adjList.get(i)) {
                nodes.get(i).nodeDeg++;
                nodes.get(neighbor).nodeDeg++;
            }
        }
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return -1*(o1.nodeDeg - o2.nodeDeg);
            }
        });

        for (Node node : nodes) {
            nodesDescendByDeg.add(node.id);
        }
    }

    protected int calColorNum(int[] colorOfNodes) {
        int colorNum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int curColor : colorOfNodes) {
            if (curColor < 0 || curColor >= V) {
                return -1;
            }
            if (!map.containsKey(curColor)) {
                colorNum ++;
                map.put(curColor, -1);
            }
        }
        return colorNum;
    }

    protected int calColorNum(int nodeId, int colorId, int[] colorOfNodes) {
        int[] changeColorOfNodes = new int[colorOfNodes.length];
        copyArray(colorOfNodes, changeColorOfNodes);
        changeColorOfNodes[nodeId] = colorId;
        return calColorNum(changeColorOfNodes);
    }


    protected boolean isValid(int nodeId, int colorId, int[] colorOfNodes) {
        for (Integer neighbor : adjList.get(nodeId)) {
            if (colorId == colorOfNodes[neighbor]) {
                return false;
            }
        }
        return true;
    }

    //Use the greedy algorithm based on node degree to create the initial color plan
    protected void initialColorPlan(int[] colorPlan) {
        for (int i = 0; i < V; i++) {
            colorPlan[i] = -1;
        }
        colorPlan[0] = 0;

        int[] nodesId = new int[V];
        int[] nodesStatus = new int[V];
        for (int i = 0; i < V; i++) {
            nodesId[i] = nodesDescendByDeg.get(i);
            nodesStatus[i] = 1;
        }

        Map<Integer, Integer> coloredNode = new HashMap<>();

        for (int i = 0; i < V; i++) {
            int selectedNodeId = getNodeIdOfMostDegree(nodesId, nodesStatus, coloredNode);
            int colorId = 0;
            while(colorId < V) {
                if (isValid(selectedNodeId, colorId, colorPlan)) {
                    break;
                }
                colorId++;
            }
            colorPlan[selectedNodeId] = colorId;
        }

        if (!isColorPlanFeasible(colorPlan)){
            System.out.println("Color Plan Unfeasible");
        }
    }

    private int getNodeIdOfMostDegree(int[] nodesId, int[] nodesStatus, Map<Integer, Integer> coloredNode) {
        int selectIndex = -1;
        int nodeDegree = -1;
        for (int i = 0; i < V; i++) {
            if (nodesStatus[i] == 1 && !adjList.get(nodesId[i]).isEmpty()) {
                int degree = 0;
                for (Integer neigh : adjList.get(nodesId[i])) {
                    if (!coloredNode.containsKey(neigh)) {
                        degree ++;
                    }
                }
                if (degree > nodeDegree) {
                    nodeDegree = degree;
                    selectIndex = i;
                }
            }
        }

        if (selectIndex != -1) {
            nodesStatus[selectIndex] = 0;
            coloredNode.put(nodesId[selectIndex], -1);
        }
        return nodesId[selectIndex];
    }

    protected void copyArray(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }

    protected boolean isColorPlanFeasible(int[] colorPlan) {
          if (colorPlan.length != V) {
              return false;
          }
          for (int i = 0; i < V; i++) {
              for (Integer neigh : adjList.get(i)) {
                  if (colorPlan[neigh] == colorPlan[i]) {
                      return false;
                  }
              }
          }
          return true;
    }


    protected void adjustColorToRemoveSymmetry(int[] colorPlan, int colorNum){
        List<List<Integer>> colorSet = new ArrayList<>(colorNum);
        for (int i = 0; i < colorNum; i++) {
            List<Integer> nodes = new ArrayList<>();
            colorSet.add(nodes);
        }
        for (int i = 0; i < V; i++) {
            colorSet.get(colorPlan[i]).add(i);
        }

        for (int i = 0; i < colorNum; i++) {
            if (colorSet.get(i).get(0) < i) {
                swapColor(colorSet, i);
            }
        }

        reviseColorPlanOnColorSet(colorPlan, colorSet);

        System.out.println("Test");
    }

    private void reviseColorPlanOnColorSet(int[] colorPlan, List<List<Integer>> colorSet) {
        for (int colorId = 0; colorId < colorSet.size(); colorId++) {
            for (Integer nodeId : colorSet.get(colorId)) {
                colorPlan[nodeId] = colorId;
            }
        }
    }

    private void swapColor(List<List<Integer>> colorSet, int colorId) {
        int minNodeId = colorSet.get(colorId).get(0);
        for (int i = 0; i <= minNodeId; i++) {
            int firstNode = colorSet.get(i).get(0);
            if (firstNode >= colorId) {
                List<Integer> tmp = new ArrayList<>();
                copyList(colorSet.get(colorId), tmp);
                copyList(colorSet.get(i), colorSet.get(colorId));
                copyList(tmp, colorSet.get(i));
                return;
            }
        }
    }

    private void copyList(List<Integer> src, List<Integer> dest) {
        dest.clear();
        for (Integer item : src) {
            dest.add(item);
        }
    }

}