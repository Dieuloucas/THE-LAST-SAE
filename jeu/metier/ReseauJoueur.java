package jeu.metier;

import java.util.ArrayList;

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

	// Retourne la dernière station ajoutée au chemin (celle depuis laquelle on peut continuer)
	public int getDerniereStation()
	{
		if (this.stations.isEmpty()) return -1;
		return this.stations.get(this.stations.size() - 1);
	}
}
