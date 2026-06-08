package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

// Ecran de placement : on pose les stations et les departs, et on voit les aretes se former.
// L'IHM affiche et transmet les clics ; les regles (aretes, "1 depart par joueur") sont dans le metier.
public class PanelJeu extends JPanel implements ActionListener, MouseListener
{
	private FrameJeu   frame;
	private Controleur ctrl;

	private JLabel lblInfo;
	private JLabel lblTitre;

	private JComboBox<String> comboFichiers;
	private JComboBox<String> comboStation;
	private JComboBox<String> comboDepart;
	private JRadioButton      rbStation;
	private JRadioButton      rbDepart;
	private JButton btnCharger;
	private JButton btnNouveau;
	private JButton btnSauvegarder;

	private GrillePanel grille;
	private CasePanel[] casePanels;

	private Image[] imagesStations = new Image[7];   // index 1..6

	public PanelJeu(FrameJeu frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		this.setPreferredSize(new Dimension(900, 640));
		this.setLayout(new BorderLayout(20, 20));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		this.add(construirePanneauGauche(), BorderLayout.WEST);

		this.lblTitre = new JLabel("Plateau courant", SwingConstants.CENTER);
		this.grille   = new GrillePanel();
		JPanel droite = new JPanel(new BorderLayout(10, 10));
		droite.setBorder(BorderFactory.createTitledBorder("Plateau interactif"));
		droite.add(this.lblTitre, BorderLayout.NORTH);
		droite.add(this.grille,   BorderLayout.CENTER);
		this.add(droite, BorderLayout.CENTER);

		majInfos();
		reconstruireGrille();
	}

	private JPanel construirePanneauGauche()
	{
		JPanel gauche = new JPanel();
		gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
		gauche.setBorder(BorderFactory.createTitledBorder("Informations"));
		gauche.setPreferredSize(new Dimension(280, 600));

		this.lblInfo = new JLabel();

		this.comboFichiers  = new JComboBox<>();
		this.btnCharger     = new JButton("Charger le plateau");
		this.btnNouveau     = new JButton("Creer un nouveau plateau");
		this.rbStation      = new JRadioButton("Placer une station", true);
		this.rbDepart       = new JRadioButton("Placer un depart");
		this.comboStation   = new JComboBox<>();
		this.comboDepart    = new JComboBox<>();
		this.btnSauvegarder = new JButton("Sauvegarder");

		ButtonGroup groupe = new ButtonGroup();
		groupe.add(this.rbStation);
		groupe.add(this.rbDepart);

		this.btnCharger.addActionListener(this);
		this.btnNouveau.addActionListener(this);
		this.btnSauvegarder.addActionListener(this);
		this.rbStation.addActionListener(this);
		this.rbDepart.addActionListener(this);

		gauche.add(this.lblInfo);
		gauche.add(Box.createVerticalStrut(15));
		gauche.add(new JLabel("Sauvegardes :"));
		gauche.add(this.comboFichiers);
		gauche.add(this.btnCharger);
		gauche.add(Box.createVerticalStrut(10));
		gauche.add(this.btnNouveau);
		gauche.add(Box.createVerticalStrut(20));
		gauche.add(new JLabel("Mode de placement :"));
		gauche.add(this.rbStation);
		gauche.add(this.comboStation);
		gauche.add(this.rbDepart);
		gauche.add(this.comboDepart);
		gauche.add(Box.createVerticalStrut(20));
		gauche.add(this.btnSauvegarder);

		return gauche;
	}

	// remplit les infos + les listes deroulantes a partir du metier
	private void majInfos()
	{
		this.lblInfo.setText("Joueurs : " + this.ctrl.getNbJoueurs() + "   Stations : " + this.ctrl.getNbStations());

		this.comboFichiers.removeAllItems();
		for (String nom : this.ctrl.listerSauvegardes()) this.comboFichiers.addItem(nom);

		this.comboStation.removeAllItems();
		this.comboStation.addItem("Aucune");
		for (int i = 1; i <= this.ctrl.getNbStations(); i++)
			this.comboStation.addItem(i <= Couleurs.NOMS_STATIONS.length ? Couleurs.NOMS_STATIONS[i - 1] : "Station " + i);

		this.comboDepart.removeAllItems();
		this.comboDepart.addItem("Aucun");
		for (int i = 1; i <= this.ctrl.getNbJoueurs(); i++) this.comboDepart.addItem("Joueur " + i);

		this.comboStation.setEnabled(this.rbStation.isSelected());
		this.comboDepart.setEnabled(this.rbDepart.isSelected());
	}

