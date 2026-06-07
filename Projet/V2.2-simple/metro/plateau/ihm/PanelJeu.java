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
	private JLabel lblMetros;
	private JLabel lblBases;
	private JLabel lblApercuTitre;

	private JRadioButton rbMetro;
	private JRadioButton rbDepart;
	private JComboBox<String> comboMetro;
	private JComboBox<String> comboDepart;
	private JButton btnSauvegarder;

	private Image[] metroImages = new Image[11]; // index 1 to 10

	private Color[] tabCouleurs = 
	{
		new Color(255, 105, 180) , // rose vif
		new Color(138, 43 , 226) , // violet
		new Color(255, 165, 0  ) , // orange
		new Color(0  , 255, 255) , // cyan
		new Color(176, 196, 222) , // bleu acier clair
		new Color(255, 20 , 147) , // rose profond
		new Color(186, 85 , 211) , // orchidée
		new Color(148, 0  , 211) , // violet foncé
		new Color(210, 105, 30 ) , // chocolat
		new Color(244, 164, 96 ) , // sable
		new Color(199, 21 , 133) , // violet rougeâtre
		new Color(123, 104, 238) , // bleu-violet doux
		new Color(255, 140, 0  ) , // orange foncé
		new Color(0  , 206, 209) , // turquoise
		new Color(72 , 61 , 139) , // bleu nuit
		new Color(220, 20 , 60 ) , // cramoisi
		new Color(169, 169, 169) , // gris foncé
		new Color(245, 222, 179) , // beige
		new Color(255, 228, 225) , // rose très pâle
		new Color(255, 192, 203)   // rose clair
	};

	public PanelJeu(FrameJeu frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		this.setPreferredSize(new Dimension(900, 580));

		this.setLayout(new BorderLayout(20, 20));

		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		/*-----------------------------*/
		/* Panneau Gauche (Paramètres) */
		/*-----------------------------*/

		this.panelGauche = new JPanel();

		this.panelGauche.setLayout(new BoxLayout(this.panelGauche, BoxLayout.Y_AXIS));

		this.panelGauche.setBorder(BorderFactory.createTitledBorder("Informations du Jeu"));
		this.panelGauche.setPreferredSize(new Dimension(280, 520));

		int nbJoueurs        = this.ctrl.getNbJoueurs();
		int nbMetro          = this.ctrl.getNbMetro();
		int nbBasesParJoueur = getNbBases(nbJoueurs);

		this.lblJoueurs = new JLabel("Nombre de joueurs : " + nbJoueurs);
		this.lblMetros  = new JLabel("Nombre de métros : "  + nbMetro  );
		
		this.lblBases   = new JLabel("Bases par joueur : "  + nbBasesParJoueur + " (Total : " + (nbBasesParJoueur * nbJoueurs) + ")");


		// Style
		

		Font fontLabel = new Font("Arial", Font.BOLD, 14);

		this.lblJoueurs.setFont(fontLabel);
		this.lblMetros .setFont(fontLabel);
		this.lblBases  .setFont(fontLabel);

		this.lblJoueurs.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblMetros .setAlignmentX(Component.LEFT_ALIGNMENT);
		this.lblBases  .setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panelGauche.add(this.lblJoueurs);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.panelGauche.add(this.lblMetros);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.panelGauche.add(this.lblBases);
		this.panelGauche.add(Box.createVerticalStrut(20));


		// Sélection et séparation du métro et de la base départ


		JLabel lblPlacement = new JLabel("Mode de placement :");

		lblPlacement.setFont(new Font("Arial", Font.BOLD, 14));
		lblPlacement.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panelGauche.add(lblPlacement);
		this.panelGauche.add(Box.createVerticalStrut(10));

		this.rbMetro  = new JRadioButton("Placer un Métro :", true);
		this.rbDepart = new JRadioButton("Placer un Départ :"     );

		ButtonGroup group = new ButtonGroup();
		group.add(this.rbMetro);
		group.add(this.rbDepart);

		this.rbMetro.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbMetro.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.rbDepart.setFont(new Font("Arial", Font.BOLD, 12));
		this.rbDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboMetro = new JComboBox<>();
		this.comboMetro.setMaximumSize(new Dimension(260, 30));
		this.comboMetro.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.comboDepart = new JComboBox<>();
		this.comboDepart.setMaximumSize(new Dimension(260, 30));
		this.comboDepart.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Synchronisation de l'activation des JComboBoxes
		this.rbMetro.addActionListener(e -> {
			this.comboMetro.setEnabled(true);
			this.comboDepart.setEnabled(false);
		});
		this.rbDepart.addActionListener(e -> {
			this.comboMetro.setEnabled(false);
			this.comboDepart.setEnabled(true);
		});

		// État initial

		this.comboMetro.setEnabled(true);
		this.comboDepart.setEnabled(false);

		mettreAJourComboPlacement();

		this.panelGauche.add(this.rbMetro);
		this.panelGauche.add(Box.createVerticalStrut(5));

		this.panelGauche.add(this.comboMetro);
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

		this.panelApercuGrille = new JPanel();
		this.panelApercuGrille.setBackground(Color.GRAY);
		this.panelApercuGrille.setLayout(new GridBagLayout()); // Centrer le message d'attente
		
		JLabel lblInfo = new JLabel("<html><center>Utilisez le menu <b>Fichier &gt; Importer une sauvegarde</b><br>pour voir et éditer le plateau.</center></html>");
		this.panelApercuGrille.add(lblInfo);

		this.panelDroite.add(this.panelApercuGrille, BorderLayout.CENTER);


		// Ajout au panel principal


		this.add(this.panelGauche, BorderLayout.WEST);
		this.add(this.panelDroite, BorderLayout.CENTER);
	}

	private void mettreAJourComboPlacement()
	{
		if (this.comboMetro == null || this.comboDepart == null) return;

		this.comboMetro.removeAllItems();
		this.comboMetro.addItem("Aucun");

		int nbMetro = this.ctrl.getNbMetro();

		for (int i = 1; i <= nbMetro; i++)
		{
			this.comboMetro.addItem("Métro " + i);
		}

		this.comboDepart.removeAllItems();
		this.comboDepart.addItem("Aucun");

		int nbJoueurs = this.ctrl.getNbJoueurs();

		for (int i = 1; i <= nbJoueurs; i++)
		{
			this.comboDepart.addItem("Joueur " + i);
		}
	}

	private int getNbBases(int nbJoueurs)
	{
		if (nbJoueurs == 2) return 4;
		if (nbJoueurs == 3) return 3;
		if (nbJoueurs == 4) return 2;
		return 1;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() != null && e.getActionCommand().equals("Importer une sauvegarde"))
		{
			importerSauvegarde();
		}
		else if (e.getSource() == this.btnSauvegarder)
		{
			sauvegarderPlateau();
		}
	}

	private void importerSauvegarde()
	{
		File dir = new File("sauvegarde");
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		JFileChooser fileChooser = new JFileChooser(dir);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			chargerApercu(selectedFile);
		}
	}

	private void chargerApercu(File file)
	{
		if (this.ctrl.chargerPlateau(file))
		{
			this.fichierCharge = file;

			// Scanner pour mettre à jour automatiquement les infos de métros et de joueurs si le fichier contient des valeurs supérieures

			int maxMetro   = this.ctrl.getNbMetro();
			int maxJoueurs = this.ctrl.getNbJoueurs();
			
			int size = this.ctrl.getLargeur() * this.ctrl.getHauteur();
			for (int i = 0; i < size; i++)
			{
				maxMetro   = Math.max(maxMetro  , this.ctrl.getMetro(i));
				maxJoueurs = Math.max(maxJoueurs, this.ctrl.getDepart(i));
			}

			this.ctrl.setConfigJeu(maxJoueurs, maxMetro);


			// Mettre à jour l'affichage gauche


			int nbJoueurs = this.ctrl.getNbJoueurs();
			int nbMetro   = this.ctrl.getNbMetro();

			int nbBasesParJoueur = getNbBases(nbJoueurs);

			this.lblJoueurs.setText("Nombre de joueurs : " + nbJoueurs);
			this.lblMetros .setText("Nombre de métros : "  + nbMetro);
			this.lblBases  .setText("Bases par joueur : "  + nbBasesParJoueur + " (Total : " + (nbBasesParJoueur * nbJoueurs) + ")");

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
			JOptionPane.showMessageDialog(this,
				"Erreur lors de la lecture ou de l'analyse du fichier.",
				"Erreur d'importation",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void sauvegarderPlateau()
	{
		if (this.fichierCharge != null)
		{

			// Validation : Exactement UNE et UNE SEULE base de départ par joueur obligatoire

			int nbJoueurs = this.ctrl.getNbJoueurs();
			int size      = this.ctrl.getLargeur() * this.ctrl.getHauteur();

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
					JOptionPane.showMessageDialog(this,"Erreur : Le joueur " + p + " doit avoir exactement UNE base de départ sur le plateau.\nActuellement : " + count + " placée(s).",	"Validation requise",
JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			if (this.ctrl.enregistrerPlateau(this.fichierCharge.getName()))
			{
				JOptionPane.showMessageDialog(this,
					"Plateau sauvegardé avec succès et écrasé dans :\n" + this.fichierCharge.getAbsolutePath(),
					"Sauvegarde Réussie",
					JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(this,
					"Erreur lors de l'enregistrement du plateau.",
					"Erreur de sauvegarde",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void cellClicked(int index)
	{
		if (this.rbMetro.isSelected())
		{
			Object selectedItem = this.comboMetro.getSelectedItem();

			if (selectedItem == null) return;

			String selected = selectedItem.toString();

			if (selected.equals("Aucun"))
			{
				this.ctrl.affecterMetro(index, 0);
			}
			else if (selected.startsWith("Métro "))
			{
				int metroNum = Integer.parseInt(selected.substring(6));
				this.ctrl.affecterMetro(index, metroNum);
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

		this.panelApercuGrille.repaint();
	}

	private Image getMetroImage(int metroNum)
	{
		if (metroNum < 1 || metroNum >= metroImages.length) return null;
		if (metroImages[metroNum] == null)
		{
			String path = getMetroImagePath(metroNum);
			if (path != null)
			{
				ImageIcon icon = new ImageIcon(path);
				metroImages[metroNum] = icon.getImage();
			}
		}
		return metroImages[metroNum];
	}

	private String getMetroImagePath(int metroNum)
	{
		String[] paths = {
			"plateau/images/"       + metroNum + ".png",
			"images/"               + metroNum + ".png",
			"../plateau/images/"    + metroNum + ".png",
			"../../plateau/images/" + metroNum + ".png"
		};
		for (String path : paths)
		{
			java.io.File file = new java.io.File(path);
			if (file.exists())
			{
				return file.getAbsolutePath();
			}
		}
		return "plateau/images/" + metroNum + ".png";
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

			// 2. Dessiner l'image du Métro
			int metro = ctrl.getMetro(index);
			if (metro > 0)
			{
				Image img = getMetroImage(metro);
				if (img != null)
				{
					g.drawImage(img, 2, 2, w - 4, h - 4, this);
				}
			}

			// 3. Dessiner le départ du joueur
			int depart = ctrl.getDepart(index);
			if (depart > 0)
			{
				int badgeSize = Math.min(w, h) / 3;
				if (badgeSize < 16) badgeSize = 16;
				int bx = w - badgeSize - 2;
				int by = 2;

				g.setColor(Color.DARK_GRAY);
				g.fillOval(bx, by, badgeSize, badgeSize);
				g.setColor(Color.WHITE);
				g.drawOval(bx, by, badgeSize, badgeSize);

				g.setFont(new Font("Arial", Font.BOLD, (int)(badgeSize * 0.7)));

				FontMetrics fm = g.getFontMetrics();

				String txt = "D" + depart;

				int tx = bx + (badgeSize - fm.stringWidth(txt)) / 2;
				int ty = by + ((badgeSize - fm.getHeight()) / 2) + fm.getAscent();
				
				g.drawString(txt, tx, ty);
			}
		}
	}
}
