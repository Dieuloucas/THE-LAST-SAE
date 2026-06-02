package ihm;

import Controleur.Controleur;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class FrameTransport extends JFrame
{
	private Controleur ctrl;
	
	private PanelPlateau panelPlateau;
	// Décommenter plus tard
	// private PanelCartes panelCartes;
	// private PanelEnnemie panelEnnemie;

	public FrameTransport(Controleur ctrl)
	{
		this.ctrl = ctrl;


		this.setTitle("Réseau Express Parisien");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		// Instanciation et ajout du plateau au centre
		this.panelPlateau = new PanelPlateau(this.ctrl);
		this.add(this.panelPlateau, BorderLayout.CENTER);

		// futurs panels
		// this.panelCartes = new PanelCartes(this.ctrl);
		// this.add(this.panelCartes, BorderLayout.SOUTH);

		// this.panelEnnemie = new PanelEnnemie(this.ctrl);
		// this.add(this.panelEnnemie, BorderLayout.EAST);

		this.setVisible(true);
	}
}
