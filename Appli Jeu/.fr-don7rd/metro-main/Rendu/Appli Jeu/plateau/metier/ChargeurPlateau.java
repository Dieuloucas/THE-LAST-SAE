package plateau.metier;

import java.io.File;
import java.util.Scanner;

public class ChargeurPlateau
{
	public static Plateau charger(File fichier)
	{
		// 1. Sécurité de base : le fichier existe-t-il et est-il lisible ?
		if (fichier == null || !fichier.exists() || !fichier.canRead())
		{
			return null;
		}

		try (Scanner scanner = new Scanner(fichier))
		{
			if (!scanner.hasNextLine()) return null;

			String premiereLigne = scanner.nextLine().trim();
			String[] dimensions = premiereLigne.split(";");

			// 2. L'en-tête doit contenir exactement 2 valeurs (largeur et hauteur)
			if (dimensions.length != 2) return null;

			int largeur = Integer.parseInt(dimensions[0]);
			int hauteur = Integer.parseInt(dimensions[1]);
			
			// 3. Les dimensions doivent être logiques (positives et pas gigantesques)
			if (largeur <= 0 || hauteur <= 0 || largeur > 100 || hauteur > 100)
			{
				return null;
			}
			
			Plateau plateau = new Plateau(largeur, hauteur);
			
			int taille 			= largeur * hauteur;
			int compteurCases 	= 0;
			
			boolean possedeAretes = false;

			while (scanner.hasNextLine())
			{
				String ligne = scanner.nextLine().trim();

				if (ligne.isEmpty()) continue;

				String[] elements = ligne.split(";");
				
				if (compteurCases < taille)
				{
					// 4. Une ligne de case doit avoir au moins un indice et une zone (2 éléments)
					if (elements.length < 2) return null;

					int index = Integer.parseInt(elements[0]);
					
					// 5. L'indice doit suivre un ordre strict de 0 à (taille - 1)
					if (index != compteurCases) return null;

					// On vérifie chaque valeur lue avant de l'enregistrer dans le plateau
					int arrondissement = Integer.parseInt(elements[1]);
					if (arrondissement < 0 || arrondissement > 20) return null;
					plateau.affecterArrondissement(index, arrondissement);
					
					if (elements.length >= 3)
					{
						int station = Integer.parseInt(elements[2]);
						if (station < 0 || station > 10) return null;
						plateau.affecterStation(index, station);
					}
					
					if (elements.length >= 4)
					{
						int depart = Integer.parseInt(elements[3]);
						if (depart < 0 || depart > 4) return null;
						plateau.affecterDepart(index, depart);
					}
					
					compteurCases++;
				}
				else
				{
					// 6. Une ligne d'arête doit absolument avoir une source et une destination
					if (elements.length < 2) return null;
					
					int source = Integer.parseInt(elements[0]);
					int destination = Integer.parseInt(elements[1]);
					
					// 7. Les points de l'arête doivent exister sur le plateau
					if (source >= 0 && source < taille && destination >= 0 && destination < taille)
					{
						plateau.getGraphe().ajouterArete(source, destination);
					}
					possedeAretes = true;
				}
			}

			// 8. Vérification finale : a-t-on lu EXACTEMENT le bon nombre de cases ?
			if (compteurCases != taille) return null;

			// Si le fichier n'avait pas d'arêtes pré-enregistrées
			if (!possedeAretes)
			{
				plateau.getGraphe().genererAretesAuto(plateau);
			}

			return plateau;
		}
		catch (NumberFormatException e)
		{
			// Est déclenché si quelqu'un a écrit du texte (ex: "A;B") au lieu de nombres
			System.out.println("Fichier corrompu : valeurs non numériques détectées.");
			return null;
		}
		catch (Exception e)
		{
			// Attrape toute autre erreur imprévue
			System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
			return null;
		}
	}
}
