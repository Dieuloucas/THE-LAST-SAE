package plateau.ihm;

import javax.swing.JFrame;
import plateau.Controleur;

public class FrameAccueil extends JFrame
{
	public FrameAccueil(Controleur ctrl)
	{
		this.setTitle("Les Cartographes du Metro - Accueil");
		this.add(new PanelAccueil(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
