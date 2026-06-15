package plateau.ihm;

import plateau.Controleur;

import java.awt.FlowLayout;
import javax.swing.*;

// Fenêtre de chargement : choix du plateau, du mode et du nombre de manches avant de lancer une partie.
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
