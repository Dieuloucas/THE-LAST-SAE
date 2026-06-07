package plateau.ihm;

import plateau.Controleur;
import javax.swing.JFrame;

public class FrameConfiguration extends JFrame
{
	public FrameConfiguration(Controleur ctrl)
	{
		this.setTitle("Configuration du plateau");
		this.add(new PanelConfiguration(this, ctrl));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
