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
    protected List<Integer> nodesIdDescendByDegree;

    protected ColoringSolver(int V, int E, List<List<Integer>> adjList) {
        this.V = V;
        this.E = E;
        this.adjList = adjList;
        this.nodesDegreeMap = new HashMap<>(V);
        this.nodesIdDescendByDegree = new ArrayList<>(V);
        calNodeDegree();
    }

    public abstract ColoringSolution solve();

    private void calNodeDegree() {
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
            nodesDegreeMap.put(node.id, node.nodeDeg);
            nodesIdDescendByDegree.add(node.id);
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
}