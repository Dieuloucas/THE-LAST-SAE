package plateau.ihm;

import javax.swing.JFrame;
import plateau.Controleur;

public class FrameJeu extends JFrame
{
	public FrameJeu(Controleur ctrl)
	{
		this.setTitle("Plateau - placement des stations et des departs");
		this.add(new PanelJeu(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
