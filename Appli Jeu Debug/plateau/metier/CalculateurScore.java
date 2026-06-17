package plateau.metier;

import java.util.ArrayList;
// Calcule le score d'un réseau à la fin d'une manche.
// Score = (nombre de zones traversées) x (nombre de sommets le plus élevé dans une seule zone).
// Une zone = un arrondissement. La station de départ compte comme un sommet.
public class CalculateurScore
{
	public static int calculer(ReseauJoueur reseau, Plateau plateau)
	{
		// Compte le nombre de stations du tracé dans chaque arrondissement (1..20)
		int[] compteParZone = new int[21];

		ArrayList<Integer> stations = reseau.getStations(); //Integer car c'est un objet et arraylist peut que les objts
		
		for (int i = 0; i < stations.size(); i++)
		{
			int arr = plateau.getArrondissement(stations.get(i));
			if (arr >= 1 && arr <= 20)
				compteParZone[arr]++;
		}

		int nbZones        = 0; // nombre de zones (arrondissements) traversées
		int maxDansUneZone = 0; // plus grand nombre de sommets dans une seule zone

		for (int a = 1; a <= 20; a++)
		{
			if (compteParZone[a] > 0)              nbZones++;
			if (compteParZone[a] > maxDansUneZone) maxDansUneZone = compteParZone[a];
		}

		return nbZones * maxDansUneZone;
	}

	// MODE DEBUG : renvoie le détail du calcul (pour l'expliquer à l'oral).
	// Reprend le même calcul que calculer(), mais en listant chaque zone.
	public static String detailler(ReseauJoueur reseau, Plateau plateau)
	{
		int[] compteParZone = new int[21];

		ArrayList<Integer> stations = reseau.getStations();
		for (int i = 0; i < stations.size(); i++)
		{
			int arr = plateau.getArrondissement(stations.get(i));
			if (arr >= 1 && arr <= 20) compteParZone[arr]++;
		}

		String detail        = "";
		int    nbZones        = 0;
		int    maxDansUneZone = 0;
		for (int a = 1; a <= 20; a++)
		{
			if (compteParZone[a] > 0)
			{
				detail += "      zone " + a + " : " + compteParZone[a] + " station(s)\n";
				nbZones++;
				if (compteParZone[a] > maxDansUneZone) maxDansUneZone = compteParZone[a];
			}
		}
		detail += "      => " + nbZones + " zone(s) x " + maxDansUneZone + " (max) = " + (nbZones * maxDansUneZone) + " points";
		return detail;
	}
}
