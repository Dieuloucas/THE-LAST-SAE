package plateau.metier;

import java.util.ArrayList;

// Réseau (ligne de métro) d'un joueur : la suite ordonnée des stations posées.
public class ReseauJoueur
{
	private ArrayList<Integer> stations;

	public ReseauJoueur()
	{
		this.stations = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getStations() { return this.stations; }

	// Ajoute une station en FIN de chemin (extension depuis la dernière extrémité)
	public void ajouterStation(int numCase)
	{
		if (!this.contient(numCase))
			this.stations.add(numCase);
	}

	// Ajoute une station en DÉBUT de chemin (extension depuis la première extrémité)
	
	public void ajouterStationEnDebut(int numCase)
	{
		if (!this.contient(numCase))
			this.stations.add(0, numCase);
	}

	public boolean contient(int numCase)
	{
		for (int i = 0; i < this.stations.size(); i++)
		{
			if (this.stations.get(i) == numCase) return true;
		}
		return false;
	}

	// Retourne la première station du chemin
	public int getPremiereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(0);
	}

	// Retourne la dernière station du chemin
	public int getDerniereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(this.stations.size() - 1);
	}

	// Retourne les deux extrémités du chemin (première et dernière station).
	// Selon les règles, le joueur peut prolonger sa ligne depuis l'un ou l'autre bout.
	public ArrayList<Integer> getExtremites()
	{
		ArrayList<Integer> extremites = new ArrayList<Integer>();

		if (this.stations.isEmpty()) return extremites;

		extremites.add(this.stations.get(0));

		if (this.stations.size() > 1)
			extremites.add(this.stations.get(this.stations.size() - 1));

		return extremites;
	}
}
