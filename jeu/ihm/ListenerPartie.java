package jeu.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Gestionnaire d'événements centralisé pour les boutons du panneau de partie
public class ListenerPartie implements ActionListener
{
	private PanelPartie panel;

	public ListenerPartie(PanelPartie panel)
	{
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e)
	{
		// Bouton "Passer le tour"
		if (e.getSource() == this.panel.getPanelInfo().getBtnPasser())
		{
			this.panel.passerTour();
		}

		// Bouton "Quitter"
		else if (e.getSource() == this.panel.getPanelInfo().getBtnQuitter())
		{
			System.exit(0);
		}
	}
}
