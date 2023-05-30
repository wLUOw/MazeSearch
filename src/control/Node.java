package control;

public class Node implements Comparable<Node> {
    public int x; // x坐标
    public int y; // y坐标
    int GValue = 0; // G值
    private int HValue = 0; // H值
    private int FValue = 0;
    Node parent;   // 父节点

    // Node对象的构造器
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void fValueInARA(int E) {
        int g = this.GValue;
        int eh = E * this.HValue;
        this.FValue = g + eh;
    }

    public int compareTo(Node node2) {  //比较两个Node对象的F值
        return Integer.compare(this.FValue, node2.FValue);
    }

    void calcH(Node end) {
        this.HValue = Math.abs(end.x - this.x) + Math.abs(end.y - this.y);
    }
}
