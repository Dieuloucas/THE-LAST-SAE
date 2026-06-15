package plateau.metier;

import java.io.File;

// Données et chemins d'images partagés par le jeu (couleurs des arrondissements, fichiers des cartes et stations).
public class UtilitaireJeu
{
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

	// Chemin de l'image d'une carte : "{numero}_claire.png" ou "{numero}_fonce.png".
	// numero = type de station (1..6), ou 7 pour le joker. Renvoie null si introuvable.
	public static String getCheminImageCarte(int numero, boolean foncee)
	{
		String suffixe = foncee ? "_fonce" : "_claire";
		String[] chemins = {
			"plateau/images/"       + numero + suffixe + ".png",
			"images/"               + numero + suffixe + ".png",
			"../plateau/images/"    + numero + suffixe + ".png",
			"../../plateau/images/" + numero + suffixe + ".png"
		};
		for (int i = 0; i < chemins.length; i++)
		{
			File fichier = new File(chemins[i]);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}
		return null;
	}

	public static String getCheminImageStation(int stationNum)
	{
		String[] chemins = {
			"plateau/images/" 		+ stationNum + ".png",
			"images/" + stationNum 	+ ".png",
			"../plateau/images/" 	+ stationNum + ".png",
			"../../plateau/images/" + stationNum + ".png"
		};
		for (int i = 0; i < chemins.length; i++)
		{
			File fichier = new File(chemins[i]);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}
		return "plateau/images/" + stationNum + ".png";
	}
}
