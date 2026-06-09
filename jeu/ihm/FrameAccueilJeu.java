package jeu.ihm;

import jeu.ControleurJeu;
import javax.swing.*;

public class FrameAccueilJeu extends JFrame
{
	public FrameAccueilJeu(ControleurJeu ctrl)
	{
		this.setTitle("Les Cartographes du Métro");
		this.setSize(724, 965);
		this.add(new PanelAccueilJeu(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