	// (re)construit la grille a partir des dimensions du plateau courant
	private void reconstruireGrille()
	{
		this.grille.removeAll();
		this.grille.setLayout(new GridLayout(this.ctrl.getHauteur(), this.ctrl.getLargeur(), 2, 2));
		this.grille.setBackground(Color.DARK_GRAY);

		this.casePanels = new CasePanel[this.ctrl.getNbCases()];
		for (int i = 0; i < this.casePanels.length; i++)
		{
			CasePanel c = new CasePanel(i);
			c.addMouseListener(this);
			this.casePanels[i] = c;
			this.grille.add(c);
		}
		this.grille.revalidate();
		this.grille.repaint();
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.rbStation || e.getSource() == this.rbDepart)
		{
			this.comboStation.setEnabled(this.rbStation.isSelected());
			this.comboDepart.setEnabled(this.rbDepart.isSelected());
		}
		else if (e.getSource() == this.btnCharger)   charger();
		else if (e.getSource() == this.btnNouveau)   { new FrameConfiguration(this.ctrl); this.frame.dispose(); }
		else if (e.getSource() == this.btnSauvegarder) sauvegarder();
	}

	private void charger()
	{
		Object choix = this.comboFichiers.getSelectedItem();
		if (choix == null) { JOptionPane.showMessageDialog(this, "Aucune sauvegarde a charger.", "Info", JOptionPane.INFORMATION_MESSAGE); return; }

		if (this.ctrl.charger(choix.toString()))
		{
			this.lblTitre.setText("Plateau : " + choix.toString());
			majInfos();
			reconstruireGrille();
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Impossible de charger ce fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void sauvegarder()
	{
		// c'est le METIER qui verifie "exactement 1 depart par joueur"
		int joueurFautif = this.ctrl.premierJoueurSansDepartUnique();
		if (joueurFautif != 0)
		{
			JOptionPane.showMessageDialog(this,
				"Le joueur " + joueurFautif + " doit avoir exactement un depart sur le plateau.",
				"Validation", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String nom = JOptionPane.showInputDialog(this, "Nom du fichier :", "mon_plateau");
		if (nom == null || nom.trim().isEmpty()) return;
		if (this.ctrl.enregistrer(nom.trim())) majInfos();   // rafraichit la liste des sauvegardes
	}

	private void caseCliquee(int index)
	{
		if (this.rbStation.isSelected())
			this.ctrl.affecterStation(index, this.comboStation.getSelectedIndex());   // 0 = Aucune
		else if (this.rbDepart.isSelected())
			this.ctrl.placerDepart(index, this.comboDepart.getSelectedIndex());       // 0 = Aucun

		this.grille.repaint();   // les aretes ont pu changer (le metier les a recalculees)
	}

	// clic sur une case (PanelJeu est le MouseListener de toutes les cases)
	public void mousePressed(MouseEvent e)
	{
		for (int i = 0; i < this.casePanels.length; i++)
			if (e.getSource() == this.casePanels[i]) { caseCliquee(i); return; }
	}
	public void mouseClicked (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}

	private Image imageStation(int station)
	{
		if (station < 1 || station >= this.imagesStations.length) return null;
		if (this.imagesStations[station] == null)
			this.imagesStations[station] = new ImageIcon(Images.chemin(station + ".png")).getImage();
		return this.imagesStations[station];
	}

	// ── La grille : dessine les cases puis les aretes par-dessus ──
	private class GrillePanel extends JPanel
	{
		public void paint(Graphics g)
		{
			super.paint(g);   // dessine d'abord les cases (les enfants)

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(3));
			g2.setColor(new Color(70, 70, 70));

			int n = ctrl.getNbCases();
			if (getComponentCount() != n) return;
			for (int i = 0; i < n; i++)
				for (int j = i + 1; j < n; j++)
					if (ctrl.aArete(i, j))
					{
						Component a = getComponent(i), b = getComponent(j);
						int xa = a.getX() + a.getWidth() / 2, ya = a.getY() + a.getHeight() / 2;
						int xb = b.getX() + b.getWidth() / 2, yb = b.getY() + b.getHeight() / 2;
						g2.drawLine(xa, ya, xb, yb);
					}
		}
	}

	// ── Une case : couleur d'arrondissement + image de station + badge de depart ──
	private class CasePanel extends JPanel
	{
		private int index;

		public CasePanel(int index)
		{
			this.index = index;
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			this.setPreferredSize(new Dimension(50, 50));
		}

		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int w = getWidth(), h = getHeight();

			int arr = ctrl.getArrondissement(this.index);
			if (arr >= 1 && arr <= Couleurs.PALETTE.length) g.setColor(Couleurs.PALETTE[arr - 1]);
			else                                            g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, w, h);

			int station = ctrl.getStation(this.index);
			if (station > 0)
			{
				Image img = imageStation(station);
				if (img != null) g.drawImage(img, 4, 4, w - 8, h - 8, this);
				else { g.setColor(Color.BLACK); g.drawString("S" + station, 5, h / 2); }
			}

			int depart = ctrl.getDepart(this.index);
			if (depart > 0)
			{
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 14));
				g.drawString("D" + depart, 5, 16);
			}
		}
	}
}
