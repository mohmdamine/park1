package park1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;

public class Fenetre extends JFrame {

    JButton b;
    JButton b2;
    JButton b3;
    JButton b4;
    public int mat;

    public Fenetre() {
        setTitle("Gestion de parking");
        b = new JButton("PARK");
        b2 = new JButton("UNPARK");
        b3 = new JButton("voir parking");
        b4 = new JButton("creer parking");
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(140, 140, 140, 140));
        p.setLayout(new GridLayout(4, 1, 40, 40));
        p.add(b);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        add(p);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                Dijkstracheck dij1 = new Dijkstracheck();
                
                Node i = dij1.trouverPlaceLaPlusProche(new Node(28, 12), dij1.chargerPlacesDepuisFichier("/Users/Asus/eclipse-workspace/park1/src/park1/node.txt"));
                if (i==null) {
                	JOptionPane.showMessageDialog(null, "parking plein","attention",JOptionPane.ERROR_MESSAGE);
                }else 
                park();
            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code pour l'UNPARK si nécessaire
                Unpark();
            }
        });
        b3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new DijkstraParking();
			}
        	
        });
        b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new CreeParking();
			}
        	
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
    }

    private void park() {
        JFrame par = new JFrame("Park");
        par.setTitle("Park");
        JButton b = new JButton("Valider");
        JTextField matricule = new JTextField(20);
        JLabel etq = new JLabel("Enter matricule :");
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));
        p.add(etq);
        p.add(matricule);
        p.add(b);
        par.add(p);

        par.setVisible(true);
        par.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        par.setSize(400, 300);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
                String mat = matricule.getText();  // Récupérer le matricule saisi par l'utilisateur
                System.out.println("Matricule saisi : " + mat);
                code(mat);  // Passer le matricule à la méthode code
            }
        });
    }

    private void code(String mat) {
        JFrame cod = new JFrame();
        JLabel j1, j2;
        Random r;
        JButton b1, b2;
        int randomNumber;
        String heureFormatee;
        cod.setTitle("Récupération code");

        r = new Random();
        randomNumber = r.nextInt(999999999);
        j1 = new JLabel("Voici votre code : " + randomNumber);

        LocalTime heure = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        heureFormatee = heure.format(formatter);
        j2 = new JLabel("Heure actuelle : " + heureFormatee);

        b1 = new JButton("Voir l'illustration ");
        
        JPanel p = new JPanel();
        p.add(j1);
        p.add(j2);
        cod.add(p);

        JPanel p1 = new JPanel();
        p1.add(b1);
        
        cod.add(p1, BorderLayout.SOUTH);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code pour la gestion du DijkstraParking
                Dijkstrapark dij = new Dijkstrapark();
                Time time = Time.valueOf(heureFormatee);
                String url = "jdbc:mysql://localhost:3306/base";
                String user = "root";
                String password = "";

                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    String sql = "INSERT INTO place(matricule, codesortir, heurearrivee) VALUES(?, ?, ?)";

                    PreparedStatement stmt = conn.prepareStatement(sql);
                   
                    stmt.setString(1,mat);
                    stmt.setInt(2, randomNumber);
                    stmt.setTime(3, time);
                    int rowsInserted = stmt.executeUpdate();
                } catch (SQLException l) {
                    l.printStackTrace();
                }
            }
        });

        cod.setVisible(true);
        cod.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cod.setSize(300, 200);
    }
    
    public void Unpark() {
   	 	JFrame unp = new JFrame();
    	setTitle("inserer code");
    	JLabel text = new JLabel("iserer le code");
    	JTextField code = new JTextField(15);
    	JButton b = new JButton("ENTRER");
    	JPanel a = new JPanel();
    	a.setLayout(new FlowLayout(FlowLayout.CENTER));
    	a.add(text);
    	JPanel c = new JPanel();
    	c.setLayout(new FlowLayout(FlowLayout.CENTER));
    	c.add(b);
    	JPanel p = new JPanel();
    	p.setBorder(BorderFactory.createEmptyBorder(80,40,80,40));
    	p.setLayout(new GridLayout(3,1,5,5));
    	p.add(a);
    	p.add(code);
    	p.add(c);
    	unp.add(p);
    	unp.setVisible(true);
    	unp.setDefaultCloseOperation(3);
    	unp.setSize(350,300);
    	unp.setResizable(false);
    	 
    	b.addActionListener(new ActionListener() {

    		@Override
    		public void actionPerformed(ActionEvent e) {
    			// TODO Auto-generated method stub
    			if(code.getText().length()!=0) {
    				
    				String pswd = code.getText();
    				int pwd=Integer.parseInt(pswd);
    				System.out.println("code de sortir saisi : " + pswd);
    				   String url = "jdbc:mysql://localhost:3306/base";
    		            String user = "root";
    		            String password = "";

    		            try {
    		                // Connexion à la base de données
    		                Connection conn = DriverManager.getConnection(url, user, password);

    		                // Requête SQL pour insérer un nouvel enregistrement
    		                String sql ="SELECT codesortir FROM place WHERE codesortir ="+pwd+""; 
    		                Statement stmt=conn.createStatement();
    		                ResultSet rs =  stmt.executeQuery(sql); 
             if(rs.next()) {
            	   paeiment(pwd); }
             else {
            	   JOptionPane.showMessageDialog(null, "entrer le code exact","attention",JOptionPane.ERROR_MESSAGE);
             }
		
    			
    		} catch (SQLException k) {
                k.printStackTrace();
            }
    		            
    			}else
    				JOptionPane.showMessageDialog(null, "entrer le code","attention",JOptionPane.ERROR_MESSAGE);
    		}
    	});
    	
    }

    public void paeiment(int pwd) {
         int tab[]= {0,5,10,15,20,25,30,35,40,45,50,55,60,65,70};
    	JFrame paie=new JFrame();
    	JLabel text;
    	JButton b;
    	float pr = 0;
        paie.setTitle("paiement");
        
        LinkedList l = new LinkedList();
    	
    		LocalTime heure = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String heureSortir = heure.format(formatter);
            Time hs = Time.valueOf(heureSortir);
           	        
            String url = "jdbc:mysql://localhost:3306/base";
            String user = "root";
            String password = "";

            try {
                // Connexion à la base de données
                Connection conn = DriverManager.getConnection(url, user, password);

                // Requête SQL pour insérer un nouvel enregistrement
                String sql ="SELECT heurearrivee FROM place WHERE codesortir="+pwd+""; 
                Statement stmt=conn.createStatement();
                ResultSet rs =  stmt.executeQuery(sql); 
                Time h = null;
                while (rs.next()) {
                	
                    h = rs.getTime("heurearrivee");     
                }
                LocalTime time1=LocalTime.of(hs.getHours(),hs.getMinutes());
                LocalTime time2=LocalTime.of(h.getHours(), h.getMinutes());
                Duration duree=Duration.between(time1, time2);
                long temp = duree.toHours();
               for(int i=0;i<tab.length;i++) {
               if(temp==i)
               {
            	   pr=tab[i];
               }
               }
               
                stmt.close();
                conn.close();
            } catch (SQLException k) {
                k.printStackTrace();
            }
            text = new JLabel("montant a payer:"+pr+"DH"); 
    		
    		b = new JButton("VALIDER");
    		JPanel a = new JPanel();
    		a.setLayout(new FlowLayout(FlowLayout.CENTER));
    		a.add(text);
    		JPanel p = new JPanel();
    		p.setBorder(BorderFactory.createEmptyBorder(100,40,100,40));
    		p.setLayout(new GridLayout(2,1,5,5));
    		p.add(a);
    		p.add(b);
    		paie.add(p);
    		paie.setVisible(true);
    		paie.setDefaultCloseOperation(3);
    		paie.setSize(350,300);
    		paie.setResizable(true);
    		b.addActionListener(new ActionListener() {

    			@Override
    			public void actionPerformed(ActionEvent e) {
    				
                    // Code pour la gestion du DijkstraParking
                    Dijkstraunpark dij1 = new Dijkstraunpark();
                   
                        // Connexion à la base de données
                        
                        String url = "jdbc:mysql://localhost:3306/base";
                        String user = "root";
                        String password = "";

                        try {
                            Connection conn = DriverManager.getConnection(url, user, password);
                            String sql = "INSERT INTO code(cs) VALUES(?)";

                            PreparedStatement stmt = conn.prepareStatement(sql);
                           
                            
                            stmt.setInt(1, pwd);
                           
                            int rowsInserted = stmt.executeUpdate();
                        } catch (SQLException l) {
                            l.printStackTrace();
                        }
    			}
    			});
            
    		}
    }
