package plateau.ihm;

import plateau.Controleur;
import plateau.metier.Carte;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;

import java.awt.event.*;
import javax.swing.*;

// Longue bande verticale centrale : carte commune (pioche) + statut de chaque joueur + boutons.
// Design simple : fond clair, texte noir lisible.
public class PanelInfos extends JPanel implements ActionListener
{
	private Controleur ctrl;
	private int        nbJoueurs;

	private PanelCarte panelCarte;
	private JLabel     lblManche;
	private JLabel     lblPioche;
	private JLabel[]   lblStatutJoueur;
	private JButton[]  btnPasserJoueur;
	private JButton    btnQuitter;

	// MODE DEBUG : sélecteur pour forcer la carte commune (restent null hors mode debug)
	private JComboBox<String> cmbTypeDebug;
	private JCheckBox         chkFonceeDebug;
	private JButton           btnForcerDebug;
	private JButton           btnSauterManche;

	public PanelInfos(Controleur ctrl)
	{
		this.ctrl      = ctrl;
		this.nbJoueurs = ctrl.getNbJoueurs();

		// Bande blanche étroite, plus haute que large
		this.setPreferredSize(new Dimension(220, 660));
		this.setBackground(Color.WHITE);

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		Font gras16   = new Font("Arial", Font.BOLD, 16);
		Font gras12   = new Font("Arial", Font.BOLD, 12);
		Font normal12 = new Font("Arial", Font.PLAIN, 12);

		JLabel lblTitre = new JLabel("Infos de jeu");
		lblTitre.setFont(gras16);

		this.lblManche = new JLabel("Manche — / —");
		this.lblManche.setFont(gras12);

		JLabel lblCarteTitre = new JLabel("Carte commune");
		lblCarteTitre.setFont(gras12);

		this.panelCarte = new PanelCarte(ctrl);

		this.lblPioche = new JLabel("— foncée(s) restante(s)");
		this.lblPioche.setFont(normal12);

		JLabel lblJoueurs = new JLabel("Joueurs");
		lblJoueurs.setFont(gras12);

		this.btnQuitter = new JButton("Quitter");
		this.btnQuitter.setFont(normal12);

		// Un libellé d'état et un bouton "Passer" pour chaque joueur
		this.lblStatutJoueur = new JLabel[this.nbJoueurs];
		this.btnPasserJoueur = new JButton[this.nbJoueurs];
		for (int j = 1; j <= this.nbJoueurs; j++)
		{
			JLabel statut = new JLabel("Joueur " + j + " : à jouer");
			statut.setFont(normal12);

			JButton pass = new JButton("Passer (Joueur " + j + ")");
			pass.setFont(normal12);
			pass.setActionCommand("PASSER_" + j);

			this.lblStatutJoueur[j - 1] = statut;
			this.btnPasserJoueur[j - 1] = pass;
		}

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

		// Tout est centré horizontalement dans la bande
		lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.lblManche.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCarteTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panelCarte.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panelCarte.setMaximumSize(new Dimension(120, 160));
		this.lblPioche.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblJoueurs.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.btnQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.btnQuitter.setMaximumSize(new Dimension(180, 28));

		// Haut de la bande : titre, manche en cours, carte commune et reste de la pioche
		this.add(lblTitre);
		this.add(Box.createVerticalStrut(6));
		this.add(this.lblManche);
		this.add(Box.createVerticalStrut(12));
		this.add(lblCarteTitre);
		this.add(Box.createVerticalStrut(4));
		this.add(this.panelCarte);
		this.add(Box.createVerticalStrut(6));
		this.add(this.lblPioche);
		this.add(Box.createVerticalStrut(14));
		this.add(new JSeparator());
		this.add(Box.createVerticalStrut(10));

		// Milieu : l'état de chaque joueur suivi de son bouton "Passer"
		this.add(lblJoueurs);
		this.add(Box.createVerticalStrut(8));
		for (int j = 0; j < this.nbJoueurs; j++)
		{
			this.lblStatutJoueur[j].setAlignmentX(Component.CENTER_ALIGNMENT);
			this.btnPasserJoueur[j].setAlignmentX(Component.CENTER_ALIGNMENT);
			this.btnPasserJoueur[j].setMaximumSize(new Dimension(180, 26));

			this.add(this.lblStatutJoueur[j]);
			this.add(Box.createVerticalStrut(3));
			this.add(this.btnPasserJoueur[j]);
			this.add(Box.createVerticalStrut(10));
		}

		// Bas : le bouton "Quitter", repoussé tout en bas par le glue
		this.add(Box.createVerticalGlue());
		this.add(this.btnQuitter);

		/*-------------------------*/
		/* DEBUG : forcer la carte */
		/*-------------------------*/
		if (this.ctrl.isModeDebug())
		{
			this.add(Box.createVerticalStrut(12));
			this.add(new JSeparator());
			this.add(Box.createVerticalStrut(6));

			JLabel lblDebug = new JLabel("DEBUG : forcer la carte");
			lblDebug.setFont(gras12);
			lblDebug.setForeground(new Color(180, 0, 0));
			lblDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(lblDebug);
			this.add(Box.createVerticalStrut(5));

			// Un choix par type de station (1..N), plus "Joker" en dernier.
			this.cmbTypeDebug = new JComboBox<String>();
			for (int t = 1; t <= this.ctrl.getNbStations(); t++)
				this.cmbTypeDebug.addItem("Station " + t);
			this.cmbTypeDebug.addItem("Joker");
			this.cmbTypeDebug.setFont(normal12);
			this.cmbTypeDebug.setMaximumSize(new Dimension(180, 26));
			this.cmbTypeDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(this.cmbTypeDebug);
			this.add(Box.createVerticalStrut(5));

			this.chkFonceeDebug = new JCheckBox("Carte foncée");
			this.chkFonceeDebug.setFont(normal12);
			this.chkFonceeDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(this.chkFonceeDebug);
			this.add(Box.createVerticalStrut(5));

			this.btnForcerDebug = new JButton("Forcer la carte");
			this.btnForcerDebug.setFont(normal12);
			this.btnForcerDebug.setMaximumSize(new Dimension(180, 26));
			this.btnForcerDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.btnForcerDebug.addActionListener(this);
			this.add(this.btnForcerDebug);

			this.add(Box.createVerticalStrut(5));

			this.btnSauterManche = new JButton("Sauter la manche");
			this.btnSauterManche.setFont(normal12);
			this.btnSauterManche.setMaximumSize(new Dimension(180, 26));
			this.btnSauterManche.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.btnSauterManche.addActionListener(this);
			this.add(this.btnSauterManche);
		}

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		for (int j = 0; j < this.nbJoueurs; j++)
			this.btnPasserJoueur[j].addActionListener(this);
		this.btnQuitter.addActionListener(this);

		rafraichir();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnQuitter)
		{
			System.exit(0);
			return;
		}

