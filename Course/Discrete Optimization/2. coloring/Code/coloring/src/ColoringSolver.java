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
            for (Integer neighbor : adjList.get(i)) {
                nodesDeg[i]++;
                nodesDeg[neighbor]++;
            }
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
        if (colorOfNodes.length != V) {
            return -1;
        }
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

    protected boolean isValid(int nodeId, int colorId, int[] colorOfNodes) {
        for (Integer neighbor : adjList.get(nodeId)) {
            if (colorId == colorOfNodes[neighbor]) {
                return false;
            }
        }
        return true;
    }

    //Use the greedy algorithm based on node degree to create the initial solution
    protected void initialColorPlan(int[] bestColorPlan) {
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

    protected void copyArray(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }


}