package jeu.metier;

import java.util.ArrayList;
import plateau.metier.GraphePlateau;

public class ReseauJoueur
{
	private ArrayList<Integer> stations;

	public ReseauJoueur()
	{
		this.stations = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getStations() { return this.stations; }

	public void ajouterStation(int numCase)
	{
		if (!this.contient(numCase))
			this.stations.add(numCase);
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

	// Retourne les extrémités : stations qui ont 0 ou 1 voisin (du graphe) aussi dans le réseau
	public ArrayList<Integer> getExtremites(GraphePlateau graphe)
	{
		ArrayList<Integer> extremites = new ArrayList<Integer>();
		int taille = graphe.getTaille();

		for (int i = 0; i < this.stations.size(); i++)
		{
			int s = this.stations.get(i);
			int nbVoisinsDansReseau = 0;

			for (int j = 0; j < taille; j++)
			{
				if (graphe.aArete(s, j) && this.contient(j))
					nbVoisinsDansReseau++;
			}

			if (nbVoisinsDansReseau <= 1)
				extremites.add(s);
		}
		return extremites;
	}
}
