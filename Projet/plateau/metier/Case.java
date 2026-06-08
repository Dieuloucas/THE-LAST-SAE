package plateau.metier;

// Une case du plateau. Au lieu de 3 tableaux paralleles, chaque case est un objet
// qui retient ses 3 informations : son arrondissement, son metro, son depart.
public class Case
{
	private int arrondissement;   // 0 = aucun, 1..20
	private int metro;            // 0 = aucun, 1..6
	private int depart;           // 0 = aucun, 1..nbJoueurs

	public int  getArrondissement()           { return this.arrondissement; }
	public void setArrondissement(int valeur) { this.arrondissement = valeur; }

	public int  getMetro()           { return this.metro; }
	public void setMetro(int valeur) { this.metro = valeur; }

	public int  getDepart()           { return this.depart; }
	public void setDepart(int valeur) { this.depart = valeur; }
}
