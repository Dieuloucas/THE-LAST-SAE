package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Carte;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Panneau principal de la fenêtre d'un joueur.
// Gauche : infos essentielles (manche, carte, pioche, boutons).
// Centre : grille du plateau.
public class PanelJoueur extends JPanel implements ActionListener
{
	private FrameJoueur   frame;
	private ControleurJeu ctrl;
	private int           numeroJoueur;

	private PanelGrille   panelGrille;
	private PanelCarte    panelCarte;

	private JLabel        lblManche;
	private JLabel        lblStatut;
	private JLabel        lblPioche;
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
		panelInfos.setPreferredSize(new Dimension(155, 460));
		panelInfos.setBackground(new Color(25, 25, 40));
		panelInfos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Font gras18 = new Font("Arial", Font.BOLD, 18);
		Font gras12 = new Font("Arial", Font.BOLD, 12);
		Font gras11 = new Font("Arial", Font.BOLD, 11);
		Font ital10 = new Font("Arial", Font.ITALIC, 10);

		// Titre joueur
		JLabel lblTitre = new JLabel("JOUEUR " + numeroJoueur);
		lblTitre.setFont(gras18);
		lblTitre.setForeground(couleurJoueur);
		lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Manche
		this.lblManche = new JLabel("Manche — / —");
		this.lblManche.setFont(gras12);
		this.lblManche.setForeground(Color.WHITE);
		this.lblManche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Statut (en attente des autres)
		this.lblStatut = new JLabel(" ");
		this.lblStatut.setFont(ital10);
		this.lblStatut.setForeground(new Color(200, 170, 100));
		this.lblStatut.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Carte
		this.panelCarte = new PanelCarte(ctrl, numeroJoueur);
		this.panelCarte.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Pioche
		this.lblPioche = new JLabel("— foncées restantes");
		this.lblPioche.setFont(gras11);
		this.lblPioche.setForeground(new Color(180, 180, 220));
		this.lblPioche.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Boutons
		this.btnPasser  = new JButton("Passer");
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
		panelInfos.add(Box.createVerticalStrut(10));
		panelInfos.add(creerSep());
		panelInfos.add(Box.createVerticalStrut(10));
		panelInfos.add(this.panelCarte);
		panelInfos.add(Box.createVerticalStrut(8));
		panelInfos.add(this.lblPioche);
		panelInfos.add(Box.createVerticalGlue());
		panelInfos.add(creerSep());
		panelInfos.add(Box.createVerticalStrut(8));
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

		this.panelGrille.construireGrille();
		this.frame.pack();
		rafraichir();
	}

	public void caseCliquee(int index)
	{
		if (this.ctrl.isMancheJoueurTerminee(this.numeroJoueur)) return;
		this.ctrl.jouerCoup(this.numeroJoueur, index);
		this.ctrl.rafraichirTout();
	}

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

	public void rafraichir()
	{
		int manche    = this.ctrl.getNumeroManche();
		int nbManches = this.ctrl.getNbManches();
		this.lblManche.setText("Manche " + manche + " / " + nbManches);

		if (this.ctrl.isMancheJoueurTerminee(this.numeroJoueur))
		{
			this.lblStatut.setText("En attente…");
			this.btnPasser.setEnabled(false);
		}
		else
		{
			this.lblStatut.setText(" ");
			this.btnPasser.setEnabled(true);
		}

		this.panelCarte.repaint();

		int fonceesR = this.ctrl.getNbFonceesRestantes(this.numeroJoueur);
		this.lblPioche.setText(fonceesR + " foncée(s) restante(s)");

		this.panelGrille.repaint();
	}

	private JSeparator creerSep()
	{
		JSeparator sep = new JSeparator();
		sep.setMaximumSize(new Dimension(140, 4));
		sep.setAlignmentX(Component.LEFT_ALIGNMENT);
		sep.setForeground(new Color(80, 80, 100));
		return sep;
	}
}
