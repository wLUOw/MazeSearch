package view;

import control.Node;
import control.Person;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MazeView extends JComponent {
    private Cell[][] grids;
    private final int SIZE;
    private Person person;
    private int time;

    public MazeView(int chessSize, int m, int n, Person person) {
        this.person = person;
        time = person.totalTime;
        grids = new Cell[m][n];
        SIZE = chessSize;
        int width = SIZE * n;
        int height = SIZE * m;
        setLayout(null);
        setSize(width, height);

        initGrids();
    }

    private void initGrids() {
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[0].length; j++) {
                if (person.originMap[i][j] == 0) {
                    Cell cell = new Cell(Type.BLANK, calculatePoint(i, j), SIZE);
                    this.add(cell);
                    grids[i][j] = cell;
                } else if (person.originMap[i][j] == 1) {
                    Cell cell = new Cell(Type.WALL, calculatePoint(i, j), SIZE);
                    this.add(cell);
                    grids[i][j] = cell;
                }
                grids[i][j].repaint();
                grids[i][j].revalidate();
            }
        }
        setPath(this.time);
    }

    void setUpGrids(int time) {
        Node node = person.getPersonCoordinateAtTimeT(time);
        for (int i = node.x - 1; i <= node.x + 1; i++) {
            for (int j = node.y - 1; j <= node.y + 1; j++) {
                if (i >= 0 && i < grids.length && j >= 0 && j < grids[0].length) {
                    if (i == person.getPersonCoordinateAtTimeT(time).x
                            && j == person.getPersonCoordinateAtTimeT(time).y) {
                        grids[i][j].setType(Type.PERSON);
                    } else if (person.originMap[i][j] == 0) {
                        grids[i][j].setType(Type.BLANK);
                    } else if (person.originMap[i][j] == 1) {
                        grids[i][j].setType(Type.WALL);
                    }
                    grids[i][j].repaint();
                    grids[i][j].revalidate();
                }
            }
        }
        if (person.magic.containsKey(time)) {
            Node wall = person.magic.get(time);
            grids[wall.x][wall.y].setType(Type.WALL);
            grids[wall.x][wall.y].repaint();
            grids[wall.x][wall.y].revalidate();
        }
        setPath(time);
    }

    private void setPath(int time) {
        if (time < person.totalTime) {
            Node personNode = person.getPersonCoordinateAtTimeT(time + 1);
            grids[personNode.x][personNode.y].setType(Type.BLANK);
            grids[personNode.x][personNode.y].repaint();
            grids[personNode.x][personNode.y].revalidate();
            List<Node> lastList = person.getStepListAtTimeT(time + 1);
            for (Node node : lastList) {
                int x = node.x;
                int y = node.y;
                if (x == person.getPersonCoordinateAtTimeT(time).x
                        && y == person.getPersonCoordinateAtTimeT(time).y) {
                    continue;
                }
                grids[x][y].setType(Type.BLANK);
                grids[x][y].repaint();
                grids[x][y].revalidate();
            }
        }

        Node personNode = person.getPersonCoordinateAtTimeT(time);
        grids[personNode.x][personNode.y].setType(Type.PERSON);
        grids[personNode.x][personNode.y].repaint();
        grids[personNode.x][personNode.y].revalidate();
        List<Node> list = person.getStepListAtTimeT(time);
        for (Node node : list) {
            int x = node.x;
            int y = node.y;
            if (x == person.getPersonCoordinateAtTimeT(time).x
                    && y == person.getPersonCoordinateAtTimeT(time).y) {
                continue;
            }
            grids[x][y].setType(Type.STEP);
            grids[x][y].repaint();
            grids[x][y].revalidate();
        }
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * SIZE, row * SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
