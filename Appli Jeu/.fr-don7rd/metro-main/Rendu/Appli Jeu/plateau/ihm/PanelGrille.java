package plateau.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import plateau.Controleur;

// Plateau d'UN joueur. Affichage simple et lisible :
// fond clair uni, stations, départs, et UNIQUEMENT le réseau de CE joueur (sa couleur).
// Les clics souris sont capturés ici et transmis à PanelJoueur.
public class PanelGrille extends JPanel implements MouseListener
{
	private Controleur  ctrl;
	private PanelJoueur panelJoueur;
	private int         numeroJoueur;

	public PanelGrille(Controleur ctrl, PanelJoueur panelJoueur, int numeroJoueur)
	{
		this.ctrl         = ctrl;
		this.panelJoueur  = panelJoueur;
		this.numeroJoueur = numeroJoueur;

		this.addMouseListener(this);
	}

	// Clic sur une case du plateau : on transmet l'indice de la case au PanelJoueur.
	public void mousePressed(MouseEvent e)
	{
		int largeur = this.ctrl.getLargeur();
		int hauteur = this.ctrl.getHauteur();
		if (largeur <= 0 || hauteur <= 0) return;

		int cellW = getWidth()  / largeur;
		int cellH = getHeight() / hauteur;
		if (cellW <= 0 || cellH <= 0) return;

		int col = e.getX() / cellW;
		int row = e.getY() / cellH;
		if (col >= largeur) col = largeur - 1;
		if (row >= hauteur) row = hauteur - 1;

		this.panelJoueur.caseCliquee(row * largeur + col);
	}

	// Méthodes de MouseListener non utilisées ici
	public void mouseClicked (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited  (MouseEvent e) {}

	// (Re)dimensionne le panneau selon le plateau chargé
	public void construireGrille()
	{
		int largeur    = this.ctrl.getLargeur();
		int hauteur    = this.ctrl.getHauteur();
		int cellTaille = 60;
		this.setPreferredSize(new Dimension(largeur * cellTaille, hauteur * cellTaille));
		this.revalidate();
		this.repaint();
	}

    @Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int w       = getWidth();
		int h       = getHeight();
		int largeur = this.ctrl.getLargeur();
		int hauteur = this.ctrl.getHauteur();
		if (largeur <= 0 || hauteur <= 0) return;

		int cellW  = w / largeur;
		int cellH  = h / hauteur;
		int taille = largeur * hauteur;

		// Fond clair uni sur toute la grille
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, w, h);

		// On colorie d'abord les zones d'arrondissement, en arrière-plan
		Color[] tabCouleurs = plateau.metier.UtilitaireJeu.getCouleurs();
		for (int i = 0; i < taille; i++)
		{
			int arr = this.ctrl.getArrondissement(i);
			if (arr >= 1 && arr <= tabCouleurs.length)
			{
				int x = (i % largeur) * cellW;
				int y = (i / largeur) * cellH;
				Color c = tabCouleurs[arr - 1];
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 110));
				g.fillRect(x, y, cellW, cellH);
			}
		}

		// Le tracé prend la couleur de ce joueur pour la manche en cours
		// (les couleurs tournent entre les joueurs d'une manche à l'autre)
		Color couleur = this.ctrl.getCouleurJoueur(this.numeroJoueur);

		// On relie les stations du réseau dans leur ordre de pose.
		// Le fond des cases n'est pas coloré : seule la ligne matérialise le réseau.
		g.setColor(couleur);

		ArrayList<Integer> chemin = this.ctrl.getCheminJoueur(this.numeroJoueur);
		for (int idx = 0; idx < chemin.size() - 1; idx++)
		{
			int s1 = chemin.get(idx);
			int s2 = chemin.get(idx + 1);
			int x1 = (s1 % largeur) * cellW + cellW / 2;
			int y1 = (s1 / largeur) * cellH + cellH / 2;
			int x2 = (s2 % largeur) * cellW + cellW / 2;
			int y2 = (s2 / largeur) * cellH + cellH / 2;
			g.drawLine(x1, y1, x2, y2);
		}

		// Stations du plateau, et mise en surbrillance des cases jouables ce tour
		ArrayList<Integer> valides = this.ctrl.getCasesValides(this.numeroJoueur);

		for (int i = 0; i < taille; i++)
		{
			int x = (i % largeur) * cellW;
			int y = (i / largeur) * cellH;

			int station = this.ctrl.getStation(i);
			if (station > 0)
			{
				Image img = this.ctrl.getImageStation(station);
				if (img != null)
					g.drawImage(img, x + 3, y + 3, cellW - 6, cellH - 6, this);
			}

			if (valides.contains(i))
			{
				g.setColor(new Color(255, 255, 0, 120));
				g.fillRect(x, y, cellW, cellH);
			}
		}

		// Enfin, on marque uniquement le départ de ce joueur, par un rond de sa couleur
		int caseDepart = this.ctrl.getCaseDepart(this.numeroJoueur);
		if (caseDepart >= 0)
		{
			int xd = (caseDepart % largeur) * cellW;
			int yd = (caseDepart / largeur) * cellH;
			g.setColor(couleur);
			g.drawOval(xd + 4, yd + 4, cellW - 8, cellH - 8);
		}
	}
}
