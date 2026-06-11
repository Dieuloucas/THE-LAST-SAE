package plateau.metier;

// Une carte de la pioche : indique quel type de station le joueur peut rejoindre
public class Carte
{
	public static final int JOKER = 0; // valeur spéciale : n'importe quel type

	private int     typeStation; // 1..nbStations, ou JOKER (0)
	private boolean estFoncee;   // true = carte foncée

	public Carte(int typeStation, boolean estFoncee)
	{
		this.typeStation = typeStation;
		this.estFoncee   = estFoncee;
	}

	public int     getTypeStation() { return this.typeStation; }
	
	public boolean estFoncee()      { return this.estFoncee; }
	public boolean estJoker()       { return this.typeStation == JOKER; }
}
