package plateau.ihm;

import plateau.Controleur;

import javax.swing.*;

// Fenêtre de jeu d'UN joueur : ne contient que son plateau.
// Le positionnement à l'écran est géré par le Controleur (bande centrale + plateaux sur les côtés).
public class FrameJoueur extends JFrame
{
	private PanelJoueur panelJoueur;

	public FrameJoueur(Controleur ctrl, int numeroJoueur)
	{
		this.setTitle("Joueur " + numeroJoueur);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panelJoueur = new PanelJoueur(this, ctrl, numeroJoueur);
		this.add(this.panelJoueur);

		this.pack();
		this.setVisible(true);
	}

	// Délègue le rafraîchissement au panneau interne
	public void rafraichir()
	{
		this.panelJoueur.rafraichir();
	}
}
