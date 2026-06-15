package plateau.ihm;

import plateau.Controleur;

import javax.swing.*;

// Bande verticale centrale : regroupe les infos de jeu et la pioche commune.
public class FrameInfos extends JFrame
{
	private PanelInfos panelInfos;

	public FrameInfos(Controleur ctrl)
	{
		this.setTitle("Infos de jeu");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panelInfos = new PanelInfos(ctrl);
		this.add(this.panelInfos);

		this.pack();
		this.setVisible(true);
	}

	public void rafraichir()
	{
		this.panelInfos.rafraichir();
	}
}
