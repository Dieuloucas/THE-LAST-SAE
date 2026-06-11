package plateau.ihm;

import javax.swing.*;
import plateau.Controleur;

// Fenêtre de fin de partie : affiche le score de chaque joueur et le vainqueur.
// Même DA que l'accueil (image de fond).

public class FrameResultats extends JFrame
{
	public FrameResultats(Controleur ctrl)
	{
		this.setTitle("Résultats de la partie");

		this.add(new PanelResultats(this, ctrl));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
