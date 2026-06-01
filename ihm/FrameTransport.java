package ihm;

import javax.swing.*;
import Controleur.*;


public class FrameTransport extends JFrame
{
	private Controleur   ctrl;
	private PanelCartes  panelCartes;
	private PanelPlateau panelPlateau;
	private PanelEnnemie panelEnnemie;

	public FrameTransport(Controleur ctrl)
	{
		this.ctrl = ctrl;
	}
}