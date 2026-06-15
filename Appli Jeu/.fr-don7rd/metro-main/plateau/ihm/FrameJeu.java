package plateau.ihm;

import java.awt.BorderLayout;
import javax.swing.*;
import plateau.Controleur;

public class FrameJeu extends JFrame
{
	public FrameJeu(Controleur ctrl)
	{
		this.setTitle("Jeu - Paramètres et Aperçu");

		PanelJeu panelJeu = new PanelJeu(this, ctrl);
		this.add(panelJeu, BorderLayout.CENTER);

		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}
}
