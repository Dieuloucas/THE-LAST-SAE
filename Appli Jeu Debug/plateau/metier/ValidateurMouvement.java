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

	// MODE DEBUG : explique en français POURQUOI un coup est accepté ou refusé.
	// Reprend exactement les mêmes tests que estValide(), dans le même ordre,
	// pour pouvoir montrer la logique de validation à l'oral.
	public static String raisonRefus(int numCase, Joueur joueur, Carte carte, Plateau plateau)
	{
		if (plateau.getStation(numCase) == 0)
			return "REFUSE : aucune station sur cette case";

		if (!carte.estJoker() && plateau.getStation(numCase) != carte.getTypeStation())
			return "REFUSE : station de type " + plateau.getStation(numCase) + " mais la carte demande le type " + carte.getTypeStation();

		if (joueur.getReseau().contient(numCase))
			return "REFUSE : cette station est deja dans le reseau du joueur";

		int premiere = joueur.getReseau().getPremiereStation();
		int derniere = joueur.getReseau().getDerniereStation();
		if (premiere == -1)
			return "REFUSE : le reseau du joueur est vide";

		if (!plateau.getGraphe().aArete(derniere, numCase) && !plateau.getGraphe().aArete(premiere, numCase))
			return "REFUSE : case non reliee a une extremite du reseau (extremites = " + premiere + " et " + derniere + ")";

		return "ACCEPTE";
	}
}
