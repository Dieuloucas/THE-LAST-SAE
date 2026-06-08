package plateau.metier;

// Une case du plateau. Chaque case retient ses 3 informations sous forme d'objet
// (au lieu de 3 tableaux paralleles) : son arrondissement, sa station, son depart.
public class Case
{
	private int arrondissement;   // 0 = aucun, 1..20
	private int station;          // 0 = aucune, 1..nbStations
	private int depart;           // 0 = aucun, 1..nbJoueurs

	public int  getArrondissement()           { return this.arrondissement; }
	public void setArrondissement(int valeur) { this.arrondissement = valeur; }

	public int  getStation()           { return this.station; }
	public void setStation(int valeur) { this.station = valeur; }

	public int  getDepart()           { return this.depart; }
	public void setDepart(int valeur) { this.depart = valeur; }

	// une case est "vide" si elle n'a pas encore d'arrondissement
	public boolean estVide() { return this.arrondissement == 0; }
}
