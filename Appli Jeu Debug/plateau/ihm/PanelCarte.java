package plateau.ihm;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import plateau.Controleur;
import plateau.metier.Carte;

// Affiche la carte COMMUNE courante sous forme d'image (ex: "3_claire.png", "7_fonce.png" pour le joker).
public class PanelCarte extends JPanel
{
	private Controleur ctrl;

	public PanelCarte(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setPreferredSize(new Dimension(110, 150));
		this.setOpaque(false);
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Carte carte = this.ctrl.getCarteCourante();
		if (carte == null) return; // pioche épuisée : rien à afficher

		Image image = this.ctrl.getImageCarte(carte);
		if (image != null)
			g.drawImage(image, 5, 5, getWidth() - 10, getHeight() - 10, this);
	}
}
