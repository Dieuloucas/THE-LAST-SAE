package plateau.ihm;

import plateau.Controleur;
import javax.swing.JFrame;

public class FrameAccueil extends JFrame
{
	public FrameAccueil(Controleur ctrl)
	{
		this.setTitle("Accueil");
		this.add(new PanelAccueil(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
