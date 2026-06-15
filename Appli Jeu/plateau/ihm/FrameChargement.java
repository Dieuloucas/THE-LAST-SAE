package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import javax.swing.*;

public class FrameChargement extends JFrame
{
	public FrameChargement(Controleur ctrl)
	{
		this.setTitle("Choisir un plateau");

		this.setLayout(new FlowLayout());

		this.add(new PanelChargement(this, ctrl));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}
}
