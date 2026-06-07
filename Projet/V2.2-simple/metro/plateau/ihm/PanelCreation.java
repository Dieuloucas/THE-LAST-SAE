package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import plateau.Controleur;

// Ecran de creation : on peint les cases pour dessiner les arrondissements.
public class PanelCreation extends JPanel implements MouseListener, ActionListener
{
	private FrameCreation frmCreation;
	private Controleur    ctrl;

	private int largeur;
	private int hauteur;
	private int nbArrondissements;
	private int tailleCases;

	private JPanel[]  tabCases;            // les cases du plateau
	private JButton[] btnArrondissements;  // un bouton par arrondissement

	private JButton btnValider;
	private JButton btnRetour;

	private int   arrondissementChoisi = 0;
	private Color couleurChoisie       = Color.LIGHT_GRAY;

	// 20 couleurs (une par arrondissement possible)
	private Color[] tabCouleurs =
	{
		new Color(255,105,180), new Color(138, 43,226), new Color(255,165,  0), new Color(  0,255,255),
		new Color(176,196,222), new Color(255, 20,147), new Color(186, 85,211), new Color(148,  0,211),
		new Color(210,105, 30), new Color(244,164, 96), new Color(199, 21,133), new Color(123,104,238),
		new Color(255,140,  0), new Color(  0,206,209), new Color( 72, 61,139), new Color(220, 20, 60),
		new Color(169,169,169), new Color(245,222,179), new Color(255,228,225), new Color(255,192,203)
	};

	public PanelCreation(FrameCreation frmCreation, Controleur ctrl, int nbArrondissements, int tailleCases)
	{
		this.frmCreation       = frmCreation;
		this.ctrl              = ctrl;
		this.nbArrondissements = nbArrondissements;
		this.tailleCases       = tailleCases;
		this.largeur           = ctrl.getLargeur();
		this.hauteur           = ctrl.getHauteur();

		this.setLayout(new BorderLayout());

		// 1) la grille des cases
		JPanel panelGrille = new JPanel(new GridLayout(this.hauteur, this.largeur));
		this.tabCases = new JPanel[this.largeur * this.hauteur];
		for (int i = 0; i < this.tabCases.length; i++)
		{
			JPanel c = new JPanel();
			c.setBackground(Color.LIGHT_GRAY);
			c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			c.setPreferredSize(new Dimension(this.tailleCases, this.tailleCases));
			c.addMouseListener(this);
			this.tabCases[i] = c;
			panelGrille.add(c);
		}

		// 2) les boutons d'arrondissements (a gauche)
		JPanel panelArr = new JPanel(new GridLayout(this.nbArrondissements, 1, 5, 5));
		this.btnArrondissements = new JButton[this.nbArrondissements];
		for (int i = 0; i < this.nbArrondissements; i++)
		{
			JButton b = new JButton("Arrondissement " + (i + 1));
			b.setBackground(this.tabCouleurs[i]);
			b.addActionListener(this);
			this.btnArrondissements[i] = b;
			panelArr.add(b);
		}

		// 3) les boutons du bas
		this.btnRetour  = new JButton("Retour configuration");
		this.btnValider = new JButton("Valider et enregistrer");
		this.btnRetour .addActionListener(this);
		this.btnValider.addActionListener(this);
		JPanel panelBas = new JPanel();
		panelBas.add(this.btnRetour);
		panelBas.add(this.btnValider);

		this.add(panelGrille, BorderLayout.CENTER);
		this.add(panelArr,    BorderLayout.WEST);
		this.add(panelBas,    BorderLayout.SOUTH);
	}

	// clic sur une case : on la peint et on previent le metier
	public void mousePressed(MouseEvent e)
	{
		for (int i = 0; i < this.tabCases.length; i++)
		{
			if (e.getSource() == this.tabCases[i])
			{
				this.tabCases[i].setBackground(this.couleurChoisie);
				this.ctrl.affecterArrondissement(i, this.arrondissementChoisi);
				return;
			}
		}
	}

	public void mouseClicked (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}

	public void actionPerformed(ActionEvent e)
	{
		// choix d'un arrondissement
		for (int i = 0; i < this.nbArrondissements; i++)
		{
			if (e.getSource() == this.btnArrondissements[i])
			{
				this.couleurChoisie       = this.tabCouleurs[i];
				this.arrondissementChoisi = i + 1;
				return;
			}
		}

		if (e.getSource() == this.btnRetour)
		{
			new FrameConfiguration(this.ctrl);
			this.frmCreation.dispose();
			return;
		}

		if (e.getSource() == this.btnValider)
		{
			// toutes les cases doivent etre peintes
			for (int i = 0; i < this.tabCases.length; i++)
			{
				if (this.tabCases[i].getBackground().equals(Color.LIGHT_GRAY))
				{
					System.out.println("Veuillez remplir toutes les cases avant de valider.");
					return;
				}
			}

			// on demande le nom dans la console (pas de fenetre de saisie)
			System.out.print("Nom du plateau : ");
			Scanner sc = new Scanner(System.in);
			String nom = sc.nextLine().trim();
			if (nom.isEmpty())
			{
				System.out.println("Nom vide : enregistrement annule.");
				return;
			}

			if (this.ctrl.enregistrerPlateau(nom))
			{
				new FrameJeu(this.ctrl);
				this.frmCreation.dispose();
			}
		}
	}
}
