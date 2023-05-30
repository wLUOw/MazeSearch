package view;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel {
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Cell(Type type, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.type = type;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        Color color = new Color(0, 0, 0, 0);
        if (type == Type.BLANK) color = new Color(255, 255, 255);
        if (type == Type.WALL) color = new Color(0, 0, 0);
        if (type == Type.PERSON) color = new Color(255, 218, 164);
        if (type == Type.STEP) color = new Color(255, 0, 0);
        g.setColor(color);
        g.fillRect(1, 1, this.getWidth() - 1, this.getHeight() - 1);
    }
}
