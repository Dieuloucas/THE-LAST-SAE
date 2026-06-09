package jeu.ihm;

import jeu.ControleurJeu;
import javax.swing.*;

public class FrameResultats extends JFrame
{
	public FrameResultats(ControleurJeu ctrl)
	{
		this.setTitle("Fin de partie — Résultats");
		this.add(new PanelResultats(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
