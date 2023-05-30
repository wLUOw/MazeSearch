import control.Maze;
import control.Node;
import control.Person;
import view.MazeFrame;

import java.util.Scanner;

public class Main {
    private static Maze readData() {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();   // 地图的高 ,m行n列
        int n = sc.nextInt();   // 地图的宽
        int e = sc.nextInt();   // 总时间
        int[][] originalMap = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                originalMap[i][j] = sc.nextInt();
            }
        }
        int numOfMagic = sc.nextInt();  // 魔法师施展魔法的次数
        int[][] magic = new int[numOfMagic][3];
        for (int i = 0; i < numOfMagic; i++) {
            for (int j = 0; j < 3; j++) {
                magic[i][j] = sc.nextInt();
            }
        }
        int numOfAsk = sc.nextInt();
        int[] askTime = new int[numOfAsk];
        for (int j = 0; j < numOfAsk; j++) {
            askTime[j] = sc.nextInt();
        }
        return new Maze(originalMap, magic, askTime, e);
    }

    public static void main(String[] args) {
        Maze maze = readData();
        Person nike = new Person(maze.start, maze.e, maze);  // 初始位置在起点的nike,初始时间为e

        while (true) {
            // magic(if has) -> findPath -> ask(if has) -> move -> magic(next)
            // 时间耗尽或到达终点时停止
            if (nike.currentTime < 0 || (nike.personX == maze.height - 1 && nike.personY == maze.width - 1)) {
                nike.action();
                System.out.println(nike.steps.size());
                for (Node node : nike.steps) {
                    System.out.print(node.x + " " + node.y + " ");
                }
                System.out.println();
                break;
            } else {  //否则进行一轮行动
                nike.action();
            }
        }

        new MazeFrame(maze.height, maze.width, nike);
    }
}