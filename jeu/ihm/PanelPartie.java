package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import javax.swing.*;

// Panneau principal de la partie : grille à droite, infos à gauche
public class PanelPartie extends JPanel
{
	private FramePartie   frame;
	private ControleurJeu ctrl;

	private PanelGrille   panelGrille;
	private PanelInfoJeu  panelInfo;

	private Image[]       stationImages = new Image[11];

	public PanelPartie(FramePartie frame, ControleurJeu ctrl)
	{
		this.frame = frame;
		this.ctrl  = ctrl;

		/*-------------------------*/
		/* Création des composants */
		/*-------------------------*/
		this.panelInfo   = new PanelInfoJeu(this, ctrl);
		this.panelGrille = new PanelGrille(ctrl, this);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setPreferredSize(new Dimension(1000, 680));

		JScrollPane scrollGrille = new JScrollPane(this.panelGrille);
		scrollGrille.setBorder(BorderFactory.createTitledBorder("Plateau"));

		this.add(this.panelInfo,   BorderLayout.WEST);
		this.add(scrollGrille,     BorderLayout.CENTER);

		/*---------------------------*/
		/* Activation des composants */
		/*---------------------------*/
		construireGrille();
	}

	// Construit (ou reconstruit) la grille selon la taille du plateau chargé
	public void construireGrille()
	{
		this.panelGrille.construireGrille();
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}

	// Appelée par CasePanelJeu quand le joueur clique sur une case
	public void caseCliquee(int index)
	{
		this.ctrl.jouerCoup(index);
		rafraichir();
		verifierFinPartie();
	}

	// Appelée par ListenerPartie quand le joueur passe son tour
	public void passerTour()
	{
		this.ctrl.passerTour();
		rafraichir();
		verifierFinPartie();
	}

	// Rafraîchit l'affichage du plateau et du panneau d'infos
	public void rafraichir()
	{
		this.panelGrille.repaint();
		this.panelInfo.rafraichir();
	}

	private void verifierFinPartie()
	{
		if (this.ctrl.isPartieTerminee())
		{
			this.frame.setVisible(false);
			new FrameResultats(this.ctrl);
		}
	}

	// Cache et mise en cache des images de stations (même système que l'éditeur)
	public Image getImageStation(int stationNum)
	{
		if (stationNum < 1 || stationNum >= this.stationImages.length) return null;
		if (this.stationImages[stationNum] == null)
		{
			String chemin = plateau.metier.UtilitaireJeu.getCheminImageStation(stationNum);
			if (chemin != null)
				this.stationImages[stationNum] = new ImageIcon(chemin).getImage();
		}
		return this.stationImages[stationNum];
	}

	public ControleurJeu getCtrl()      { return this.ctrl; }
	public FramePartie   getFrame()     { return this.frame; }
	public PanelInfoJeu  getPanelInfo() { return this.panelInfo; }
}
