package jeu.metier;

import java.util.ArrayList;
import plateau.metier.Plateau;

public class ValidateurMouvement
{
	// Vérifie si un coup est valide pour un joueur selon les règles du Donjon de Naheulbeuk :
	//  1. La case doit contenir une station
	//  2. Le type de station doit correspondre à la carte (sauf joker)
	//  3. La station ne doit pas déjà être dans le réseau du joueur
	//  4. La station doit être accessible depuis l'une des deux extrémités du chemin
	//     (le graphe garantit déjà qu'il n'y a pas de station intermédiaire sur la section)
	//  5. La nouvelle section ne doit pas en croiser une déjà tracée
	public static boolean estValide(int numCase, Joueur joueur, Carte carte, Plateau plateau)
	{
		// 1. Il faut qu'une station soit posée ici
		if (plateau.getStation(numCase) == 0) return false;

		// 2. Le type doit correspondre à la carte (sauf joker)
		if (!carte.estJoker() && plateau.getStation(numCase) != carte.getTypeStation()) return false;

		// 3. La case ne doit pas être déjà dans le réseau
		if (joueur.getReseau().contient(numCase)) return false;

		// 4 & 5. La case doit être accessible depuis une extrémité, sans croisement
		ArrayList<Integer> extremites = joueur.getReseau().getExtremites();
		for (int i = 0; i < extremites.size(); i++)
		{
			int ext = extremites.get(i);
			if (plateau.getGraphe().aArete(ext, numCase))
			{
				// Adjacente à cette extrémité : vérifier qu'on ne croise aucune section existante
				if (!croiseSectionExistante(ext, numCase, joueur.getReseau(), plateau.getLargeur()))
					return true; // Coup valide
			}
		}
		return false;
	}

	// Vérifie si la section (from → to) croise une section déjà tracée dans le réseau.
	// Une section est définie par deux stations consécutives dans le chemin.
	private static boolean croiseSectionExistante(int from, int to, ReseauJoueur reseau, int largeur)
	{
		ArrayList<Integer> chemin = reseau.getStations();
		int fromX = from % largeur; int fromY = from / largeur;
		int toX   = to   % largeur; int toY   = to   / largeur;

		for (int i = 0; i < chemin.size() - 1; i++)
		{
			int s1 = chemin.get(i);
			int s2 = chemin.get(i + 1);

			// Ignorer les sections qui ont "from" comme extrémité commune (pas un croisement)
			if (s1 == from || s2 == from) continue;

			int s1x = s1 % largeur; int s1y = s1 / largeur;
			int s2x = s2 % largeur; int s2y = s2 / largeur;

			if (sesCroisent(fromX, fromY, toX, toY, s1x, s1y, s2x, s2y))
				return true;
		}
		return false;
	}

	// Retourne true si les segments (ax,ay)→(bx,by) et (cx,cy)→(dx,dy) se croisent strictement.
	// Utilise le test CCW (sens trigonométrique) via produit vectoriel.
	// Un croisement "strict" exclut les cas où les segments se touchent seulement aux extrémités.
	private static boolean sesCroisent(int ax, int ay, int bx, int by,
	                                   int cx, int cy, int dx, int dy)
	{
		int d1 = orientation(cx, cy, dx, dy, ax, ay);
		int d2 = orientation(cx, cy, dx, dy, bx, by);
		int d3 = orientation(ax, ay, bx, by, cx, cy);
		int d4 = orientation(ax, ay, bx, by, dx, dy);

		// Croisement strict : A et B sont de part et d'autre de CD, et C et D de part et d'autre de AB
		if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
		    ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
			return true;

		return false;
	}

	// Retourne le signe du produit vectoriel (AB) × (AP).
	// Positif : P est à gauche de AB. Négatif : P est à droite. Zéro : colinéaire.
	private static int orientation(int ax, int ay, int bx, int by, int px, int py)
	{
		int v = (bx - ax) * (py - ay) - (by - ay) * (px - ax);
		if (v > 0) return  1;
		if (v < 0) return -1;
		return 0;
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
