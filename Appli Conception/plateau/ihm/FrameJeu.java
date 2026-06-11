package plateau.ihm;

import plateau.Controleur;

import java.awt.BorderLayout;
import javax.swing.*;

public class FrameJeu extends JFrame
{
	public FrameJeu(Controleur ctrl)
	{
		this.setTitle("Jeu - Paramètres et Aperçu");

		PanelJeu panelJeu = new PanelJeu(this, ctrl);
		this.add(panelJeu, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}
}
