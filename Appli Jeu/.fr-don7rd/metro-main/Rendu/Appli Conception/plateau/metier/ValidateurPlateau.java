package plateau.metier;

public class ValidateurPlateau
{
	public static boolean validerDeparts(Plateau plateau, int nbJoueurs)
	{
		int taille = plateau.getLargeur() * plateau.getHauteur();
		
		for (int p = 1; p <= nbJoueurs; p++)
		{
			int compteur = 0;	
			for (int i = 0; i < taille; i++)
			{
				if (plateau.getDepart(i) == p)
				{
					compteur++;
				}
			}
			if (compteur != 1)
			{
				System.out.println("Erreur : Le joueur " + p + " doit avoir exactement UNE base de départ sur le plateau. Actuellement : " + compteur + " placée(s).");
				return false;
			}
		}
		return true;
	}
}
