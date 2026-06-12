package plateau.ihm;

import plateau.Controleur;

import java.awt.*;
import javax.swing.*;

// Fenêtre d'un joueur : uniquement le plateau (la grille).
// Toutes les infos de jeu sont regroupées dans la bande centrale (FrameInfos).
public class PanelJoueur extends JPanel
{
	private Controleur  ctrl;
	private int         numeroJoueur;
	private PanelGrille panelGrille;

	public PanelJoueur(FrameJoueur frame, Controleur ctrl, int numeroJoueur)
	{
		this.ctrl         = ctrl;
		this.numeroJoueur = numeroJoueur;

		this.setLayout(new BorderLayout());

		// Plateau ajouté directement (sans JScrollPane) : il s'adapte à la taille de la fenêtre,
		// on voit donc tout le plateau d'un coup.
		this.panelGrille = new PanelGrille(ctrl, this, numeroJoueur);
		this.add(this.panelGrille, BorderLayout.CENTER);

		this.panelGrille.construireGrille();
	}

	// Clic sur une case : seul le joueur dont c'est le tour peut poser, puis on rafraîchit TOUTES les fenêtres
	public void caseCliquee(int index)
	{
		if (!this.ctrl.estSonTour(this.numeroJoueur)) return;
		this.ctrl.jouerCoup(this.numeroJoueur, index);
		this.ctrl.rafraichirTout();
	}

	public void rafraichir()
	{
		this.panelGrille.repaint();
	}
}
