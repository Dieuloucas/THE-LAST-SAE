package Metier;


public class Plateau
{
	private int id;
	private List<Sommet> stations;
	private int lignes;
	private int colonnes;
	private Sommet s;


	public Plateau(int lignes, int colonnes)
	{
		this.lignes		= lignes
		this.colonnes	= colonnes
	}
	
	public Zone(int id)
	{
		this.id = id;
		this.stations = new ArrayList<>();
	}

	public void ajouterStation(Sommet s)
	{
		this.stations.add(s);
	}
	
	public void retirerSommet(Sommet s)
	{
		// a faire
	}

	public getSommets()
	{
		return this.stations;
	}
}
