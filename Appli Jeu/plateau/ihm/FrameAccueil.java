package plateau.ihm;

import plateau.Controleur;

import javax.swing.*;

// Fenêtre d'accueil : la première affichée au lancement, elle contient le menu principal.
public class FrameAccueil extends JFrame
{
	public FrameAccueil(Controleur ctrl)
	{
		this.setSize(724, 965);
		this.setTitle("Accueil Jeu");

		this.add(new PanelAccueil(this, ctrl));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}
}
