package plateau.ihm;

import javax.swing.JFrame;
import plateau.Controleur;

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
