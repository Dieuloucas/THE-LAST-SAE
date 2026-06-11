package plateau.metier;

import java.io.File;
import java.util.Scanner;

// Lit un plateau depuis un fichier texte.
// Format du fichier :
//   1ère ligne          : "largeur;hauteur"
//   puis 1 ligne / case : "index;arrondissement;station;depart"
//   puis (optionnel)     : 1 ligne / arête "source;destination"
// Si le fichier ne contient aucune arête, on les génère automatiquement.
public class ChargeurPlateau
{
	public static Plateau charger(File fichier)
	{
		if (fichier == null || !fichier.exists()) return null;

		Scanner scanner = null;
		try
		{
			scanner = new Scanner(fichier);

			// 1ère ligne : les dimensions du plateau
			String[] dimensions = scanner.nextLine().trim().split(";");
			int largeur = Integer.parseInt(dimensions[0]);
			int hauteur = Integer.parseInt(dimensions[1]);

			Plateau plateau = new Plateau(largeur, hauteur);
			int     taille  = largeur * hauteur;
			int     nbCases = 0;       // nombre de cases déjà lues
			boolean aretesLues = false; // le fichier contenait-il des arêtes ?

			while (scanner.hasNextLine())
			{
				String ligne = scanner.nextLine().trim();
				if (ligne.isEmpty()) continue;

				String[] valeurs = ligne.split(";");

				if (nbCases < taille)
				{
					// Ligne de case : index ; arrondissement ; [station] ; [départ]
					int index = Integer.parseInt(valeurs[0]);
					plateau.affecterArrondissement(index, Integer.parseInt(valeurs[1]));
					if (valeurs.length >= 3) plateau.affecterStation(index, Integer.parseInt(valeurs[2]));
					if (valeurs.length >= 4) plateau.affecterDepart (index, Integer.parseInt(valeurs[3]));
					nbCases++;
				}
				else
				{
					// Ligne d'arête : source ; destination
					plateau.getGraphe().ajouterArete(Integer.parseInt(valeurs[0]), Integer.parseInt(valeurs[1]));
					aretesLues = true;
				}
			}

			// Fichier incomplet : on n'a pas lu toutes les cases attendues
			if (nbCases != taille) return null;

			// Aucune arête dans le fichier : on les déduit automatiquement
			if (!aretesLues) plateau.getGraphe().genererAretesAuto(plateau);

			return plateau;
		}
		catch (Exception e)
		{
			// Fichier introuvable, mal formé, ou valeurs non numériques
			System.err.println("Impossible de charger le plateau : " + e.getMessage());
			return null;
		}
		finally
		{
			// On ferme toujours le Scanner, même en cas d'erreur
			if (scanner != null) scanner.close();
		}
	}
}
