package plateau.ihm;

import java.io.File;

// Petit utilitaire : retrouve le chemin d'une image quel que soit le dossier de lancement.
public class Images
{
	public static String chemin(String nom)
	{
		String[] bases = { "plateau/images/", "images/", "../plateau/images/", "../../plateau/images/" };
		for (String base : bases)
		{
			File f = new File(base + nom);
			if (f.exists()) return f.getAbsolutePath();
		}
		return "plateau/images/" + nom;
	}
}
