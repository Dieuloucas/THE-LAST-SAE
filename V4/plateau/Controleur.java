package plateau;

import java.io.File;

import plateau.ihm.FrameAccueil;
import plateau.metier.Configuration;
import plateau.metier.Plateau;
import plateau.metier.Sauvegarde;

// Facade entre l'IHM et le metier : le controleur ne fait QU'appeler le metier.
// Aucune regle de jeu ici (tout est dans le metier).
public class Controleur
{
	private Plateau plateau;

	public Controleur()
	{
		this.plateau = new Plateau(10, 10);
		new FrameAccueil(this);
	}

	// ── Configuration / creation ──
	public String verifierConfig(int largeur, int hauteur, int nbJoueurs, int nbStations, int nbArrondissements)
	{
		return Configuration.verifier(largeur, hauteur, nbJoueurs, nbStations, nbArrondissements);
	}

	public void creerPlateau(int largeur, int hauteur, int nbJoueurs, int nbStations)
	{
		this.plateau = new Plateau(largeur, hauteur);
		this.plateau.setConfig(nbJoueurs, nbStations);
	}

	// ── Dimensions / config ──
	public int getLargeur()    { return this.plateau.getLargeur(); }
	public int getHauteur()    { return this.plateau.getHauteur(); }
	public int getNbCases()    { return this.plateau.getNbCases(); }
	public int getNbJoueurs()  { return this.plateau.getNbJoueurs(); }
	public int getNbStations() { return this.plateau.getNbStations(); }

	// ── Cases ──
	public int  getArrondissement(int numCase)               { return this.plateau.getArrondissement(numCase); }
	public void affecterArrondissement(int numCase, int arr) { this.plateau.affecterArrondissement(numCase, arr); }
	public int  getStation(int numCase)                      { return this.plateau.getStation(numCase); }
	public void affecterStation(int numCase, int station)    { this.plateau.affecterStation(numCase, station); }
	public int  getDepart(int numCase)                       { return this.plateau.getDepart(numCase); }
	public void placerDepart(int numCase, int joueur)        { this.plateau.placerDepart(numCase, joueur); }

	// ── Aretes ──
	public boolean aArete(int i, int j) { return this.plateau.aArete(i, j); }

	// ── Validations (le metier decide, l'IHM affiche) ──
	public boolean toutesCasesRemplies()           { return this.plateau.toutesCasesRemplies(); }
	public int     premierJoueurSansDepartUnique() { return this.plateau.premierJoueurSansDepartUnique(); }

	// ── Persistance ──
	public String[] listerSauvegardes()     { return Sauvegarde.listerNoms(); }
	public boolean  enregistrer(String nom) { return Sauvegarde.enregistrer(this.plateau, nom); }
	public boolean  charger(String nomFichier)
	{
		Plateau p = Sauvegarde.charger(new File("sauvegarde", nomFichier));
		if (p == null) return false;
		this.plateau = p;
		return true;
	}

	public static void main(String[] args)
	{
		new Controleur();
	}
}
