package plateau.metier;

// La configuration d'une partie : nombre de joueurs et nombre de types de station.
public class Configuration
{
	public static final int MAX_JOUEURS        = 4;
	public static final int MAX_STATIONS       = 6;
	public static final int MAX_ARRONDISSEMENTS = 20;

	private int nbJoueurs;
	private int nbStations;

	public void setConfig(int nbJoueurs, int nbStations) { this.nbJoueurs = nbJoueurs; this.nbStations = nbStations; }
	public int  getNbJoueurs()  { return this.nbJoueurs; }
	public int  getNbStations() { return this.nbStations; }

	// Verifie les valeurs saisies a l'ecran de configuration.
	// Renvoie un message d'erreur, ou null si tout est correct.
	public static String verifier(int largeur, int hauteur, int nbJoueurs, int nbStations, int nbArrondissements)
	{
		if (largeur <= 0 || hauteur <= 0 || nbJoueurs <= 0 || nbStations <= 0 || nbArrondissements <= 0)
			return "Toutes les valeurs doivent etre positives.";
		if (nbJoueurs > MAX_JOUEURS)                 return MAX_JOUEURS + " joueurs maximum.";
		if (nbStations > MAX_STATIONS)               return MAX_STATIONS + " types de station maximum.";
		if (nbArrondissements > MAX_ARRONDISSEMENTS) return MAX_ARRONDISSEMENTS + " arrondissements maximum.";
		if (largeur * hauteur < nbJoueurs)           return "Le plateau est trop petit pour " + nbJoueurs + " joueurs.";
		return null;
	}
}
