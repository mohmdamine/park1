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


	public class CreeParking {
	    static final int WIDTH = 35, HEIGHT = 20;
	    static boolean[][] obstacles = new boolean[HEIGHT][WIDTH];
	    //public static int i;
	    //public static int j;
	    List<Node> placesDisponibles = new LinkedList();
	    public static void main(String[] args) {
	        // Exemple d'obstacles
	    	   
	    	 new CreeParking(); 
	    }
	    
	    public CreeParking() {
	    	
	    	
	    	 SwingUtilities.invokeLater(() -> {
	             JFrame frame = new JFrame("Robot Parking - Dijkstra");
	             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	             frame.setLayout(new BorderLayout());
	             //JPanel p = new JPanel();
	             //p.setLayout(new GridLayout(1,2));
	             JButton b1 = new JButton("choisir les obstacles");
	             JButton b2 = new JButton("choisir places disponibles");
	             
	             //Node start = new Node(1, 4);
	            
	             //Node start2 = new Node(29,8);
	             //Node sorti = new Node(1,12);
	             //List<Node> dest = List.of( new Node(29,8));
	             //Node[] robotPosition = { new Node(4, 4) };
	             JPanel p = new JPanel();
	             GridCree panel = new GridCree(HEIGHT, WIDTH, obstacles, null, null, placesDisponibles);
	             JButton saveButton = new JButton("Save");
	             saveButton.addActionListener(e -> {
	                 try {
	                     saveToFile(panel);
	                     JOptionPane.showMessageDialog(frame, "Fichiers sauvegardés avec succès !");
	                 } catch (IOException ex) {
	                     ex.printStackTrace();
	                     JOptionPane.showMessageDialog(frame, "Erreur lors de la sauvegarde.");
	                 }
	             });
	             p.add(saveButton); // ajoute le bouton dans le panneau bas
	             frame.add(panel, BorderLayout.CENTER);
	             frame.add(p,BorderLayout.SOUTH);
	             frame.pack();
	             frame.setLocationRelativeTo(null);
	             frame.setVisible(true);
	         });
	    	 
	    }
	    
	    public static void saveToFile(GridCree panel) throws IOException {
	        // Sauvegarde des obstacles (format : x y x y ...)
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Asus\\eclipse-workspace\\park1\\src\\park1\\obstacles4.txt"))) {
	            boolean[][] obstacles = panel.getObstacles();
	            StringBuilder sb = new StringBuilder();
	            for (int y = 0; y < obstacles.length; y++) {
	                for (int x = 0; x < obstacles[0].length; x++) {
	                    if (obstacles[y][x]) {
	                        sb.append(x).append(" ").append(y).append(" ");
	                    }
	                }
	            }
	            writer.write(sb.toString().trim()); // trim pour supprimer l'espace final
	        }

	        // Sauvegarde des places disponibles (format : x y disponibilite x y disponibilite ...)
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Asus\\eclipse-workspace\\park1\\src\\park1\\places4.txt"))) {
	            List<Node> places = panel.getPlacesDisponibles();
	            StringBuilder sb = new StringBuilder();
	            for (Node n : places) {
	                sb.append(n.x).append(" ").append(n.y).append(" ").append(n.disponibilite).append(" ");
	            }
	            writer.write(sb.toString().trim());
	        }
	    }
	    

	  
	    

}
