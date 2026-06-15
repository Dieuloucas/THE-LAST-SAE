package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

public class FrameConfiguration extends JFrame
{
	public FrameConfiguration(Controleur ctrl)
	{
		this.setTitle("Configuration Plateau");

		this.setLayout(new FlowLayout());

		this.add(new PanelConfiguration(this, ctrl));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}

}