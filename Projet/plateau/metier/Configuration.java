package plateau.metier;

// La configuration de la partie : nombre de joueurs et nombre de types de metro.
public class Configuration
{
	private int nbJoueurs;
	private int nbMetro;

	public void setConfig(int nbJoueurs, int nbMetro) { this.nbJoueurs = nbJoueurs; this.nbMetro = nbMetro; }
	public int  getNbJoueurs() { return this.nbJoueurs; }
	public int  getNbMetro()   { return this.nbMetro; }
}
