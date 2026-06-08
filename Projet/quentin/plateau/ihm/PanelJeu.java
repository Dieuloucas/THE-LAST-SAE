package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class PanelJeu extends JPanel implements ActionListener
{
	private FrameJeu frame;
	private Controleur ctrl;
	private File fichierCharge;

	private JPanel panelGauche;
	private JPanel panelDroite;
	private JPanel panelApercuGrille;

	private JLabel lblJoueurs;
	private JLabel lblStations;
	private JLabel lblApercuTitre;

	private JRadioButton rbStation;
	private JRadioButton rbDepart;
	private JComboBox<String> comboStation;
	private JComboBox<String> comboDepart;
	private JButton btnSauvegarder;

	private JComboBox<String> comboFichiersImport;
	private JButton btnImporter;
	private JButton btnCreerNouveau;

	private Image[] stationImages = new Image[11]; // index 1 to 10

	private String[] nomsStations = {
		"Tour Eiffel",
		"Moulin Rouge",
		"Louvre",
		"Restaurant",
		"Gare",
		"Aéroport"
	};

	private Color[] tabCouleurs = 
	{
		new Color(255, 105, 180), // rose vif
		new Color(138, 43, 226),  // violet
		new Color(255, 165, 0),   // orange
		new Color(0, 255, 255),   // cyan
		new Color(176, 196, 222), // bleu acier clair
		new Color(255, 20, 147),  // rose profond
		new Color(186, 85, 211),  // orchidée
		new Color(148, 0, 211),   // violet foncé
		new Color(210, 105, 30),  // chocolat
		new Color(244, 164, 96),  // sable
		new Color(199, 21, 133),  // violet rougeâtre
		new Color(123, 104, 238), // bleu-violet doux
		new Color(255, 140, 0),   // orange foncé
		new Color(0, 206, 209),   // turquoise
		new Color(72, 61, 139),   // bleu nuit
		new Color(220, 20, 60),   // cramoisi
		new Color(169, 169, 169), // gris foncé
		new Color(245, 222, 179), // beige
		new Color(255, 228, 225), // rose très pâle
		new Color(255, 192, 203)  // rose clair
	};

	public PanelJeu(FrameJeu frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl = ctrl;

		this.setPreferredSize(new Dimension(900, 640));
		this.setLayout(new BorderLayout(20, 20));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		/*-----------------------------*/
		/* Panneau Gauche (Paramètres) */
		/*-----------------------------*/
		this.panelGauche = new JPanel();
		this.panelGauche.setLayout(new BoxLayout(this.panelGauche, BoxLayout.Y_AXIS));
		this.panelGauche.setBorder(BorderFactory.createTitledBorder("Informations du Jeu"));
		this.panelGauche.setPreferredSize(new Dimension(280, 580));

		int nbJoueurs = this.ctrl.getNbJoueurs();
		int nbStations = this.ctrl.getNbStations();

		this.lblJoueurs = new JLabel("Nombre de joueurs : " + nbJoueurs);
		this.lblStations  = new JLabel("Nombre de stations : " + nbStations);

		// Style
		Font fontLabel = new Font("Arial", Font.BOLD, 14);
		this.lblJoueurs.setFont(fontLabel);
		this.lblStations.setFont(fontLabel);

		this.lblJoueurs.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblStations.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panelGauche.add(this.lblJoueurs);
		this.panelGauche.add(Box.createVerticalStrut(10));
		this.panelGauche.add(this.lblStations);
		this.panelGauche.add(Box.createVerticalStrut(20));

		// Import / Création de fichier
		JLabel lblFichier = new JLabel("Fichier de sauvegarde :");
		lblFichier.setFont(new Font("Arial", Font.BOLD, 12));
		lblFichier.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(lblFichier);
		this.panelGauche.add(Box.createVerticalStrut(5));

		this.comboFichiersImport = new JComboBox<>();
		this.comboFichiersImport.setMaximumSize(new Dimension(260, 30));
		this.comboFichiersImport.setAlignmentX(Component.LEFT_ALIGNMENT);
		mettreAJourComboFichiers();
		this.panelGauche.add(this.comboFichiersImport);
		this.panelGauche.add(Box.createVerticalStrut(5));

		this.btnImporter = new JButton("Charger le plateau");
		this.btnImporter.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnImporter.addActionListener(this);
		this.panelGauche.add(this.btnImporter);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.btnCreerNouveau = new JButton("Créer un nouveau plateau");
		this.btnCreerNouveau.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnCreerNouveau.addActionListener(this);
		this.panelGauche.add(this.btnCreerNouveau);
		this.panelGauche.add(Box.createVerticalStrut(20));

		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(260, 10));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(sep);
		this.panelGauche.add(Box.createVerticalStrut(15));

		// Sélection et séparation du métro et de la base départ
		JLabel lblPlacement = new JLabel("Mode de placement :");
		lblPlacement.setFont(new Font("Arial", Font.BOLD, 14));
		lblPlacement.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.panelGauche.add(lblPlacement);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.rbStation = new JRadioButton("Placer une Station :", true);
		this.rbDepart = new JRadioButton("Placer un Départ :");
		ButtonGroup group = new ButtonGroup();
		group.add(this.rbStation);
		group.add(this.rbDepart);

		this.rbStation.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbStation.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.rbDepart.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboStation = new JComboBox<>();
		this.comboStation.setMaximumSize(new Dimension(260, 30));
		this.comboStation.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboDepart = new JComboBox<>();
		this.comboDepart.setMaximumSize(new Dimension(260, 30));
		this.comboDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Synchronisation de l'activation des JComboBoxes
		this.rbStation.addActionListener(e -> {
			this.comboStation.setEnabled(true);
			this.comboDepart.setEnabled(false);
		});
		this.rbDepart.addActionListener(e -> {
			this.comboStation.setEnabled(false);
			this.comboDepart.setEnabled(true);
		});

		// État initial
		this.comboStation.setEnabled(true);
		this.comboDepart.setEnabled(false);

		mettreAJourComboPlacement();

		this.panelGauche.add(this.rbStation);
		this.panelGauche.add(Box.createVerticalStrut(5));
		this.panelGauche.add(this.comboStation);
		this.panelGauche.add(Box.createVerticalStrut(15));
		this.panelGauche.add(this.rbDepart);
		this.panelGauche.add(Box.createVerticalStrut(5));
		this.panelGauche.add(this.comboDepart);
		this.panelGauche.add(Box.createVerticalStrut(30));

		// Bouton de sauvegarde
		this.btnSauvegarder = new JButton("Sauvegarder les modifications");
		this.btnSauvegarder.setFont(new Font("Arial", Font.BOLD, 12));
		this.btnSauvegarder.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnSauvegarder.setEnabled(false);
		this.btnSauvegarder.addActionListener(this);
		this.panelGauche.add(this.btnSauvegarder);

		/*---------------------------*/
		/* Panneau Droit (Aperçu)    */
		/*---------------------------*/
		this.panelDroite = new JPanel(new BorderLayout(10, 10));
		this.panelDroite.setBorder(BorderFactory.createTitledBorder("Plateau Interactif"));

		this.lblApercuTitre = new JLabel("Aucun plateau chargé", SwingConstants.CENTER);
		this.lblApercuTitre.setFont(new Font("Arial", Font.ITALIC, 14));
		this.panelDroite.add(this.lblApercuTitre, BorderLayout.NORTH);

		this.panelApercuGrille = new JPanel()
		{
			@Override
			public void paint(Graphics g)
			{
				super.paint(g); // Dessine le fond et les cases
				
				// Dessiner les arêtes par-dessus les cases
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(new BasicStroke(3));
				g2d.setColor(new Color(160, 160, 160, 200)); // Gris semi-transparent

				int size = ctrl.getLargeur() * ctrl.getHauteur();
				if (getComponentCount() == size)
				{
					for (int i = 0; i < size; i++)
					{
						for (int j = i + 1; j < size; j++)
						{
							if (ctrl.aArete(i, j))
							{
								Component compI = getComponent(i);
								Component compJ = getComponent(j);

								int x1 = compI.getX() + compI.getWidth() / 2;
								int y1 = compI.getY() + compI.getHeight() / 2;
								int x2 = compJ.getX() + compJ.getWidth() / 2;
								int y2 = compJ.getY() + compJ.getHeight() / 2;

								g2d.drawLine(x1, y1, x2, y2);
							}
						}
					}
				}
			}
		};
		this.panelApercuGrille.setBackground(Color.GRAY);
		this.panelApercuGrille.setLayout(new GridBagLayout()); // Centrer le message d'attente
		
		JLabel lblInfo = new JLabel("<html><center>Sélectionnez une sauvegarde à gauche et cliquez sur <b>Charger le plateau</b></center></html>");
		this.panelApercuGrille.add(lblInfo);

		this.panelDroite.add(this.panelApercuGrille, BorderLayout.CENTER);

		// Ajout au panel principal
		this.add(this.panelGauche, BorderLayout.WEST);
		this.add(this.panelDroite, BorderLayout.CENTER);
	}

	private void mettreAJourComboFichiers()
	{
		this.comboFichiersImport.removeAllItems();
		File dir = new File("sauvegarde");
		if (dir.exists() && dir.isDirectory())
		{
			File[] list = dir.listFiles();
			if (list != null)
			{
				for (File f : list)
				{
					if (f.isFile() && f.getName().endsWith(".txt"))
					{
						this.comboFichiersImport.addItem(f.getName());
					}
				}
			}
		}
	}

	private void mettreAJourComboPlacement()
	{
		if (this.comboStation == null || this.comboDepart == null) return;

		this.comboStation.removeAllItems();
		this.comboStation.addItem("Aucun");
		int nbStations = this.ctrl.getNbStations();
		for (int i = 1; i <= nbStations; i++)
		{
			if (i <= nomsStations.length)
			{
				this.comboStation.addItem(nomsStations[i - 1]);
			}
			else
			{
				this.comboStation.addItem("Station " + i);
			}
		}

		this.comboDepart.removeAllItems();
		this.comboDepart.addItem("Aucun");
		int nbJoueurs = this.ctrl.getNbJoueurs();
		for (int i = 1; i <= nbJoueurs; i++)
		{
			this.comboDepart.addItem("Joueur " + i);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnImporter)
		{
			importerSauvegarde();
		}
		if (e.getSource() == this.btnCreerNouveau)
		{
			creerNouveauPlateau();
		}
		if (e.getSource() == this.btnSauvegarder)
		{
			sauvegarderPlateau();
		}
	}

	private void creerNouveauPlateau()
	{
		new FrameConfiguration(this.ctrl);
		this.frame.dispose();
	}

	private void importerSauvegarde()
	{
		Object selectedItem = this.comboFichiersImport.getSelectedItem();
		if (selectedItem == null)
		{
			System.out.println("Aucune sauvegarde sélectionnée.");
			return;
		}

		String nomFichier = selectedItem.toString();
		File file = new File("sauvegarde", nomFichier);
		if (!file.exists())
		{
			System.out.println("Le fichier sauvegarde/" + file.getName() + " n'existe pas.");
			return;
		}

		chargerApercu(file);
	}

	private void chargerApercu(File file)
	{
		if (this.ctrl.chargerPlateau(file))
		{
			this.fichierCharge = file;

			// Scanner pour mettre à jour automatiquement les infos de stations et de joueurs si le fichier contient des valeurs supérieures
			int maxStations = this.ctrl.getNbStations();
			int maxJoueurs = this.ctrl.getNbJoueurs();
			int size = this.ctrl.getLargeur() * this.ctrl.getHauteur();
			for (int i = 0; i < size; i++)
			{
				maxStations = Math.max(maxStations, this.ctrl.getStation(i));
				maxJoueurs = Math.max(maxJoueurs, this.ctrl.getDepart(i));
			}
			this.ctrl.setConfigJeu(maxJoueurs, maxStations);

			// Mettre à jour l'affichage gauche
			int nbJoueurs = this.ctrl.getNbJoueurs();
			int nbStations = this.ctrl.getNbStations();

			this.lblJoueurs.setText("Nombre de joueurs : " + nbJoueurs);
			this.lblStations.setText("Nombre de stations : " + nbStations);

			mettreAJourComboPlacement();
			this.btnSauvegarder.setEnabled(true);

			// Vider et reconstruire la grille
			this.panelApercuGrille.removeAll();
			int largeur = this.ctrl.getLargeur();
			int hauteur = this.ctrl.getHauteur();
			this.panelApercuGrille.setLayout(new GridLayout(hauteur, largeur, 2, 2));
			this.panelApercuGrille.setBackground(Color.DARK_GRAY);

			for (int i = 0; i < size; i++)
			{
				CasePanel cell = new CasePanel(i);
				this.panelApercuGrille.add(cell);
			}

			this.lblApercuTitre.setText("Plateau : " + file.getName());
			this.lblApercuTitre.setFont(new Font("Arial", Font.BOLD, 14));

			this.panelApercuGrille.revalidate();
			this.panelApercuGrille.repaint();
			this.frame.pack();
			this.frame.setLocationRelativeTo(null);
		}
		else
		{
			System.out.println("Erreur lors de la lecture ou de l'analyse du fichier.");
		}
	}

	private void sauvegarderPlateau()
	{
		if (this.fichierCharge != null)
		{
			// Validation : Exactement UNE et UNE SEULE base de départ par joueur obligatoire
			int nbJoueurs = this.ctrl.getNbJoueurs();
			int size = this.ctrl.getLargeur() * this.ctrl.getHauteur();
			for (int p = 1; p <= nbJoueurs; p++)
			{
				int count = 0;
				for (int i = 0; i < size; i++)
				{
					if (this.ctrl.getDepart(i) == p)
					{
						count++;
					}
				}
				if (count != 1)
				{
					System.out.println("Erreur : Le joueur " + p + " doit avoir exactement UNE base de départ sur le plateau. Actuellement : " + count + " placée(s).");
					return;
				}
			}

			if (this.ctrl.enregistrerPlateau(this.fichierCharge.getName()))
			{
				System.out.println("Plateau sauvegardé avec succès et écrasé dans : " + this.fichierCharge.getAbsolutePath());
				this.frame.dispose();
			}
			else
			{
				System.out.println("Erreur lors de l'enregistrement du plateau.");
			}
		}
	}

	private void cellClicked(int index)
	{
		if (this.rbStation.isSelected())
		{
			Object selectedItem = this.comboStation.getSelectedItem();
			if (selectedItem == null) return;
			String selected = selectedItem.toString();
			if (selected.equals("Aucun"))
			{
				this.ctrl.affecterStation(index, 0);
			}
			else
			{
				int stationNum = 0;
				for (int i = 0; i < nomsStations.length; i++)
				{
					if (selected.equals(nomsStations[i]))
					{
						stationNum = i + 1;
						break;
					}
				}

				if (stationNum == 0 && selected.startsWith("Station "))
				{
					try
					{
						stationNum = Integer.parseInt(selected.substring(8));
					}
					catch (Exception ex) {}
				}

				this.ctrl.affecterStation(index, stationNum);
			}
		}
		else if (this.rbDepart.isSelected())
		{
			Object selectedItem = this.comboDepart.getSelectedItem();
			if (selectedItem == null) return;
			String selected = selectedItem.toString();
			if (selected.equals("Aucun"))
			{
				this.ctrl.affecterDepart(index, 0);
			}
			else if (selected.startsWith("Joueur "))
			{
				int playerNum = Integer.parseInt(selected.substring(7));

				// Enforce ONE and ONLY ONE starting base per player:
				// Clear any other cell currently set to this player start
				int size = this.ctrl.getLargeur() * this.ctrl.getHauteur();
				for (int i = 0; i < size; i++)
				{
					if (this.ctrl.getDepart(i) == playerNum)
					{
						this.ctrl.affecterDepart(i, 0);
					}
				}

				this.ctrl.affecterDepart(index, playerNum);
			}
		}

		// Recalculer les arêtes après modification
		this.ctrl.genererAretesAuto();
		this.panelApercuGrille.repaint();
	}

	private Image getStationImage(int stationNum)
	{
		if (stationNum < 1 || stationNum >= stationImages.length) return null;
		if (stationImages[stationNum] == null)
		{
			String path = getStationImagePath(stationNum);
			if (path != null)
			{
				ImageIcon icon = new ImageIcon(path);
				stationImages[stationNum] = icon.getImage();
			}
		}
		return stationImages[stationNum];
	}

	private String getStationImagePath(int stationNum)
	{
		String[] paths = {
			"plateau/images/" + stationNum + ".png",
			"images/" + stationNum + ".png",
			"../plateau/images/" + stationNum + ".png",
			"../../plateau/images/" + stationNum + ".png"
		};
		for (String path : paths)
		{
			java.io.File file = new java.io.File(path);
			if (file.exists())
			{
				return file.getAbsolutePath();
			}
		}
		return "plateau/images/" + stationNum + ".png";
	}

	/*------------------------*/
	/* Panel Interne des cases*/
	/*------------------------*/
	private class CasePanel extends JPanel
	{
		private int index;

		public CasePanel(int index)
		{
			this.index = index;
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			this.setPreferredSize(new Dimension(50, 50));
			this.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					cellClicked(CasePanel.this.index);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int w = getWidth();
			int h = getHeight();

			// 1. Dessiner le fond (arrondissement)
			int arr = ctrl.getArrondissement(index);
			if (arr == 0)
			{
				g.setColor(Color.LIGHT_GRAY);
			}
			else if (arr > 0 && arr <= tabCouleurs.length)
			{
				g.setColor(tabCouleurs[arr - 1]);
			}
			else
			{
				g.setColor(Color.WHITE);
			}
			g.fillRect(0, 0, w, h);

			// 2. Dessiner l'image de la Station
			int station = ctrl.getStation(index);
			if (station > 0)
			{
				Image img = getStationImage(station);
				if (img != null)
				{
					g.drawImage(img, 2, 2, w - 4, h - 4, this);
				}
			}

			// 3. Dessiner le départ du joueur
			int depart = ctrl.getDepart(index);
			if (depart > 0)
			{
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 14));
				g.drawString("D" + depart, 5, 18);
			}
		}
	}
}
