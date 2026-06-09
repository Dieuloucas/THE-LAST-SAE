package jeu.ihm;

import jeu.ControleurJeu;
import jeu.metier.Carte;
import plateau.metier.UtilitaireJeu;

import java.awt.*;
import javax.swing.*;

// Panneau qui dessine visuellement la carte courante d'un joueur (style "carte à jouer").
// Le fond change selon le type (foncée = sombre, claire = clair).
// Tout est dessiné dans paintComponent pour un rendu soigné.
public class PanelCarte extends JPanel
{
	private ControleurJeu ctrl;
	private int           numeroJoueur;

	public PanelCarte(ControleurJeu ctrl, int numeroJoueur)
	{
		this.ctrl         = ctrl;
		this.numeroJoueur = numeroJoueur;
		this.setPreferredSize(new Dimension(110, 150));
		this.setOpaque(false);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();

		Carte carte = this.ctrl.getCarteCourante(this.numeroJoueur);

		// --- Carte non disponible : dos de carte ---
		if (carte == null)
		{
			g.setColor(new Color(60, 60, 90));
			g.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);
			g.setColor(new Color(100, 100, 140));
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);
			return;
		}

		// --- Fond de la carte ---
		if (carte.estFoncee())
			g.setColor(new Color(30, 30, 70));
		else
			g.setColor(new Color(240, 240, 255));
		g.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);

		// --- Bordure colorée ---
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(2));
		if (carte.estFoncee())
			g2d.setColor(new Color(100, 100, 200));
		else
			g2d.setColor(new Color(120, 120, 180));
		g2d.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);

		if (carte.estJoker())
		{
			// --- Joker : grande étoile dorée ---
			g2d.setFont(new Font("Arial", Font.BOLD, 38));
			g2d.setColor(Color.YELLOW);
			FontMetrics fm = g2d.getFontMetrics();
			String etoile  = "★";
			g2d.drawString(etoile, (w - fm.stringWidth(etoile)) / 2, h / 2 + 10);

			g2d.setFont(new Font("Arial", Font.BOLD, 14));
			g2d.setColor(carte.estFoncee() ? Color.WHITE : new Color(30, 30, 80));
			fm = g2d.getFontMetrics();
			String texte = "JOKER";
			g2d.drawString(texte, (w - fm.stringWidth(texte)) / 2, h - 28);
		}
		else
		{
			// --- Image de la station ---
			Image img = this.ctrl.getImageStation(carte.getTypeStation());
			if (img != null)
				g.drawImage(img, 12, 12, w - 24, h - 55, this);

			// --- Nom de la station ---
			String[] noms = UtilitaireJeu.getNomsStations();
			int      idx  = carte.getTypeStation() - 1;
			String   nom  = (idx >= 0 && idx < noms.length) ? noms[idx] : "Station " + carte.getTypeStation();
			g2d.setFont(new Font("Arial", Font.BOLD, 10));
			g2d.setColor(carte.estFoncee() ? Color.WHITE : new Color(30, 30, 80));
			FontMetrics fm = g2d.getFontMetrics();
			g2d.drawString(nom, Math.max(8, (w - fm.stringWidth(nom)) / 2), h - 28);
		}

		// --- Indicateur foncée / claire en bas ---
		String nature = carte.estFoncee() ? "● FONCÉE" : "○ claire";
		g2d.setFont(new Font("Arial", Font.BOLD, 9));
		g2d.setColor(carte.estFoncee() ? new Color(255, 120, 100) : new Color(60, 160, 60));
		g2d.drawString(nature, 10, h - 10);
	}
}
