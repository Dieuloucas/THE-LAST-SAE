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

	// Ajoute une station à la FIN du tracé (prolonge depuis la dernière extrémité)
	public void ajouterStation(int numCase)
	{
		if (!this.contient(numCase))
			this.stations.add(numCase);
	}

	// Ajoute une station au DÉBUT du tracé (prolonge depuis la première extrémité)
	public void ajouterDebut(int numCase)
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

	// Vide le réseau (réinitialisation entre les manches)
	public void reinitialiser()
	{
		this.stations.clear();
	}

	// Première extrémité du tracé
	public int getPremiereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(0);
	}

	// Dernière extrémité du tracé
	public int getDerniereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(this.stations.size() - 1);
	}
}
