package plateau;

import plateau.ihm.FrameAccueil;
import plateau.metier.Plateau;

// Facade entre l'IHM et le metier : le controleur ne fait qu'appeler le metier.
public class Controleur
{
	private FrameAccueil ihm;
	private Plateau      metier;

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil(this);
	}

	// recree le plateau a la bonne taille (en gardant la config deja saisie)
	public void initialiserPlateau(int largeur, int hauteur)
	{
		int j = this.metier.getNbJoueurs();
		int m = this.metier.getNbMetro();
		this.metier = new Plateau(largeur, hauteur);
		this.metier.setConfig(j, m);
	}

	public void setConfigJeu(int nbJoueurs, int nbMetro) { this.metier.setConfig(nbJoueurs, nbMetro); }
	public int  getNbJoueurs() { return this.metier.getNbJoueurs(); }
	public int  getNbMetro()   { return this.metier.getNbMetro(); }

	public int getLargeur() { return this.metier.getLargeur(); }
	public int getHauteur() { return this.metier.getHauteur(); }
	public int getNbCases() { return this.metier.getNbCases(); }

	public int  getArrondissement(int numCase)               { return this.metier.getArrondissement(numCase); }
	public void affecterArrondissement(int numCase, int arr) { this.metier.affecterArrondissement(numCase, arr); }
	public int  getMetro(int numCase)                        { return this.metier.getMetro(numCase); }
	public void affecterMetro(int numCase, int metro)        { this.metier.affecterMetro(numCase, metro); }
	public int  getDepart(int numCase)                       { return this.metier.getDepart(numCase); }
	public void affecterDepart(int numCase, int depart)      { this.metier.affecterDepart(numCase, depart); }

	public boolean enregistrerPlateau(String nom)            { return this.metier.enregistrerPlateau(nom); }
	public boolean chargerPlateau(java.io.File file)         { return this.metier.chargerPlateau(file); }

	public static void main(String[] args)
	{
		new Controleur();
	}
}
