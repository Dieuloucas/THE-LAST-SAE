package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import plateau.Controleur;

// Ecran de jeu : on place les metros et les departs sur le plateau.
// Chaque case est un JLabel : couleur de fond = arrondissement, texte = "M3" (metro) et/ou "D2" (depart).
public class PanelJeu extends JPanel implements ActionListener, MouseListener
{
	private FrameJeu   frame;
	private Controleur ctrl;

	private JLabel       lblInfo;
	private JRadioButton rbMetro;
	private JRadioButton rbDepart;
	private JComboBox<String> comboMetro;
	private JComboBox<String> comboDepart;
	private JButton btnImporter;
	private JButton btnSauvegarder;

	private JPanel   panelGrille;
	private JLabel[] cases;

	private Color[] tabCouleurs =
	{
		new Color(255,105,180), new Color(138, 43,226), new Color(255,165,  0), new Color(  0,255,255),
		new Color(176,196,222), new Color(255, 20,147), new Color(186, 85,211), new Color(148,  0,211),
		new Color(210,105, 30), new Color(244,164, 96), new Color(199, 21,133), new Color(123,104,238),
		new Color(255,140,  0), new Color(  0,206,209), new Color( 72, 61,139), new Color(220, 20, 60),
		new Color(169,169,169), new Color(245,222,179), new Color(255,228,225), new Color(255,192,203)
	};

	public PanelJeu(FrameJeu frame, Controleur ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		this.setLayout(new BorderLayout(10, 10));

		// ----- panneau de gauche (les controles) -----
		JPanel gauche = new JPanel(new GridLayout(0, 1, 5, 5));

		this.lblInfo  = new JLabel();
		this.rbMetro  = new JRadioButton("Placer un metro", true);
		this.rbDepart = new JRadioButton("Placer un depart");

		ButtonGroup groupe = new ButtonGroup();
		groupe.add(this.rbMetro);
		groupe.add(this.rbDepart);

		this.comboMetro    = new JComboBox<String>();
		this.comboDepart   = new JComboBox<String>();
		this.btnImporter   = new JButton("Importer une sauvegarde");
		this.btnSauvegarder= new JButton("Sauvegarder");

		this.rbMetro      .addActionListener(this);
		this.rbDepart     .addActionListener(this);
		this.btnImporter  .addActionListener(this);
		this.btnSauvegarder.addActionListener(this);

		gauche.add(this.lblInfo);
		gauche.add(this.rbMetro);
		gauche.add(this.comboMetro);
		gauche.add(this.rbDepart);
		gauche.add(this.comboDepart);
		gauche.add(this.btnImporter);
		gauche.add(this.btnSauvegarder);

		// ----- la grille (a droite) -----
		this.panelGrille = new JPanel();

		this.add(gauche, BorderLayout.WEST);
		this.add(this.panelGrille, BorderLayout.CENTER);

		construireGrille();
	}

	// remplit les infos + les listes deroulantes a partir de la config
	private void majInfos()
	{
		this.lblInfo.setText("Joueurs : " + this.ctrl.getNbJoueurs() + "  |  Metros : " + this.ctrl.getNbMetro());

		this.comboMetro.removeAllItems();
		this.comboMetro.addItem("Aucun");
		for (int i = 1; i <= this.ctrl.getNbMetro(); i++) this.comboMetro.addItem("Metro " + i);

		this.comboDepart.removeAllItems();
		this.comboDepart.addItem("Aucun");
		for (int i = 1; i <= this.ctrl.getNbJoueurs(); i++) this.comboDepart.addItem("Joueur " + i);

		this.comboMetro .setEnabled(this.rbMetro.isSelected());
		this.comboDepart.setEnabled(this.rbDepart.isSelected());
	}

	// (re)construit la grille a partir des dimensions du plateau courant
	private void construireGrille()
	{
		majInfos();

		this.panelGrille.removeAll();
		this.panelGrille.setLayout(new GridLayout(this.ctrl.getHauteur(), this.ctrl.getLargeur()));

		this.cases = new JLabel[this.ctrl.getNbCases()];
		for (int i = 0; i < this.cases.length; i++)
		{
			JLabel c = new JLabel("", SwingConstants.CENTER);
			c.setOpaque(true);
			c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			c.setPreferredSize(new Dimension(60, 60));
			c.addMouseListener(this);
			this.cases[i] = c;
			this.panelGrille.add(c);
			majCase(i);
		}

		this.panelGrille.revalidate();
		this.panelGrille.repaint();
		this.frame.pack();
	}

