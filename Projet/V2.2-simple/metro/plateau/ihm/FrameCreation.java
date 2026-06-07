package plateau.ihm;

import plateau.Controleur;
import javax.swing.JFrame;

public class FrameCreation extends JFrame
{
	public FrameCreation(Controleur ctrl, int nbArrondissements, int tailleCases)
	{
		this.setTitle("Creation du plateau");
		this.add(new PanelCreation(this, ctrl, nbArrondissements, tailleCases));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
