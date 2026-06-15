package plateau.metier;

public class Plateau
{
	private int		largeur;
	private int 	hauteur;
	
	private int[] 	tabArrondissements;
	private int[] 	tabStations;
	private int[] 	tabDeparts;
	
	private GraphePlateau graphe;

	public Plateau(int largeur, int hauteur)
	{
		this.largeur  = largeur;
		this.hauteur  = hauteur;

		int taille 	  = largeur * hauteur;

		this.tabArrondissements = new int[taille];
		this.tabStations 		= new int[taille];
		this.tabDeparts 		= new int[taille];
		
		this.graphe 			= new GraphePlateau(taille);
	}

	public int getLargeur() { return this.largeur; }
	
	public int getHauteur() { return this.hauteur; }
	
	public GraphePlateau getGraphe() { return this.graphe; }

	public int getArrondissement(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabArrondissements.length)
			return this.tabArrondissements[numCase];
		return 0;
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		if (numCase >= 0 && numCase < this.tabArrondissements.length)
			this.tabArrondissements[numCase] = arrondissement;
	}

	public void affecterStation(int numCase, int station)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			this.tabStations[numCase] = station;
	}

	public int getStation(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			return this.tabStations[numCase];
		return 0;
	}

	public void affecterDepart(int numCase, int depart)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			this.tabDeparts[numCase] = depart;
	}

	public int getDepart(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			return this.tabDeparts[numCase];
		return 0;
	}
}
