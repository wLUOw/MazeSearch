package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
    public int personX;
    public int personY;

    private Node personNode;   // 人的当前位置节点

    public int currentTime;   // 当前的时间e
    public int totalTime;

    public final int[][] originMap;
    private List<Node>[] everyTimeSteps;
    public List<Node> steps;
    public Map<Integer, Node> magic;
    private Node[] personCoordinate;
    private Maze maze;

    public Person(Node startNode, int startTime, Maze maze) {  // 构造器
        this.personNode = startNode;
        this.currentTime = startTime;
        this.totalTime = startTime;
        this.everyTimeSteps = new ArrayList[startTime + 1];
        this.personCoordinate = new Node[startTime + 1];
        this.originMap = maze.nodeBoard;
        this.maze = maze;
        this.steps = new ArrayList<>();
        this.steps.add(startNode);

        this.magic = new HashMap<>();
        for (int[] magic : maze.magic) {
            this.magic.put(magic[0], new Node(magic[1], magic[2]));
        }
    }

    public void action() {    // 人进行一轮行动
        // 如果魔法师施法
        if (maze.isMagicTime(currentTime) != null) {
            int[] currentMagic = maze.isMagicTime(currentTime);
            int newObstacleX = currentMagic[1];
            int newObstacleY = currentMagic[2];
            // 小妮可自身会抵御魔法攻击
            if (newObstacleX != personX || newObstacleY != personY) {
                maze.nodeBoard[newObstacleX][newObstacleY] = 1;
            } else {
                maze.nodeBoard[newObstacleX][newObstacleY] = 0;
            }
        }

        // 寻找最短路径，需要修改
        List<Node> minPathForTest = maze.findMinPathByARA(personNode, currentTime);

        if (currentTime >= 0) {
            List<Node> minPathClone = new ArrayList<>();
            for (Node node : minPathForTest) {
                Node cloneNode = new Node(node.x, node.y);
                minPathClone.add(cloneNode);
            }
            everyTimeSteps[currentTime] = minPathClone;
            personCoordinate[currentTime] = new Node(personX, personY);
        }

        // 如果坤坤询问,就输出
        if (maze.isAskTime(currentTime) >= 0) {
            if (minPathForTest != null) {
                System.out.println(minPathForTest.size());
                for (Node node : minPathForTest) {
                    System.out.print(node.x + " " + node.y + " ");
                }
                System.out.println();
            } else {
                System.out.println(0);
                System.out.println();
                return;
            }
        }

        move(minPathForTest); // 行动
        currentTime--;
    }

    public void move(List<Node> minPath) {
        Node nextNode;
        if (minPath == null) {
            return;
        }
        if (minPath.size() > 1) {
            nextNode = minPath.get(1);
        }
        else {
            nextNode = minPath.get(0);
        }
        personX = nextNode.x;
        personY = nextNode.y;
        personNode = nextNode;
        steps.add(nextNode);
    }

    // 剩余时间为t时的到终点路线
    public List<Node> getStepListAtTimeT(int t){
        return everyTimeSteps[t];
    }

    // 剩余时间为t时person的位置
    public Node getPersonCoordinateAtTimeT(int t){
        return personCoordinate[t];
    }
}
