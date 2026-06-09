package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Carte;
import jeu.metier.Joueur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Panneau principal de la fenêtre d'un joueur.
// Gauche : informations (manche, carte, pioche, scores, boutons).
// Centre : grille du plateau avec le réseau de tous les joueurs.
public class PanelJoueur extends JPanel implements ActionListener
{
	private FrameJoueur   frame;
	private ControleurJeu ctrl;
	private int           numeroJoueur;

	private PanelGrille   panelGrille;
	private PanelCarte    panelCarte;

	private JLabel        lblManche;
	private JLabel        lblStatut;
	private JLabel        lblNatureCarte;
	private JLabel        lblPioche;
	private JLabel[]      lblScores;
	private JButton       btnPasser;
	private JButton       btnQuitter;

	public PanelJoueur(FrameJoueur frame, ControleurJeu ctrl, int numeroJoueur)
	{
		this.frame        = frame;
		this.ctrl         = ctrl;
		this.numeroJoueur = numeroJoueur;

		this.setLayout(new BorderLayout(8, 8));
		this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		Color couleurJoueur = ControleurJeu.COULEURS_JOUEURS[numeroJoueur - 1];

		/*---------------------------------*/
		/* Panneau d'infos (côté gauche)   */
		/*---------------------------------*/
		JPanel panelInfos = new JPanel();
		panelInfos.setLayout(new BoxLayout(panelInfos, BoxLayout.Y_AXIS));
		panelInfos.setPreferredSize(new Dimension(195, 520));
		panelInfos.setBackground(new Color(25, 25, 40));
		panelInfos.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		Font gras18 = new Font("Arial", Font.BOLD, 18);
		Font gras13 = new Font("Arial", Font.BOLD, 13);
		Font gras11 = new Font("Arial", Font.BOLD, 11);
		Font ital10 = new Font("Arial", Font.ITALIC, 10);

		// En-tête : "JOUEUR N" en grand dans la couleur du joueur
		JLabel lblTitre = new JLabel("JOUEUR " + numeroJoueur);
		lblTitre.setFont(gras18);
		lblTitre.setForeground(couleurJoueur);
		lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Manche courante
		this.lblManche = new JLabel("Manche — / —");
		this.lblManche.setFont(gras13);
		this.lblManche.setForeground(Color.WHITE);
		this.lblManche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Statut (en attente des autres joueurs…)
		this.lblStatut = new JLabel(" ");
		this.lblStatut.setFont(ital10);
		this.lblStatut.setForeground(new Color(200, 170, 100));
		this.lblStatut.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Carte tirée
		JLabel lblTitreCarte = new JLabel("Carte tirée :");
		lblTitreCarte.setFont(gras11);
		lblTitreCarte.setForeground(new Color(180, 180, 220));
		lblTitreCarte.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.panelCarte = new PanelCarte(ctrl, numeroJoueur);
		this.panelCarte.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.lblNatureCarte = new JLabel(" ");
		this.lblNatureCarte.setFont(ital10);
		this.lblNatureCarte.setForeground(Color.LIGHT_GRAY);
		this.lblNatureCarte.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Pioche
		this.lblPioche = new JLabel("Pioche : —");
		this.lblPioche.setFont(gras11);
		this.lblPioche.setForeground(new Color(180, 180, 220));
		this.lblPioche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Scores des joueurs
		JLabel lblTitreScores = new JLabel("Scores");
		lblTitreScores.setFont(gras13);
		lblTitreScores.setForeground(Color.WHITE);
		lblTitreScores.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.lblScores = new JLabel[4];
		for (int i = 0; i < 4; i++)
		{
			this.lblScores[i] = new JLabel("Joueur " + (i + 1) + " : —");
			this.lblScores[i].setFont(gras11);
			this.lblScores[i].setForeground(ControleurJeu.COULEURS_JOUEURS[i]);
			this.lblScores[i].setAlignmentX(Component.LEFT_ALIGNMENT);
		}

		// Boutons
		this.btnPasser  = new JButton("Passer ce tour");
		this.btnQuitter = new JButton("Quitter");
		this.btnPasser .setFont(gras11);
		this.btnQuitter.setFont(gras11);
		this.btnPasser .setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnQuitter.setAlignmentX(Component.LEFT_ALIGNMENT);

		/*---------------------------------*/
		/* Assemblage du panneau d'infos   */
		/*---------------------------------*/
		panelInfos.add(lblTitre);
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(this.lblManche);
		panelInfos.add(Box.createVerticalStrut(4));
		panelInfos.add(this.lblStatut);
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(creerSep());
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(lblTitreCarte);
		panelInfos.add(Box.createVerticalStrut(4));
		panelInfos.add(this.panelCarte);
		panelInfos.add(Box.createVerticalStrut(4));
		panelInfos.add(this.lblNatureCarte);
		panelInfos.add(Box.createVerticalStrut(4));
		panelInfos.add(this.lblPioche);
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(creerSep());
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(lblTitreScores);
		panelInfos.add(Box.createVerticalStrut(4));
		for (int i = 0; i < 4; i++) panelInfos.add(this.lblScores[i]);
		panelInfos.add(Box.createVerticalGlue());
		panelInfos.add(creerSep());
		panelInfos.add(Box.createVerticalStrut(6));
		panelInfos.add(this.btnPasser);
		panelInfos.add(Box.createVerticalStrut(4));
		panelInfos.add(this.btnQuitter);

		/*---------------------------------*/
		/* Grille du plateau               */
		/*---------------------------------*/
		this.panelGrille = new PanelGrille(ctrl, this, numeroJoueur);
		JScrollPane scroll = new JScrollPane(this.panelGrille);
		scroll.setBorder(BorderFactory.createLineBorder(couleurJoueur, 2));

		/*---------------------------------*/
		/* Assemblage final                */
		/*---------------------------------*/
		this.add(panelInfos, BorderLayout.WEST);
		this.add(scroll,     BorderLayout.CENTER);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnPasser .addActionListener(this);
		this.btnQuitter.addActionListener(this);

		// Construction initiale de la grille
		this.panelGrille.construireGrille();
		this.frame.pack();
		rafraichir();
	}

