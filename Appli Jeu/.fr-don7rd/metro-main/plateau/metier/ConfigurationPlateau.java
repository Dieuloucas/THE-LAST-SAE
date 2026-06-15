package plateau.metier;

public class ConfigurationPlateau
{
	public static int[] detecterMaxJoueursEtStations(Plateau plateau, int nbJoueursDefaut, int nbStationsDefaut)
	{
		int maxStations = nbStationsDefaut;
		int maxJoueurs  = nbJoueursDefaut;

		int taille      = plateau.getLargeur() * plateau.getHauteur();
		for (int i = 0; i < taille; i++)
		{
			maxStations = Math.max(maxStations, plateau.getStation(i));
			maxJoueurs  = Math.max(maxJoueurs , plateau.getDepart (i));
		}
		return new int[]{maxJoueurs, maxStations};
	}
}
