package control;

import java.util.*;

public class Maze {
    public int e;
    public int height;
    public int width;

    public final Node start; // 起点
    private final Node end; // 终点

    final int[][] nodeBoard;
    private Node[][] nodeBoardNode;
    final int[][] magic;
    private final int[] askTime;
    private static final int[][] dir = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
            {0, 1}, {1, -1}, {1, 0}, {1, 1}}; // 方向数组

    private PriorityQueue<Node> openList = new PriorityQueue<>(); // 开启列表，存放探索到但是没走或即将走的Node
    private Set<Node> closeList = new HashSet<>();

    // 迷宫的构造器，输入为长宽和障碍物矩阵，魔法矩阵，询问矩阵，初始e（时间），对迷宫初始化
    public Maze(int[][] originalMap, int[][] magic, int[] askTime, int e) {
        this.height = originalMap.length;// 矩阵的行数
        this.width = originalMap[0].length;// 矩阵的列数
        this.nodeBoard = originalMap;
        this.magic = magic;
        this.askTime = askTime;
        this.e = e;

        nodeBoardNode = new Node[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                nodeBoardNode[i][j] = new Node(i, j);
            }
        }
        start = nodeBoardNode[0][0];
        end = nodeBoardNode[height - 1][width - 1];

        start.GValue = 0;  // 起点的G值
        start.calcH(end);
    }

    // 判断迷宫某个位置是否可以达到
    private boolean inBound(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

    // 判断迷宫某个节点是否为障碍物
    private boolean isObstacle(int x, int y) {
        if (x >= 0 && x < height && y >= 0 && y < width)
            return nodeBoard[x][y] == 1;
        return true;
    }

    // 返回在maze迷宫中，从点node可以走到的所有点
    private static List<Node> getCanGoNode(Node node, Maze maze) {
        List<Node> canGoList = new ArrayList<>();
        int x = node.x;
        int y = node.y;
        for (int[] direction : dir) {
            int dx = direction[0];
            int dy = direction[1];
            int neighborX = x + dx;
            int neighborY = y + dy;
            if (maze.inBound(neighborX, neighborY) && !maze.isObstacle(neighborX, neighborY)) {
                Node canGo = maze.nodeBoardNode[neighborX][neighborY];
                canGo.calcH(maze.end);
                canGoList.add(canGo);  // 每次探索可行点都会创建新的node对象
            }
        }
        return canGoList;
    }

    public List<Node> findMinPathByARA(Node start, int currentE) {
        openList.clear();
        closeList.clear();
        start.GValue = 0;
        start.parent = null;
        start.fValueInARA(currentE);
        openList.add(start);
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();  // 取出F最小的点
            if (currentNode.equals(end)) {
                List<Node> minPath = new ArrayList<>();
                while (currentNode != null) {
                    minPath.add(currentNode);
                    currentNode = currentNode.parent;
                }
                Collections.reverse(minPath);
                return minPath;
            }
            closeList.add(currentNode);
            // 遍历能走的node
            for (Node canGoNode : getCanGoNode(currentNode, this)) {
                if (closeList.contains(canGoNode)) {
                    continue;
                }
                // 从起点实际走到这个点的代价值，除了最后一步，其他前面所有步是已经走过的
                int actualNewG = currentNode.GValue + 1;
                // 如果邻居节点不在开放列表中，或者在开放列表中（肯定已经有了非0的g）但是当前路径的G更短
                if (!openList.contains(canGoNode) || canGoNode.GValue > actualNewG) {
                    canGoNode.GValue = actualNewG;
                    canGoNode.calcH(end);
                    canGoNode.fValueInARA(currentE);
                    canGoNode.parent = currentNode;
                    if (!openList.contains(canGoNode)) {
                        openList.add(canGoNode);
                    }
                }
            }
        }
        return null; // 没有这样一条路径
    }

    // 判断当前时间是否是魔法时间，如果是，返回int[]数组，分别为施法时间，坐标;如果不是，返回null
    int[] isMagicTime(int currentTime) {
        for (int[] ints : magic) {
            if (ints[0] == currentTime) {
                return ints;
            }
        }
        return null;
    }

    int isAskTime(int currentTime) {
        for (int time : askTime) {
            if (time == currentTime) {
                return time;
            }
        }
        return -1;
    }
}