	// Appelée par PanelGrille quand le joueur clique sur une case
	public void caseCliquee(int index)
	{
		if (this.ctrl.isMancheJoueurTerminee(this.numeroJoueur)) return;
		this.ctrl.jouerCoup(this.numeroJoueur, index);
		this.ctrl.rafraichirTout();
	}

	// Gestion des boutons "Passer" et "Quitter"
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnPasser)
		{
			if (!this.ctrl.isMancheJoueurTerminee(this.numeroJoueur))
			{
				this.ctrl.passerTour(this.numeroJoueur);
				this.ctrl.rafraichirTout();
			}
		}
		else if (e.getSource() == this.btnQuitter)
		{
			System.exit(0);
		}
	}

	// Met à jour tous les composants selon l'état courant du jeu
	public void rafraichir()
	{
		int manche    = this.ctrl.getNumeroManche();
		int nbManches = this.ctrl.getNbManches();
		this.lblManche.setText("Manche " + manche + " / " + nbManches);

		// Statut : désactivation une fois les foncées épuisées
		if (this.ctrl.isMancheJoueurTerminee(this.numeroJoueur))
		{
			this.lblStatut.setText("En attente des autres joueurs…");
			this.btnPasser.setEnabled(false);
		}
		else
		{
			this.lblStatut.setText(" ");
			this.btnPasser.setEnabled(true);
		}

		// Carte courante (repaint du composant dessiné)
		this.panelCarte.repaint();

		// Nature de la carte
		Carte carte = this.ctrl.getCarteCourante(this.numeroJoueur);
		if (carte != null)
		{
			if (carte.estFoncee())
			{
				this.lblNatureCarte.setText("Foncée — à poser sur le réseau");
				this.lblNatureCarte.setForeground(new Color(255, 140, 100));
			}
			else
			{
				this.lblNatureCarte.setText("Claire — facultative");
				this.lblNatureCarte.setForeground(new Color(100, 200, 100));
			}
		}
		else
		{
			this.lblNatureCarte.setText(" ");
		}

		// Pioche
		int fonceesR = this.ctrl.getNbFonceesRestantes(this.numeroJoueur);
		int totalR   = this.ctrl.getNbCartesRestantes(this.numeroJoueur);
		this.lblPioche.setText(fonceesR + " foncées / " + totalR + " restantes");

		// Scores de tous les joueurs
		Joueur[] joueurs = this.ctrl.getJoueurs();
		for (int i = 0; i < 4; i++)
		{
			if (i < joueurs.length)
			{
				Joueur j  = joueurs[i];
				int score = j.getScoreManche(manche - 1);
				int total = j.getScoreTotal();
				this.lblScores[i].setText("J" + j.getNumero() + " : " + score + " pt  (tot. " + total + ")");
				this.lblScores[i].setVisible(true);
			}
			else
			{
				this.lblScores[i].setVisible(false);
			}
		}

		this.panelGrille.repaint();
	}

	private JSeparator creerSep()
	{
		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(185, 4));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		sep.setForeground(new Color(80, 80, 100));
		return sep;
	}
}
