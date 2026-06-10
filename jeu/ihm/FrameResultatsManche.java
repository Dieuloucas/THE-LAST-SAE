package jeu.ihm;

import jeu.ControleurJeu;
import javax.swing.*;

// Fenêtre de résultats affichée entre deux manches.
// Même DA que les autres écrans (fond image + overlay sombre).
public class FrameResultatsManche extends JFrame
{
	public FrameResultatsManche(ControleurJeu ctrl)
	{
		this.setTitle("Fin de manche " + ctrl.getNumeroManche() + " / " + ctrl.getNbManches());
		this.add(new PanelResultatsManche(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
