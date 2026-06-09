package jeu.ihm;

import jeu.ControleurJeu;

import java.awt.*;
import javax.swing.*;

// Fenêtre de jeu pour UN joueur. Toutes les FrameJoueur sont ouvertes simultanément,
// chaque joueur joue dans sa propre fenêtre de façon indépendante.
public class FrameJoueur extends JFrame
{
	private PanelJoueur panelJoueur;

	public FrameJoueur(ControleurJeu ctrl, int numeroJoueur)
	{
		this.setTitle("Joueur " + numeroJoueur);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panelJoueur = new PanelJoueur(this, ctrl, numeroJoueur);
		this.add(this.panelJoueur);

		this.pack();
		positionnerFenetre(numeroJoueur);
		this.setVisible(true);
	}

	// Répartit les fenêtres en grille 2×2 pour éviter les superpositions
	private void positionnerFenetre(int numeroJoueur)
	{
		Dimension ecran = Toolkit.getDefaultToolkit().getScreenSize();
		int col  = (numeroJoueur - 1) % 2;
		int row  = (numeroJoueur - 1) / 2;
		int wFen = Math.min(this.getWidth(),  ecran.width  / 2 - 10);
		int hFen = Math.min(this.getHeight(), ecran.height / 2 - 40);
		int xPos = col * (ecran.width  / 2) + 5;
		int yPos = row * (ecran.height / 2) + 20;
		this.setSize(wFen, hFen);
		this.setLocation(xPos, yPos);
	}

	// Délégue le rafraîchissement complet au panneau interne
	public void rafraichir()
	{
		this.panelJoueur.rafraichir();
	}
}
