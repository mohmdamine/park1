package park1;

import javax.swing.*;


import java.awt.*;
import java.util.List;

public class GridPanel extends JPanel {
    private int rows, cols, cellSize = 40;
    private boolean[][] obstacles;
    private List<Node> path,path2;
    private Node start, goal, startFixed;
    private List<Node> placesDisponibles;

    public GridPanel(int rows, int cols, boolean[][] obstacles, List<Node> path, Node start, Node goal, List<Node> placesDisponibles, Node startFixed) {
        this.rows = rows;
        this.cols = cols;
        this.obstacles = obstacles;
        this.path = path;
        this.start = start;
        this.goal = goal;
        this.placesDisponibles = placesDisponibles;
        this.startFixed = startFixed;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
    }
  
    public void setStart(Node newStart) {
        this.start = newStart;
    }

    public void setPath(List<Node> newPath) {
        this.path = newPath;
    }
     
    public void setPath2(List<Node> newPath) {
        this.path2 = newPath;
    }

    public void setGoal(Node newGoal) {
        this.goal = newGoal;
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                g.setColor(Color.WHITE);
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);

                if (obstacles[y][x]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        if (path != null) {
            for (Node node : path) {
                g.setColor(new Color(173, 216, 230));
                g.fillRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
            }
        }
        if (path2 != null) {
            for (Node node : path2) {
                g.setColor(new Color(173, 216, 230));
                g.fillRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
            }
        }

        if (placesDisponibles != null) {
            for (Node place : placesDisponibles) {
                if (!place.equals(goal)) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(place.x * cellSize, place.y * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(place.x * cellSize, place.y * cellSize, cellSize, cellSize);
                }
            }
        }

        if (goal != null) {
            g.setColor(Color.RED);
            g.fillRect(goal.x * cellSize, goal.y * cellSize, cellSize, cellSize);
        }

        if (startFixed != null) {
            g.setColor(Color.GREEN);
            g.fillRect(startFixed.x * cellSize, startFixed.y * cellSize, cellSize, cellSize);
            g.setColor(Color.GRAY);
            g.drawRect(startFixed.x * cellSize, startFixed.y * cellSize, cellSize, cellSize);
        }

        if (start != null && !start.equals(goal)) {
            g.setColor(new Color(0, 0, 128));
            g.fillOval(start.x * cellSize + 3, start.y * cellSize + 3, cellSize - 7, cellSize - 7);
        }
        
        if (placesDisponibles != null) {
            for (Node node : placesDisponibles) {
                if (!obstacles[node.y][node.x]) {
                    if (node.disponibilite==0) {
                        g.setColor(Color.GREEN); // Disponible
                    } else {
                        g.setColor(Color.RED);// Occupée
                    }
                    g.fillRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(node.x * cellSize, node.y * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}