	// met a jour une case : couleur de l'arrondissement + texte metro/depart
	private void majCase(int i)
	{
		int arr = this.ctrl.getArrondissement(i);
		if (arr >= 1 && arr <= this.tabCouleurs.length) this.cases[i].setBackground(this.tabCouleurs[arr - 1]);
		else                                            this.cases[i].setBackground(Color.LIGHT_GRAY);

		String txt = "";
		int metro  = this.ctrl.getMetro(i);
		int depart = this.ctrl.getDepart(i);
		if (metro  > 0) txt += "M" + metro;
		if (depart > 0) txt += (txt.isEmpty() ? "" : " ") + "D" + depart;
		this.cases[i].setText(txt);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.rbMetro || e.getSource() == this.rbDepart)
		{
			this.comboMetro .setEnabled(this.rbMetro.isSelected());
			this.comboDepart.setEnabled(this.rbDepart.isSelected());
		}
		else if (e.getSource() == this.btnImporter)
		{
			importer();
		}
		else if (e.getSource() == this.btnSauvegarder)
		{
			sauvegarder();
		}
	}

	// charge un plateau : on demande le nom du fichier dans la console (pas d'explorateur)
	private void importer()
	{
		System.out.print("Nom du fichier a charger (dans sauvegarde/) : ");
		Scanner sc = new Scanner(System.in);
		String nom = sc.nextLine().trim();
		if (!nom.endsWith(".txt")) nom = nom + ".txt";

		File f = new File("sauvegarde", nom);
		if (this.ctrl.chargerPlateau(f))
		{
			System.out.println("Plateau charge : " + f.getPath());
			construireGrille();
		}
		else
		{
			System.out.println("Impossible de charger : " + f.getPath());
		}
	}

	// enregistre : chaque joueur doit avoir exactement 1 depart
	private void sauvegarder()
	{
		for (int joueur = 1; joueur <= this.ctrl.getNbJoueurs(); joueur++)
		{
			int n = 0;
			for (int i = 0; i < this.ctrl.getNbCases(); i++)
				if (this.ctrl.getDepart(i) == joueur) n++;

			if (n != 1)
			{
				System.out.println("Erreur : le joueur " + joueur + " doit avoir exactement 1 depart (actuellement " + n + ").");
				return;
			}
		}

		System.out.print("Nom pour enregistrer : ");
		Scanner sc = new Scanner(System.in);
		String nom = sc.nextLine().trim();
		if (nom.isEmpty()) { System.out.println("Nom vide : annule."); return; }

		this.ctrl.enregistrerPlateau(nom);
	}

	// clic sur une case
	public void mousePressed(MouseEvent e)
	{
		for (int i = 0; i < this.cases.length; i++)
			if (e.getSource() == this.cases[i]) { caseCliquee(i); return; }
	}

	private void caseCliquee(int i)
	{
		if (this.rbMetro.isSelected())
		{
			// index 0 = "Aucun", 1 = "Metro 1", ... donc la valeur = l'index
			this.ctrl.affecterMetro(i, this.comboMetro.getSelectedIndex());
		}
		else if (this.rbDepart.isSelected())
		{
			int joueur = this.comboDepart.getSelectedIndex(); // 0 = Aucun
			if (joueur == 0)
			{
				this.ctrl.affecterDepart(i, 0);
			}
			else
			{
				// un seul depart par joueur : on efface l'ancien
				for (int k = 0; k < this.ctrl.getNbCases(); k++)
					if (this.ctrl.getDepart(k) == joueur) this.ctrl.affecterDepart(k, 0);
				this.ctrl.affecterDepart(i, joueur);
			}
		}

		// on redessine toutes les cases (un depart a pu etre efface ailleurs)
		for (int k = 0; k < this.cases.length; k++) majCase(k);
	}

	public void mouseClicked (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}
}
