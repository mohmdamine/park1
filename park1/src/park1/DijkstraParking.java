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


public class DijkstraParking {
    static final int WIDTH = 30, HEIGHT = 13;
    static boolean[][] obstacles = new boolean[HEIGHT][WIDTH];
    //public static int i;
    //public static int j;
    List<Node> placesDisponibles = new LinkedList();
    static String cheminFichier = "/Users/Asus/eclipse-workspace/park1/src/park1/node.txt";
    static String fichierObstacles = "/Users/Asus/eclipse-workspace/park1/src/park1/obstacles.txt";
    public static void main(String[] args) {
        // Exemple d'obstacles
    	   
    	 new DijkstraParking(); 
    }
    
    public DijkstraParking() {
    	
    	
    	
    	
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
             JFrame frame = new JFrame("Robot Dijkstra");
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             frame.setLayout(new BorderLayout());
             
             Node start = new Node(28, 12);
             Node sorti = new Node(1,12);
             Node[] robotPosition = { new Node(28, 12) };
             
             GridPanel panel = new GridPanel(HEIGHT, WIDTH, obstacles, null, robotPosition[0], null, placesDisponibles, start);
            
             frame.add(panel, BorderLayout.CENTER);

  
             frame.pack();
             frame.setLocationRelativeTo(null);
             frame.setVisible(true);
         });
    	 
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
       	 	
       	 obstacles[c][d] = true;
        }
        
        scanner1.close();
    }
    
}
