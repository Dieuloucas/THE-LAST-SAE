package jeu.ihm;

import jeu.ControleurJeu;
import javax.swing.*;

public class FramePartie extends JFrame
{
	public FramePartie(ControleurJeu ctrl)
	{
		this.setTitle("Les Cartographes du Métro — Partie");
		this.add(new PanelPartie(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
