package plateau.metier;

import java.util.ArrayList;

// Vérifie qu'un joueur peut bien poser la carte courante sur une case donnée.
public class ValidateurMouvement
{
	public static boolean estValide(int numCase, Joueur joueur, Carte carte, Plateau plateau)
	{
		// 1. Il faut qu'une station soit posée ici
		if (plateau.getStation(numCase) == 0) return false;

		// 2. Le type doit correspondre à la carte (sauf joker)
		if (!carte.estJoker() && plateau.getStation(numCase) != carte.getTypeStation()) return false;

		// 3. La case ne doit pas être déjà dans le réseau du joueur
		if (joueur.getReseau().contient(numCase)) return false;

		// 4. La case doit être voisine de l'UNE DES DEUX extrémités du tracé
		int premiere = joueur.getReseau().getPremiereStation();
		int derniere = joueur.getReseau().getDerniereStation();
		if (premiere == -1) return false;
		return plateau.getGraphe().aArete(derniere, numCase)
			|| plateau.getGraphe().aArete(premiere, numCase);
	}

	public static ArrayList<Integer> getCasesValides(Joueur joueur, Carte carte, Plateau plateau)
	{
		ArrayList<Integer> casesValides = new ArrayList<Integer>();
		int taille = plateau.getLargeur() * plateau.getHauteur();
		for (int i = 0; i < taille; i++)
		{
			if (estValide(i, joueur, carte, plateau))
				casesValides.add(i);
		}
		return casesValides;
	}
}
