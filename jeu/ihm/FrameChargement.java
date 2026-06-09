package jeu.ihm;

import jeu.ControleurJeu;
import javax.swing.*;
import java.awt.FlowLayout;

public class FrameChargement extends JFrame
{
	public FrameChargement(ControleurJeu ctrl)
	{
		this.setTitle("Choisir un plateau");
		this.setLayout(new FlowLayout());
		this.add(new PanelChargement(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
