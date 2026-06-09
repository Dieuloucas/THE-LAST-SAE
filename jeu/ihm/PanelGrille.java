package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

// Panneau unique du plateau : fond image + cases transparentes + lignes réseau.
// Appartient à une FrameJoueur précise : surbrillance des coups valides pour CE joueur uniquement.
// Les clics souris sont capturés ici et transmis à PanelJoueur.
public class PanelGrille extends JPanel
{
	private ControleurJeu ctrl;
	private PanelJoueur   panelJoueur;
	private int           numeroJoueur;
	private Image         imgFond;

	public PanelGrille(ControleurJeu ctrl, PanelJoueur panelJoueur, int numeroJoueur)
	{
		this.ctrl         = ctrl;
		this.panelJoueur  = panelJoueur;
		this.numeroJoueur = numeroJoueur;
		this.imgFond      = new ImageIcon(ctrl.getImageFond()).getImage();

		// Capture des clics : convertit les coordonnées souris en indice de case
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int largeur = ctrl.getLargeur();
				int hauteur = ctrl.getHauteur();
				if (largeur <= 0 || hauteur <= 0) return;

				int cellW = getWidth()  / largeur;
				int cellH = getHeight() / hauteur;
				if (cellW <= 0 || cellH <= 0) return;

				int col = e.getX() / cellW;
				int row = e.getY() / cellH;

				// Garde-fous pour rester dans la grille
				if (col >= largeur) col = largeur - 1;
				if (row >= hauteur) row = hauteur - 1;

				panelJoueur.caseCliquee(row * largeur + col);
			}
		});
	}

	// Appelée depuis PanelJoueur pour (re)dimensionner le panneau selon le plateau chargé
	public void construireGrille()
	{
		int largeur    = this.ctrl.getLargeur();
		int hauteur    = this.ctrl.getHauteur();
		int cellTaille = 60;
		this.setPreferredSize(new Dimension(largeur * cellTaille, hauteur * cellTaille));
		this.revalidate();
		this.repaint();
	}

	// Tout le rendu est ici : fond, arrondissements, réseaux, stations, départs, surbrillances
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int w       = getWidth();
		int h       = getHeight();
		int largeur = this.ctrl.getLargeur();
		int hauteur = this.ctrl.getHauteur();
		if (largeur <= 0 || hauteur <= 0) return;

		int cellW = w / largeur;
		int cellH = h / hauteur;

		// --- 1. Image de fond (le plateau) ---
		if (this.imgFond != null)
			g.drawImage(this.imgFond, 0, 0, w, h, this);
		else
		{
			g.setColor(new Color(40, 40, 60));
			g.fillRect(0, 0, w, h);
		}

		Color[] tabCouleurs = plateau.metier.UtilitaireJeu.getCouleurs();
		int     taille      = largeur * hauteur;
		int     nbJoueurs   = this.ctrl.getNbJoueurs();

		// --- 2. Fond arrondissement (semi-transparent) ---
		for (int i = 0; i < taille; i++)
		{
			int col = i % largeur;
			int row = i / largeur;
			int x   = col * cellW;
			int y   = row * cellH;

			int arr = this.ctrl.getArrondissement(i);
			if (arr >= 1 && arr <= tabCouleurs.length)
			{
				Color c = tabCouleurs[arr - 1];
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 70));
				g.fillRect(x, y, cellW, cellH);
			}

			// Overlay coloré si la case est dans le réseau d'un joueur
			for (int j = 1; j <= nbJoueurs; j++)
			{
				if (this.ctrl.estDansReseau(j, i))
				{
					Color c = this.ctrl.getCouleurJoueur(j);
					// Notre propre réseau est plus opaque (plus visible)
					int alpha = (j == this.numeroJoueur) ? 130 : 75;
					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
					g.fillRect(x, y, cellW, cellH);
					break;
				}
			}
		}

		// --- 3. Lignes de réseau par joueur ---
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int j = 1; j <= nbJoueurs; j++)
		{
			// Notre réseau : trait plus épais
			float epaisseur = (j == this.numeroJoueur) ? 5f : 2.5f;
			g2d.setStroke(new BasicStroke(epaisseur));
			g2d.setColor(this.ctrl.getCouleurJoueur(j));
			for (int i = 0; i < taille; i++)
			{
				if (!this.ctrl.estDansReseau(j, i)) continue;
				for (int k = i + 1; k < taille; k++)
				{
					if (!this.ctrl.estDansReseau(j, k)) continue;
					if (!this.ctrl.aArete(i, k))        continue;

					int x1 = (i % largeur) * cellW + cellW / 2;
					int y1 = (i / largeur) * cellH + cellH / 2;
					int x2 = (k % largeur) * cellW + cellW / 2;
					int y2 = (k / largeur) * cellH + cellH / 2;
					g2d.drawLine(x1, y1, x2, y2);
				}
			}
		}

		// --- 4. Images stations, marqueurs départs, surbrillance coups valides ---
		// La surbrillance jaune concerne uniquement les coups valides de CE joueur
		ArrayList<Integer> valides = this.ctrl.getCasesValides(this.numeroJoueur);
		int fontSize = Math.max(9, Math.min(cellH / 3, 14));

		for (int i = 0; i < taille; i++)
		{
			int col = i % largeur;
			int row = i / largeur;
			int x   = col * cellW;
			int y   = row * cellH;

			// Image de station
			int station = this.ctrl.getStation(i);
			if (station > 0)
			{
				Image img = this.ctrl.getImageStation(station);
				if (img != null)
					g.drawImage(img, x + 3, y + 3, cellW - 6, cellH - 6, this);
			}

			// Texte du départ (ex: "D1") dans la couleur du joueur
			int depart = this.ctrl.getDepart(i);
			if (depart > 0)
			{
				Color c = this.ctrl.getCouleurJoueur(depart);
				g.setColor(c);
				g.setFont(new Font("Arial", Font.BOLD, fontSize));
				g.drawString("D" + depart, x + 3, y + fontSize + 2);
			}

			// Surbrillance jaune pour les coups valides de CE joueur
			boolean estValide = false;
			for (int v = 0; v < valides.size(); v++)
			{
				if (valides.get(v) == i) { estValide = true; break; }
			}
			if (estValide)
			{
				g.setColor(new Color(255, 255, 0, 120));
				g.fillRect(x, y, cellW, cellH);
			}
		}

		// --- 5. Grille de séparation (lignes subtiles) ---
		g.setColor(new Color(0, 0, 0, 50));
		for (int col = 0; col <= largeur; col++)
			g.drawLine(col * cellW, 0, col * cellW, h);
		for (int row = 0; row <= hauteur; row++)
			g.drawLine(0, row * cellH, w, row * cellH);
	}
}
