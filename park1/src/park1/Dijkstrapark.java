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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;


public class Dijkstrapark {
    static final int WIDTH = 85, HEIGHT = 38;
    static boolean[][] obstacles = new boolean[HEIGHT][WIDTH];
    //public static int i;
    //public static int j;
    List<Node> placesDisponibles = new LinkedList();
    static String cheminFichier = "/Users/Asus/eclipse-workspace/park1/src/park1/places4.txt";
    static String fichierObstacles = "/Users/Asus/eclipse-workspace/park1/src/park1/obstacles4.txt";
    public static void main(String[] args) {
        // Exemple d'obstacles
    	   
    	 new Dijkstrapark(); 
    }
    
    public Dijkstrapark() {
    	
    	
    	
    	
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
             JPanel p = new JPanel();
             p.setLayout(new GridLayout(1,2));
             
             Node start = new Node(59, 37);
             //Node start2 = new Node(29,8);
             Node sorti = new Node(59,37);
             //List<Node> dest = List.of( new Node(29,8));
             Node[] robotPosition = { new Node(59, 37) };
             
             GridPanel panel = new GridPanel(HEIGHT, WIDTH, obstacles, null, robotPosition[0], null, placesDisponibles, start);
            
             frame.add(panel, BorderLayout.CENTER);

             JButton bouton = new JButton("park");
             
             
             bouton.addActionListener(e -> {
                // executerDijkstraAvecAnimation(start, placesDisponibles, obstacles, panel);
            	 int[] infos = executerDijkstraAvecAnimation(start, placesDisponibles, obstacles, panel);
            	    //System.out.println("x : " + infos[0]);
            	   // System.out.println("y : " + infos[1]);
            	 int m= infos[0];
            	 int n= infos[1];
            	
                 
                String url = "jdbc:mysql://localhost:3306/base2";
                 String user = "root";
                 String password = "";

                 
                 try (Connection conn = DriverManager.getConnection(url, user, password)) {
                     // Requête de mise à jour
                     String sql = "UPDATE place SET x = ?, y = ? WHERE x = ? AND y = ?";
                     PreparedStatement stmt = conn.prepareStatement(sql);

                     // Valeurs à mettre à jour
                     stmt.setInt(1, m); // Nouvelle valeur pour l'email
                     stmt.setInt(2, n); // Nom de l'utilisateur à modifier
                     stmt.setInt(3, 0);
                     stmt.setInt(4, 0);
                     int lignesModifiees = stmt.executeUpdate();

                     System.out.println(lignesModifiees + " ligne(s) modifiée(s).");

                 } catch (SQLException l) {
                     l.printStackTrace();
                 }
            	 
            	 
             });
            
             p.add(bouton);
             frame.add(p, BorderLayout.SOUTH);
             

             frame.pack();
             frame.setLocationRelativeTo(null);
             frame.setVisible(true);
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
            majDisponibiliteFichier(cheminFichier, meilleure);
 
        }
        return meilleure;
        
    }
    
 
    
    public static int[] executerDijkstraAvecAnimation(Node startNode, List<Node> places, boolean[][] obstacles, GridPanel panel) {
        Node goal = trouverPlaceLaPlusProche(startNode, places);
        
        List<Node> path = dijkstra(startNode, goal, places);
        Node[] robotPosition = { new Node(startNode.x, startNode.y) };
        panel.setGoal(goal);
        panel.setPath(path);
        panel.setStart(robotPosition[0]);
        panel.repaint();

        Timer timer = new Timer(200, null);
        ActionListener listener = new ActionListener() {
            int index = 0;

            public void actionPerformed(ActionEvent e) {
                if (index < path.size()) {
                    Node next = path.get(index);
                    afficherInstruction(robotPosition[0], next);
                    robotPosition[0].x = next.x;
                    robotPosition[0].y = next.y;
                    panel.setStart(robotPosition[0]);
                    panel.repaint();
                    index++;
                } else {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Arrivé à la place !");
                }
            }
        };
        timer.addActionListener(listener);
        timer.start();
        int x = goal.getX();
        int y = goal.getY();
        return new int[] { x, y };
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
   
    
   
    public static void afficherInstruction(Node from, Node to) {
        int dx = to.x - from.x, dy = to.y - from.y;
        if (dx == 1) System.out.println("Instruction : Aller à DROITE");
        else if (dx == -1) System.out.println("Instruction : Aller à GAUCHE");
        else if (dy == 1) System.out.println("Instruction : Aller en BAS");
        else if (dy == -1) System.out.println("Instruction : Aller en HAUT");
    
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
    
    public static void majDisponibiliteFichier(String cheminFichier, Node nodeAMettreAJour) {
        try {
            // Lire tout le fichier dans une liste
            List<String> donnees = new ArrayList<>();
            Scanner scanner = new Scanner(new File(cheminFichier));
            while (scanner.hasNext()) {
                donnees.add(scanner.next());
            }
            scanner.close();

            // Parcourir les triplets (x, y, dispo) → tous les 3 éléments
            for (int i = 0; i < donnees.size(); i += 3) {
                int x = Integer.parseInt(donnees.get(i));
                int y = Integer.parseInt(donnees.get(i + 1));

                if (x == nodeAMettreAJour.getX() && y == nodeAMettreAJour.getY()) {
                    // Modifier la disponibilité à 1
                    donnees.set(i + 2, "1");
                    break;
                }
            }

            // Réécriture dans le fichier
            FileWriter writer = new FileWriter(cheminFichier, false); // overwrite
            for (String val : donnees) {
                writer.write(val + " ");
            }
            writer.close();

            System.out.println("Fichier mis à jour avec succès.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void unparkPlaceSansVirgule(int xTarget, int yTarget, String fichier) {
        try {
            // Lire toute la ligne du fichier
            BufferedReader reader = new BufferedReader(new FileReader(fichier));
            String ligne = reader.readLine();
            reader.close();

            // Diviser tous les nombres
            String[] tokens = ligne.split(" ");

            // Parcourir les données par groupes de 3 (x y dispo)
            for (int i = 0; i < tokens.length; i += 3) {
                int x = Integer.parseInt(tokens[i]);
                int y = Integer.parseInt(tokens[i + 1]);

                // Trouver la place correspondante
                if (x == xTarget && y == yTarget) {
                    tokens[i + 2] = "0"; // Modifier la disponibilité à 0
                    break;
                }
            }

            // Recomposer la ligne modifiée
            String nouvelleLigne = String.join(" ", tokens);

            // Réécrire dans le fichier
            BufferedWriter writer = new BufferedWriter(new FileWriter(fichier));
            writer.write(nouvelleLigne);
            writer.close();

            System.out.println("Place (" + xTarget + "," + yTarget + ") libérée !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
