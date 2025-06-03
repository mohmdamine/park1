package park1;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

public class GridCree extends JPanel {
    private int rows, cols, cellSize = 40;
    private boolean[][] obstacles;
    private List<Node> path,path2;
    private Node start, goal, startFixed;
    private List<Node> placesDisponibles;

    public GridCree(int rows, int cols, boolean[][] obstacles, List<Node> path, Node goal, List<Node> placesDisponibles) {
        this.rows = rows;
        this.cols = cols;
        this.obstacles = obstacles;
        this.path = path;
        this.start = start;
        this.goal = goal;
        this.placesDisponibles = placesDisponibles;
        this.startFixed = startFixed;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                if (x >= 0 && x < cols && y >= 0 && y < rows) {
                    // CLIC GAUCHE : toggle obstacle (noir <-> blanc)
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        obstacles[y][x] = !obstacles[y][x];
                        if (obstacles[y][x]) {
                            // Supprimer si c'était une place disponible
                            placesDisponibles.removeIf(n -> n.x == x && n.y == y);
                        }
                    }

                    // CLIC DROIT : toggle place disponible -> rouge/vert <-> blanc
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        boolean trouve = false;
                        Iterator<Node> it = placesDisponibles.iterator();
                        while (it.hasNext()) {
                            Node node = it.next();
                            if (node.x == x && node.y == y) {
                                it.remove(); // supprimer la place -> devient blanc
                                trouve = true;
                                break;
                            }
                        }

                        if (!trouve) {
                            Node nouvellePlace = new Node(x, y);
                            nouvellePlace.disponibilite = 0; // disponible
                            placesDisponibles.add(nouvellePlace);
                        }

                        // S'assurer que ce n'est pas un obstacle
                        obstacles[y][x] = false;
                    }

                    repaint();
                }
            }
        });
    }
    
    public boolean[][] getObstacles() {
        return obstacles;
    }

    public List<Node> getPlacesDisponibles() {
        return placesDisponibles;
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
            g.fillOval(start.x * cellSize + 10, start.y * cellSize + 10, cellSize - 20, cellSize - 20);
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

