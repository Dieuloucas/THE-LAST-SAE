package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import plateau.metier.UtilitaireJeu;

// Une case de la grille de jeu : affiche l'arrondissement, la station, le départ
// et met en surbrillance les coups valides du joueur courant
public class CasePanelJeu extends JPanel
{
	private int           index;
	private ControleurJeu ctrl;
	private PanelPartie   panelPartie;
	private Color[]       tabCouleurs;

	public CasePanelJeu(int index, ControleurJeu ctrl, PanelPartie panelPartie)
	{
		this.index       = index;
		this.ctrl        = ctrl;
		this.panelPartie = panelPartie;
		this.tabCouleurs = UtilitaireJeu.getCouleurs();

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setPreferredSize(new Dimension(50, 50));

		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				CasePanelJeu.this.panelPartie.caseCliquee(CasePanelJeu.this.index);
			}
		});
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();

		// 1. Fond : couleur de l'arrondissement
		int arr = this.ctrl.getArrondissement(this.index);
		if (arr >= 1 && arr <= this.tabCouleurs.length)
			g.setColor(this.tabCouleurs[arr - 1]);
		else
			g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, w, h);

		// 2. Overlay coloré si la case appartient au réseau d'un joueur
		int nbJoueurs = this.ctrl.getNbJoueurs();
		for (int j = 1; j <= nbJoueurs; j++)
		{
			if (this.ctrl.estDansReseau(j, this.index))
			{
				Color c = this.ctrl.getCouleurJoueur(j);
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 100));
				g.fillRect(0, 0, w, h);
				break;
			}
		}

		// 3. Image de la station
		int station = this.ctrl.getStation(this.index);
		if (station > 0)
		{
			Image img = this.panelPartie.getImageStation(station);
			if (img != null) g.drawImage(img, 2, 2, w - 4, h - 4, this);
		}

		// 4. Texte du départ (ex: "D1") dans la couleur du joueur
		int depart = this.ctrl.getDepart(this.index);
		if (depart > 0)
		{
			Color c = this.ctrl.getCouleurJoueur(depart);
			g.setColor(c);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString("D" + depart, 3, 16);
		}

		// 5. Surbrillance jaune si c'est un coup valide pour le joueur courant
		ArrayList<Integer> valides = this.ctrl.getCasesValides();
		for (int i = 0; i < valides.size(); i++)
		{
			if (valides.get(i) == this.index)
			{
				g.setColor(new Color(255, 255, 0, 130));
				g.fillRect(0, 0, w, h);
				break;
			}
		}
	}
}
