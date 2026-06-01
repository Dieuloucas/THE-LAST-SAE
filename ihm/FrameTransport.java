package ihm;

import Controleur.*;
import javax.swing.*;


public class FrameTransport extends JFrame
{
	private Controleur   ctrl;
	private PanelCartes  panelCartes;
	private PanelPlateau panelPlateau;
	private PanelEnnemie panelEnnemie;

	public FrameTransport(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setSize(700,500);
		this.setTitle("Réseau Express Parisien");
	}
}