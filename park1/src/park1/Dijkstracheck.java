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
	    JFrame frame;
	    JTextField wid, hei, placesfich, obstaclesfich;
	    GridCree panel;
	    JPanel p;  
	    JPanel nord;
	    JPanel centerPanel;
	    List<Node> placesDisponibles = new LinkedList<>();
	    JScrollPane scrollPane;
	    JButton sauvegarder;

	    public static void main(String[] args) {
	        new CreeParking(); 
	    }

	    public CreeParking() {
	        SwingUtilities.invokeLater(() -> {
	        	
	            frame = new JFrame("Robot Parking - Dijkstra");
	            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            frame.setLayout(new BorderLayout());

	            JLabel labelplaces = new JLabel("Entrer fichier des places:");
	            placesfich = new JTextField();
	            JLabel labelobstacles = new JLabel("Entrer fichier des obstacles:");
	            obstaclesfich = new JTextField();
	            JLabel labelwidth = new JLabel("Entrer la largeur du parking:");
	            wid = new JTextField();
	            JLabel labelheight = new JLabel("Entrer la hauteur du parking:");
	            hei = new JTextField();

	            JPanel input = new JPanel(new GridLayout(4, 2, 5, 5));
	            input.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	            input.add(labelplaces);
	            input.add(placesfich);
	            input.add(labelobstacles);
	            input.add(obstaclesfich);
	            input.add(labelwidth);
	            input.add(wid);
	            input.add(labelheight);
	            input.add(hei);

	            JButton appliquer = new JButton("Appliquer");
	            JPanel appbutton = new JPanel();
	            appbutton.add(appliquer);

	            nord = new JPanel();
	            nord.setLayout(new BoxLayout(nord, BoxLayout.Y_AXIS));
	            nord.add(input);
	            nord.add(appbutton);

	            p = new JPanel();
	            

	            // Panel central qui va contenir le GridCree
	            centerPanel = new JPanel(new BorderLayout());
	            frame.add(centerPanel, BorderLayout.CENTER);
	            
	            appliquer.addActionListener(e -> updateGrid());

	           
	            
	            frame.add(nord, BorderLayout.NORTH);
	            
	            frame.pack();
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	        });
	    }

	    private void updateGrid() {
	        try {
	            int width = Integer.parseInt(wid.getText());
	            int height = Integer.parseInt(hei.getText());

	            boolean[][] obstacles = new boolean[height][width];
	            placesDisponibles.clear();

	            if (scrollPane != null) {
	                centerPanel.remove(scrollPane);
	            }
	            sauvegarder = new JButton("Sauvegarder");
	            JPanel sud = new JPanel();
	            sud.add(sauvegarder);
	            panel = new GridCree(height, width, obstacles, null, null, placesDisponibles);

	            scrollPane = new JScrollPane(panel); // ajout du scroll
	            scrollPane.setPreferredSize(new Dimension(800, 600)); // taille de la vue visible
	            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // vitesse de scroll
	            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

	            centerPanel.add(scrollPane, BorderLayout.CENTER);
	            centerPanel.revalidate();
	            centerPanel.repaint();
	            frame.add(sud,BorderLayout.SOUTH);
	            frame.pack();
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(frame, "Veuillez entrer des nombres valides pour la largeur et la hauteur.");
	        }
	    }

	    public static void saveToFile(GridCree panel, String placesfichier, String obstaclesfichier) throws IOException {
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(obstaclesfichier))) {
	            boolean[][] obstacles = panel.getObstacles();
	            StringBuilder sb = new StringBuilder();
	            for (int y = 0; y < obstacles.length; y++) {
	                for (int x = 0; x < obstacles[0].length; x++) {
	                    if (obstacles[y][x]) {
	                        sb.append(x).append(" ").append(y).append(" ");
	                    }
	                }
	            }
	            writer.write(sb.toString().trim());
	        }

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(placesfichier))) {
	            List<Node> places = panel.getPlacesDisponibles();
	            StringBuilder sb = new StringBuilder();
	            for (Node n : places) {
	                sb.append(n.x).append(" ").append(n.y).append(" ").append(n.disponibilite).append(" ");
	            }
	            writer.write(sb.toString().trim());
	        }
	    }
	}
