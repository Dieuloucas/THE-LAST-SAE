package plateau.ihm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import plateau.Controleur;

// Ecran de creation : on peint les cases pour dessiner les arrondissements.
// L'IHM ne fait que peindre + prevenir le controleur ; les regles sont dans le metier.
public class PanelCreation extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
	private FrameCreation frmCreation;
	private Controleur    ctrl;

	private int largeur;
	private int hauteur;
	private int nbArrondissements;
	private int tailleCases;

	private JPanel    panelPlateau;
	private JPanel[]  cases;
	private JButton[] btnArrondissements;
	private JButton   btnValider;
	private JButton   btnRetour;
	private JTextField txtNom;

	private int   arrondissementChoisi = 0;
	private Color couleurChoisie       = Color.LIGHT_GRAY;

	private Color[] tabCouleurs = Couleurs.PALETTE;

	public PanelCreation(FrameCreation frmCreation, Controleur ctrl, int nbArrondissements, int tailleCases)
	{
		this.frmCreation       = frmCreation;
		this.ctrl              = ctrl;
		this.nbArrondissements = nbArrondissements;
		this.tailleCases       = tailleCases;
		this.largeur           = ctrl.getLargeur();
		this.hauteur           = ctrl.getHauteur();

		this.setLayout(new BorderLayout());

		// la grille
		this.panelPlateau = new JPanel(new GridLayout(this.hauteur, this.largeur, 0, 0));
		this.cases = new JPanel[this.largeur * this.hauteur];
		for (int i = 0; i < this.cases.length; i++)
		{
			JPanel c = new JPanel();
			c.setBackground(Color.LIGHT_GRAY);
			c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			c.setPreferredSize(new Dimension(this.tailleCases, this.tailleCases));
			this.cases[i] = c;
			this.panelPlateau.add(c);
		}

		// les boutons d'arrondissements (colores)
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

		// bas : nom + boutons
		this.txtNom    = new JTextField("mon_plateau", 15);
		this.btnValider = new JButton("Valider et enregistrer");
		this.btnRetour  = new JButton("Retour configuration");
		this.btnValider.addActionListener(this);
		this.btnRetour.addActionListener(this);

		JPanel bas = new JPanel();
		bas.add(new JLabel("Nom :"));
		bas.add(this.txtNom);
		bas.add(this.btnRetour);
		bas.add(this.btnValider);

		this.add(this.panelPlateau, BorderLayout.CENTER);
		this.add(panelArr,          BorderLayout.WEST);
		this.add(bas,               BorderLayout.SOUTH);

		this.panelPlateau.addMouseListener(this);
		this.panelPlateau.addMouseMotionListener(this);
	}

	// peint la case sous la souris et previent le metier
	private void peindreCaseSousSouris(MouseEvent e)
	{
		Component comp = this.panelPlateau.getComponentAt(e.getPoint());
		for (int i = 0; i < this.cases.length; i++)
		{
			if (comp == this.cases[i])
			{
				this.cases[i].setBackground(this.couleurChoisie);
				this.ctrl.affecterArrondissement(i, this.arrondissementChoisi);
				return;
			}
		}
	}

	public void mousePressed(MouseEvent e) { peindreCaseSousSouris(e); }
	public void mouseDragged(MouseEvent e) { peindreCaseSousSouris(e); }
	public void mouseMoved   (MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}

	public void actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < this.nbArrondissements; i++)
			if (e.getSource() == this.btnArrondissements[i])
			{
				this.couleurChoisie       = this.tabCouleurs[i];
				this.arrondissementChoisi = i + 1;
				return;
			}

		if (e.getSource() == this.btnRetour)
		{
			new FrameConfiguration(this.ctrl);
			this.frmCreation.dispose();
			return;
		}

		if (e.getSource() == this.btnValider)
		{
			// c'est le METIER qui dit si toutes les cases sont remplies
			if (!this.ctrl.toutesCasesRemplies())
			{
				JOptionPane.showMessageDialog(this, "Veuillez remplir toutes les cases avant de valider.",
					"Cases non remplies", JOptionPane.WARNING_MESSAGE);
				return;
			}

			String nom = this.txtNom.getText().trim();
			if (nom.isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Le nom ne peut pas etre vide.", "Nom invalide", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (this.ctrl.enregistrer(nom))
			{
				new FrameJeu(this.ctrl);
				this.frmCreation.dispose();
			}
		}
	}
}
