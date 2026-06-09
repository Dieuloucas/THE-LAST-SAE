package jeu.metier;

import plateau.metier.Plateau;

// Formule : nb arrondissements traversés × nb stations dans l'arrondissement le plus représenté
public class CalculateurScore
{
	public static int calculer(ReseauJoueur reseau, Plateau plateau)
	{
		int[] compteParArr = new int[21]; // index 1..20, index 0 ignoré

		for (int i = 0; i < reseau.getStations().size(); i++)
		{
			int numCase = reseau.getStations().get(i);
			int arr     = plateau.getArrondissement(numCase);
			if (arr >= 1 && arr <= 20)
				compteParArr[arr]++;
		}

		int nbArrondissements = 0;
		int maxStations       = 0;
		for (int a = 1; a <= 20; a++)
		{
			if (compteParArr[a] > 0)          nbArrondissements++;
			if (compteParArr[a] > maxStations) maxStations = compteParArr[a];
		}

		return nbArrondissements * maxStations;
	}
}
