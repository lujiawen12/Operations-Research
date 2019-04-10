public class ColoringSolution {
    private String approach;
    private int obj;
    private int[] colorOfNodes;

    public void print() {
        System.out.println(obj+" 0");
        for (int color : colorOfNodes) {
            System.out.print(color + " ");
        }
        System.out.println();
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getApproach() {
        return approach;
    }

    public void setObj(int obj) {
        this.obj = obj;
    }

    public int getObj() {
        return obj;
    }

    public void setColorOfNodes(int[] colorOfNodes) {
        this.colorOfNodes = colorOfNodes;
    }

    public int[] getColorOfNodes() {
        return colorOfNodes;
    }
}
