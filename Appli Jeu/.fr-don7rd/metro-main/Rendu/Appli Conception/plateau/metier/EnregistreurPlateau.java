package plateau.metier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class EnregistreurPlateau
{
	public static boolean enregistrer(Plateau plateau, String nomFichier)
	{
		try
		{
			plateau.getGraphe().genererAretesAuto(plateau);

			File dossier = new File("sauvegarde");
			if (!dossier.exists()) dossier.mkdirs();

			// 1. On prépare la variable pour le nom du fichier
			String nomComplet = nomFichier;

			// 2. Si le nom ne finit pas par ".txt", on lui ajoute l'extension
			if (!nomFichier.endsWith(".txt")) 
			{
				nomComplet = nomFichier + ".txt";
			}

			// 3. On crée le fichier avec le bon nom
			File fichier = new File(dossier, nomComplet);
			PrintWriter pw = new PrintWriter(new FileOutputStream(fichier));

			pw.println(plateau.getLargeur() + ";" + plateau.getHauteur());

			int taille = plateau.getLargeur() * plateau.getHauteur();
			for (int i = 0; i < taille; i++)
			{
				pw.println(i + ";" + plateau.getArrondissement(i) + ";" + plateau.getStation(i) + ";" + plateau.getDepart(i));
			}

			for (int i = 0; i < taille; i++)
			{
				for (int j = i + 1; j < taille; j++)
				{
					if (plateau.getGraphe().aArete(i, j))
					{
						pw.println(i + ";" + j);
					}
				}
			}

			pw.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}
	}
}
