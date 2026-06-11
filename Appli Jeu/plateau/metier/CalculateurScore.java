package plateau.metier;

import java.util.ArrayList;

// Calcule le score d'un réseau (chemin) selon les règles du jeu :
// nombre de zones (arrondissements) traversées × nombre de stations
// présentes dans la zone la plus représentée du chemin.

public class CalculateurScore
{
	public static int calculer(ReseauJoueur reseau, Plateau plateau)
	{
		ArrayList<Integer> stations = reseau.getStations();

		// Trouver le plus grand numéro d'arrondissement présent dans le chemin

		int maxArr = 0;
		for (int i = 0; i < stations.size(); i++)
		{
			int arr = plateau.getArrondissement(stations.get(i));
			if (arr > maxArr) maxArr = arr;
		}
		if (maxArr == 0) return 0;

		// Compter le nombre de stations du chemin par arrondissement

		int[] compteParArr = new int[maxArr + 1]; // index 1..maxArr, index 0 ignoré
		for (int i = 0; i < stations.size(); i++)
		{
			int arr = plateau.getArrondissement(stations.get(i));
			if (arr >= 1) compteParArr[arr]++;
		}

		int nbZones     = 0; // nombre d'arrondissements différents traversés
		int maxStations = 0; // stations dans l'arrondissement le plus représenté

		for (int a = 1; a <= maxArr; a++)
		{
			if (compteParArr[a] > 0)           nbZones++;
			if (compteParArr[a] > maxStations) maxStations = compteParArr[a];
		}

		return nbZones * maxStations;
	}
}
