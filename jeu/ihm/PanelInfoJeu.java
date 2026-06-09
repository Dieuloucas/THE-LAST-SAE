package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Carte;
import jeu.metier.Joueur;
import plateau.metier.UtilitaireJeu;

import java.awt.*;
import javax.swing.*;

public class PanelInfoJeu extends JPanel
{
	private PanelPartie   panelPartie;
	private ControleurJeu ctrl;

	// Manche
	private JLabel        lblManche;

	// Joueur courant
	private JLabel        lblJoueurCourant;

	// Carte tirée
	private JLabel        lblCarteNom;
	private JLabel        lblCarteFonce;
	private JLabel        lblImageCarte;

	// Pioche
	private JLabel        lblPioche;

	// Scores
	private JLabel[]      lblScores;

	// Boutons
	private JButton       btnPasser;
	private JButton       btnQuitter;

	private ListenerPartie listener;

	public PanelInfoJeu(PanelPartie panelPartie, ControleurJeu ctrl)
	{
		this.panelPartie = panelPartie;
		this.ctrl        = ctrl;
		this.listener    = new ListenerPartie(panelPartie);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setPreferredSize(new Dimension(260, 600));
		this.setBackground(new Color(30, 30, 45));

		Font gras16 = new Font("Arial", Font.BOLD, 16);
		Font gras13 = new Font("Arial", Font.BOLD, 13);
		Font gras11 = new Font("Arial", Font.BOLD, 11);
		Font ital11 = new Font("Arial", Font.ITALIC, 11);

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/

		// — Manche —
		this.lblManche = new JLabel("Manche — / —");
		this.lblManche.setFont(gras16);
		this.lblManche.setForeground(Color.WHITE);
		this.lblManche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// — Joueur courant —
		this.lblJoueurCourant = new JLabel("Tour du joueur —");
		this.lblJoueurCourant.setFont(gras13);
		this.lblJoueurCourant.setForeground(Color.WHITE);
		this.lblJoueurCourant.setAlignmentX(Component.LEFT_ALIGNMENT);

		// — Bloc carte : image + nom + nature —
		JPanel panelCarte = new JPanel(new BorderLayout(6, 4));
		panelCarte.setBackground(new Color(50, 50, 70));
		panelCarte.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(120, 120, 160)),
			"Carte tirée",
			javax.swing.border.TitledBorder.LEFT,
			javax.swing.border.TitledBorder.TOP,
			gras11,
			new Color(180, 180, 220)
		));
		panelCarte.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelCarte.setMaximumSize(new Dimension(240, 80));

		this.lblImageCarte = new JLabel();
		this.lblImageCarte.setPreferredSize(new Dimension(50, 50));
		this.lblImageCarte.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelCarteTexte = new JPanel(new GridLayout(2, 1, 2, 2));
		panelCarteTexte.setOpaque(false);

		this.lblCarteNom = new JLabel("—");
		this.lblCarteNom.setFont(gras13);
		this.lblCarteNom.setForeground(Color.WHITE);

		this.lblCarteFonce = new JLabel("—");
		this.lblCarteFonce.setFont(ital11);
		this.lblCarteFonce.setForeground(Color.LIGHT_GRAY);

		panelCarteTexte.add(this.lblCarteNom);
		panelCarteTexte.add(this.lblCarteFonce);

		panelCarte.add(this.lblImageCarte, BorderLayout.WEST);
		panelCarte.add(panelCarteTexte,    BorderLayout.CENTER);

		// — Pioche —
		this.lblPioche = new JLabel("Pioche : —");
		this.lblPioche.setFont(gras11);
		this.lblPioche.setForeground(new Color(180, 180, 220));
		this.lblPioche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// — Scores —
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

		// — Boutons —
		this.btnPasser  = new JButton("Passer le tour");
		this.btnQuitter = new JButton("Quitter");
		this.btnPasser .setFont(gras11);
		this.btnQuitter.setFont(gras11);
		this.btnPasser .setAlignmentX(Component.LEFT_ALIGNMENT);
		this.btnQuitter.setAlignmentX(Component.LEFT_ALIGNMENT);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.lblManche);
		this.add(Box.createVerticalStrut(8));
		this.add(creerSep());
		this.add(Box.createVerticalStrut(8));
		this.add(this.lblJoueurCourant);
		this.add(Box.createVerticalStrut(8));
		this.add(panelCarte);
		this.add(Box.createVerticalStrut(4));
		this.add(this.lblPioche);
		this.add(Box.createVerticalStrut(8));
		this.add(creerSep());
		this.add(Box.createVerticalStrut(8));
		this.add(lblTitreScores);
		this.add(Box.createVerticalStrut(4));
		for (int i = 0; i < 4; i++) this.add(this.lblScores[i]);
		this.add(Box.createVerticalGlue());
		this.add(creerSep());
		this.add(Box.createVerticalStrut(8));
		this.add(this.btnPasser);
		this.add(Box.createVerticalStrut(4));
		this.add(this.btnQuitter);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		this.btnPasser .addActionListener(this.listener);
		this.btnQuitter.addActionListener(this.listener);

		rafraichir();
	}

	public void rafraichir()
	{
		// — Manche —
		int manche    = this.ctrl.getNumeroManche();
		int nbManches = this.ctrl.getNbManches();
		this.lblManche.setText("Manche " + manche + " / " + nbManches);

		// — Joueur courant —
		Joueur joueur = this.ctrl.getJoueurCourant();
		if (joueur != null)
		{
			this.lblJoueurCourant.setText("Tour du Joueur " + joueur.getNumero());
			this.lblJoueurCourant.setForeground(this.ctrl.getCouleurJoueur(joueur.getNumero()));
		}

		// — Carte tirée —
		Carte carte = this.ctrl.getCarteCourante();
		if (carte != null)
		{
			String nomType;
			if (carte.estJoker())
				nomType = "Joker";
			else
			{
				String[] noms = UtilitaireJeu.getNomsStations();
				int idx = carte.getTypeStation() - 1;
				nomType = (idx >= 0 && idx < noms.length) ? noms[idx] : "Station " + carte.getTypeStation();
			}
			this.lblCarteNom.setText(nomType);

			if (carte.estFoncee())
			{
				this.lblCarteFonce.setText("Foncée — compte pour la fin");
				this.lblCarteFonce.setForeground(new Color(255, 140, 100));
			}
			else
			{
				this.lblCarteFonce.setText("Claire — ne compte pas");
				this.lblCarteFonce.setForeground(new Color(160, 200, 160));
			}

			// Image de la carte (même image que la station sur le plateau)
			if (!carte.estJoker())
			{
				java.awt.Image img = this.ctrl.getImageStation(carte.getTypeStation());
				if (img != null)
				{
					java.awt.Image scaled = img.getScaledInstance(46, 46, java.awt.Image.SCALE_SMOOTH);
					this.lblImageCarte.setIcon(new javax.swing.ImageIcon(scaled));
				}
				else
				{
					this.lblImageCarte.setIcon(null);
				}
			}
			else
			{
				this.lblImageCarte.setIcon(null);
				this.lblImageCarte.setText("★");
				this.lblImageCarte.setForeground(Color.YELLOW);
				this.lblImageCarte.setFont(new Font("Arial", Font.BOLD, 24));
			}
		}

		// — Pioche —
		int fonceesRestantes = this.ctrl.getNbFonceesRestantes();
		int totalRestantes   = this.ctrl.getNbCartesRestantes();
		this.lblPioche.setText("Pioche : " + fonceesRestantes + " foncées / " + totalRestantes + " cartes");

		// — Scores —
		Joueur[] joueurs = this.ctrl.getJoueurs();
		for (int i = 0; i < 4; i++)
		{
			if (i < joueurs.length)
			{
				Joueur j      = joueurs[i];
				int mScoreCourant = j.getScoreManche(manche - 1);
				int total     = j.getScoreTotal();
				this.lblScores[i].setText("J" + j.getNumero() + " : " + mScoreCourant + " pt  (total : " + total + ")");
				this.lblScores[i].setVisible(true);
			}
			else
			{
				this.lblScores[i].setVisible(false);
			}
		}
	}

	private JSeparator creerSep()
	{
		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(240, 4));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		sep.setForeground(new Color(80, 80, 100));
		return sep;
	}

	public JButton getBtnPasser()  { return this.btnPasser; }
	public JButton getBtnQuitter() { return this.btnQuitter; }
}
