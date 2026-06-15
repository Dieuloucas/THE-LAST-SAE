package plateau.metier;

import java.io.File;

public class UtilitaireJeu
{
	public static String[] getNomsStations()
	{
		return new String[]{
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

	public static String getCheminImageStation(int stationNum)
	{
		String[] chemins = {
			"plateau/images/" 		+ stationNum + ".png",
			"images/"               + stationNum + ".png",
			"../plateau/images/" 	+ stationNum + ".png",
			"../../plateau/images/" + stationNum + ".png"
		};
		for (String chemin : chemins)
		{
			File fichier = new File(chemin);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}
		return "plateau/images/" + stationNum + ".png";
	}
}