		// DEBUG : forcer la carte commune choisie dans le sélecteur
		if (this.btnForcerDebug != null && e.getSource() == this.btnForcerDebug)
		{
			int idx = this.cmbTypeDebug.getSelectedIndex();
			int nb  = this.ctrl.getNbStations();
			// idx 0..nb-1 -> Station 1..N ; dernier indice -> Joker
			int type = (idx >= 0 && idx < nb) ? (idx + 1) : Carte.JOKER;
			this.ctrl.forcerCarte(type, this.chkFonceeDebug.isSelected());
			this.ctrl.rafraichirTout();
			return;
		}

		// DEBUG : sauter la manche en cours (fin forcée)
		if (this.btnSauterManche != null && e.getSource() == this.btnSauterManche)
		{
			this.ctrl.sauterManche();
			return;
		}

		String cmd = e.getActionCommand();
		if (cmd != null && cmd.startsWith("PASSER_"))
		{
			int j = Integer.parseInt(cmd.substring(7));
			if (!this.ctrl.aJoue(j) && !this.ctrl.isPartieTerminee())
			{
				this.ctrl.passerTour(j);
				this.ctrl.rafraichirTout();
			}
		}
	}

	public void rafraichir()
	{
		this.lblManche.setText("Manche " + this.ctrl.getNumeroManche() + " / " + this.ctrl.getNbManches());
		this.panelCarte.repaint();
		this.lblPioche.setText(this.ctrl.getNbFonceesRestantes() + " foncée(s) restante(s)");

		for (int j = 1; j <= this.nbJoueurs; j++)
		{
			if (this.ctrl.isPartieTerminee())
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : terminé");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
			else if (this.ctrl.aJoue(j))
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : A déjà joué");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
			else if (this.ctrl.estSonTour(j))
			{
				// C'est à ce joueur de jouer : lui seul peut passer son tour
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : Jouer");
				this.btnPasserJoueur[j - 1].setEnabled(true);
			}
			else
			{
				this.lblStatutJoueur[j - 1].setText("Joueur " + j + " : En attente");
				this.btnPasserJoueur[j - 1].setEnabled(false);
			}
		}
	}
}
