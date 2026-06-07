package plateau.ihm;

import plateau.Controleur;

import java.awt.BorderLayout;
import javax.swing.*;

public class FrameJeu extends JFrame
{
	public FrameJeu(Controleur ctrl)
	{
		this.setTitle("Jeu - Paramètres et Aperçu");

		PanelJeu panelJeu = new PanelJeu(this, ctrl);
		this.add(panelJeu, BorderLayout.CENTER);

		// Barre de menu avec JMenuItem pour importer la sauvegarde
		JMenuBar menuBar       = new JMenuBar ();
		JMenu menuSauvegarde   = new JMenu    ("Fichier"                );
		JMenuItem itemImporter = new JMenuItem("Importer une sauvegarde");
		
		itemImporter.addActionListener(panelJeu);
		
		menuSauvegarde.add(itemImporter);
		menuBar       .add(menuSauvegarde);

		this.setJMenuBar(menuBar);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null); // Centre la fenêtre
		this.setVisible(true);
	}
}
