package plateau.ihm;

import plateau.Controleur;
import javax.swing.JFrame;

public class FrameJeu extends JFrame
{
	public FrameJeu(Controleur ctrl)
	{
		this.setTitle("Jeu - placement des metros et des departs");
		this.add(new PanelJeu(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
