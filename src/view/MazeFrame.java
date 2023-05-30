package view;

import control.Person;

import javax.swing.*;
import java.awt.*;

public class MazeFrame extends JFrame {
    private final int m;
    private final int n;
    private final int SIZE;
    private int time;

    private Person person;
    private MazeView mazeView;
    private JLabel timeLabel;

    public MazeFrame(int m, int n, Person person) {
        setTitle("Maze");
        this.SIZE = Math.min(680 / m, 680 / n);
        this.m = m;
        this.n = n;
        time = person.totalTime;
        this.person = person;

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        addTimeLabel();
        addQueryButton();
        addMazeView();

        this.setVisible(true);
    }

    private void addMazeView() {
        mazeView = new MazeView(SIZE, m, n, person);
        mazeView.setLocation(50, 50);
        add(mazeView);
    }

    private void addTimeLabel() {
        timeLabel = new JLabel("Time: " + person.totalTime);
        timeLabel.setLocation(800,50);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(timeLabel);
    }

    private void addQueryButton() {
        JButton button = new JButton("Next");
        button.addActionListener((e) -> {
            if (time >= 0) {
                time--;
                try {
                    mazeView.setUpGrids(time);
                } catch (Exception exception) {
                    System.out.print("");
                }
                timeLabel.setText("Time: " + time);
            } else {
                JOptionPane.showMessageDialog(null,"Time is up");
            }
        });
        button.setLocation(800, 130);
        button.setSize(150, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
}
