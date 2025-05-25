package park1;

import javax.swing.*;
import java.util.*;
import java.util.List;

import javax.swing.Timer;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class Dijkstracheck {
    static final int WIDTH = 85, HEIGHT = 38;
    static boolean[][] obstacles = new boolean[HEIGHT][WIDTH];
    //public static int i;
    //public static int j;
    List<Node> placesDisponibles = new LinkedList();
    static String cheminFichier = "/Users/Asus/eclipse-workspace/park1/src/park1/places4.txt";
    static String fichierObstacles = "/Users/Asus/eclipse-workspace/park1/src/park1/obstacles4.txt";
    public static void main(String[] args) {
        // Exemple d'obstacles
    	   
    	 new Dijkstracheck(); 
    }
    
    public Dijkstracheck() {
    	
    	
    	List<Node> placesDisponibles = chargerPlacesDepuisFichier(cheminFichier);
    	
    	try {
            chargerDepuisFichiernode(cheminFichier);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fichier introuvable !");
            e.printStackTrace();
        }
    	
    	try {
    		chargerDepuisFichierobstacles(fichierObstacles);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Fichier introuvable !");
            e.printStackTrace();
        }
    	
    	 SwingUtilities.invokeLater(() -> {
             JFrame frame = new JFrame("Robot Parking - Dijkstra");
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             frame.setLayout(new BorderLayout());
             //JPanel p = new JPanel();
             //p.setLayout(new GridLayout(1,2));
             
             Node start = new Node(59, 37);
             //Node start2 = new Node(29,8);
             Node sorti = new Node(57,37);
             //List<Node> dest = List.of( new Node(29,8));
             Node[] robotPosition = { new Node(59, 37) };
             
             GridPanel panel = new GridPanel(HEIGHT, WIDTH, obstacles, null, robotPosition[0], null, placesDisponibles, start);
            
             frame.add(panel, BorderLayout.CENTER);

             frame.pack();
             frame.setLocationRelativeTo(null);
             frame.setVisible(false);
         });
    	 
    }
    public static Node trouverPlaceLaPlusProche(Node start, List<Node> places) {
        Node meilleure = null;
        int meilleurCout = Integer.MAX_VALUE;
        for (Node goal : places) {
        	if (goal.disponibilite == 0) {
        		List<Node> chemin = dijkstra(start, goal, places);
        		if (!chemin.isEmpty() && chemin.size() < meilleurCout) {
        			meilleurCout = chemin.size();
        			meilleure = goal;
        			
        		}
        		
        	}else {
        		System.out.println("pas assez de places");
        	}
        	
        }
        if (meilleure != null) {
            meilleure.disponibilite = 1;
            
            
         
        }
        return meilleure;
        
    }
    

    
   public static boolean estUnePlaceDeParking(int x, int y, List<Node> placesDisponibles) {
        for (Node p : placesDisponibles) {
            if (p.x == x && p.y == y) return true;
        }
        return false;
    }

   public static List<Node> dijkstra(Node start, Node goal, List<Node> placesDisponibles) {
        int rows = obstacles.length;
        int cols = obstacles[0].length;
        int[][] distances = new int[rows][cols];
        Node[][] previous = new Node[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for (int[] row : distances) Arrays.fill(row, Integer.MAX_VALUE);
        distances[start.y][start.x] = 0;

        class PointAvecDistance {
            int x, y, dist;

            PointAvecDistance(int x, int y, int dist) {
                this.x = x;
                this.y = y;
                this.dist = dist;
            }
        }

        PriorityQueue<PointAvecDistance> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        queue.add(new PointAvecDistance(start.x, start.y, 0));

        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};

        while (!queue.isEmpty()) {
            PointAvecDistance current = queue.poll();
            if (visited[current.y][current.x]) continue;
            visited[current.y][current.x] = true;
            if (current.x == goal.x && current.y == goal.y) break;

            for (int[] d : dirs) {
                int nx = current.x + d[0], ny = current.y + d[1];
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows 
                    && !obstacles[ny][nx] 
                    && (!estUnePlaceDeParking(nx, ny, placesDisponibles) || (nx == goal.x && ny == goal.y))) {
                    
                    int newDist = distances[current.y][current.x] + 1;
                    if (newDist < distances[ny][nx]) {
                        distances[ny][nx] = newDist;
                        previous[ny][nx] = new Node(current.x, current.y); // ou conserver une seule instance
                        queue.add(new PointAvecDistance(nx, ny, newDist));
                    }
                }
            }
        }

        List<Node> path = new ArrayList<>();
        Node step = goal;
        while (step != null && !(step.x == start.x && step.y == start.y)) {
            path.add(0, step);
            step = previous[step.y][step.x];
        }
        if (step != null) path.add(0, start);
        return path;
    }
    
 
    public static List<Node> chargerPlacesDepuisFichier(String cheminFichier) {
        List<Node> places = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            while (scanner.hasNextInt()) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int dispo = scanner.nextInt();
                Node node = new Node(x, y);
                node.disponibilite = dispo;
                places.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }
    
    public void chargerDepuisFichiernode(String cheminFichier) throws FileNotFoundException {
    	File file = new File(cheminFichier);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextInt()) {
            if (scanner.hasNextInt()) {
                int a = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    int b = scanner.nextInt();
                    if (scanner.hasNextInt()) {
                        int c = scanner.nextInt();
                        
                        Node t = new Node(a, b);
                        t.disponibilite = c;
                        placesDisponibles.add(t);
                    } else {
                        System.err.println("Erreur: valeur manquante pour la disponibilité.");
                        break;
                    }
                } else {
                    System.err.println("Erreur: valeur manquante pour y.");
                    break;
                }
            } else {
                System.err.println("Erreur: valeur manquante pour x.");
                break;
            }
        }
        
        scanner.close();
    }
    public void chargerDepuisFichierobstacles(String cheminFichier) throws FileNotFoundException {
    	File files = new File(fichierObstacles);
        Scanner scanner1 = new Scanner(files);

        while (scanner1.hasNextLine()) {
        	int c = scanner1.nextInt();
       	 	int d = scanner1.nextInt();
       	 	
       	 obstacles[d][c] = true;
        }
        
        scanner1.close();
    }

}
