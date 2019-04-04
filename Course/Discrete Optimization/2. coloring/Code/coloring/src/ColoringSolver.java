import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ColoringSolver {
    protected int V;
    protected int E;
    protected List<List<Integer>> adjList;

    protected ColoringSolver(int V, int E, List<List<Integer>> adjList) {
        this.V = V;
        this.E = E;
        this.adjList = adjList;
    }

    public abstract ColoringSolution solve();

    protected boolean isMeetConstraint(int[] colorOfNodes, List<List<Integer>> adjList) {
        for (int i = 0; i < V; i++) {
            int curColor = colorOfNodes[i];
            for (Integer adjNode : adjList.get(i)) {
                if (colorOfNodes[adjNode] == curColor) {
                    return false;
                }
            }
        }
        return true;
    }

    protected int calColorNum(int[] colorOfNodes) {
        int colorNum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int curColor : colorOfNodes) {
            if (!map.containsKey(curColor)) {
                colorNum ++;
                map.put(curColor, -1);
            }
        }
        return colorNum;
    }
}
