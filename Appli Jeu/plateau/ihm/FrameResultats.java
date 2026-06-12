package plateau.ihm;

import plateau.Controleur;

import javax.swing.*;

// Écran final : scores de toutes les manches + total + gagnant(s).
public class FrameResultats extends JFrame
{
	public FrameResultats(Controleur ctrl)
	{
		this.setTitle("Résultats");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(new PanelResultats(ctrl));

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
