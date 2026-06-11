package plateau.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import plateau.Controleur;
import plateau.metier.Carte;
import plateau.metier.UtilitaireJeu;

/*
 * =====================================================================
 *  PanelCarte
 * =====================================================================
 *  RÔLE : afficher la carte commune courante (celle qui vient d'être
 *  tirée de la pioche).
 *
 *  CHOIX D'AFFICHAGE — version "image".
 *  Chaque carte a sa propre image, nommée "<type>_clair.png" ou
 *  "<type>_foncé.png" (ex: 1_clair.png, 2_foncé.png ; joker_clair.png).
 *  On la met simplement dans un JLabel via une ImageIcon : c'est Swing
 *  qui l'affiche, aucun dessin manuel.
 *
 *  REPLI : si l'image de carte n'existe pas encore, on retombe sur
 *  l'ancien affichage (image de la station + texte), pour que l'appli
 *  fonctionne quand même.
 * =====================================================================
 */
public class PanelCarte extends JPanel
{
	private Controleur ctrl;

	// Les deux étiquettes (labels) qui composent la carte.
	private JLabel     lblImage; // affiche l'image de la carte
	private JLabel     lblTexte; // texte de repli (nom + foncée/claire)

	public PanelCarte(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setPreferredSize(new Dimension(110, 150));
		this.setOpaque(false);                 // fond transparent
		this.setLayout(new BorderLayout(0, 4)); // image au centre, texte en bas

		// Création des deux labels, centrés horizontalement.
		this.lblImage = new JLabel("", SwingConstants.CENTER);
		this.lblTexte = new JLabel("", SwingConstants.CENTER);
		this.lblTexte.setFont(new Font("Arial", Font.BOLD, 11));

		this.add(this.lblImage, BorderLayout.CENTER);
		this.add(this.lblTexte, BorderLayout.SOUTH);

		mettreAJour(); // affichage initial
	}

	/*
	 * Met à jour l'affichage selon la carte courante.
	 * À appeler chaque fois que la carte change (depuis PanelInfos.rafraichir).
	 */
	public void mettreAJour()
	{
		Carte carte = this.ctrl.getCarteCourante();

		// Pas de carte (pioche épuisée / partie pas commencée) : on vide tout.
		if (carte == null)
		{
			this.lblImage.setIcon(null);
			this.lblTexte.setText("—");
			return;
		}

		// On cherche l'image dédiée de la carte ("<type>_clair.png" / "<type>_foncé.png").
		String chemin = UtilitaireJeu.getCheminImageCarte(carte.getTypeStation(), carte.estFoncee());

		if (chemin != null)
		{
			// L'image existe : elle représente toute la carte (clair/foncé compris).
			Image img = new ImageIcon(chemin).getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
			this.lblImage.setIcon(new ImageIcon(img));
			this.lblTexte.setText(""); // l'image se suffit à elle-même
			return;
		}

		// --- REPLI : l'image de carte n'existe pas encore ---
		if (carte.estJoker())
		{
			this.lblImage.setIcon(null);
			this.lblTexte.setText(carte.estFoncee() ? "JOKER (foncée)" : "JOKER (claire)");
		}
		else
		{
			Image img = this.ctrl.getImageStation(carte.getTypeStation());
			if (img != null)
				this.lblImage.setIcon(new ImageIcon(img.getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
			else
				this.lblImage.setIcon(null);

			String[] noms = UtilitaireJeu.getNomsStations();
			int      idx  = carte.getTypeStation() - 1;
			String   nom  = (idx >= 0 && idx < noms.length) ? noms[idx] : "Station " + carte.getTypeStation();
			this.lblTexte.setText(nom + (carte.estFoncee() ? " (foncée)" : " (claire)"));
		}
	}
}
