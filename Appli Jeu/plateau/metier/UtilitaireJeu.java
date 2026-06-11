package plateau.metier;

import java.io.File;

public class UtilitaireJeu
{
	public static String[] getNomsStations()
	{
		return new String[]
		{
			"Tour Eiffel",
			"Moulin Rouge",
			"Louvre",
			"Restaurant",
			"Gare",
			"Aéroport"
		};
	}

	public static java.awt.Color[] getCouleurs()
	{
		return new java.awt.Color[]{
			new java.awt.Color(255, 105, 180),
			new java.awt.Color(138, 43, 226),
			new java.awt.Color(255, 165, 0),
			new java.awt.Color(0, 255, 255),
			new java.awt.Color(176, 196, 222),
			new java.awt.Color(255, 20, 147),
			new java.awt.Color(186, 85, 211),
			new java.awt.Color(148, 0, 211),
			new java.awt.Color(210, 105, 30),
			new java.awt.Color(244, 164, 96),
			new java.awt.Color(199, 21, 133),
			new java.awt.Color(123, 104, 238),
			new java.awt.Color(255, 140, 0),
			new java.awt.Color(0, 206, 209),
			new java.awt.Color(72, 61, 139),
			new java.awt.Color(220, 20, 60),
			new java.awt.Color(169, 169, 169),
			new java.awt.Color(245, 222, 179),
			new java.awt.Color(255, 228, 225),
			new java.awt.Color(255, 192, 203)
		};
	}

	// Chemin de l'image d'une station (ex: "plateau/images/1.png").
	public static String getCheminImageStation(int stationNum)
	{
		return "plateau/images/" + stationNum + ".png";
	}

	// Chemin de l'image d'une CARTE de la pioche.
	// Nom attendu : "<type>_clair.png" / "<type>_foncé.png" (joker : "joker_clair.png").
	// Renvoie null si l'image n'existe pas (l'affichage bascule alors sur un repli).
	public static String getCheminImageCarte(int typeStation, boolean foncee)
	{
		String base = (typeStation == Carte.JOKER) ? "joker" : Integer.toString(typeStation);
		File fichier = new File("plateau/images/" + base + (foncee ? "_foncé" : "_clair") + ".png");
		return fichier.exists() ? fichier.getAbsolutePath() : null;
	}
}
