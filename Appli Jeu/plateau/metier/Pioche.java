package plateau.metier;

import java.util.ArrayList;
import java.util.Random;

// Pioche COMMUNE : un seul tas de cartes partagé par tous les joueurs.
// Une seule carte est révélée à la fois, la même pour tout le monde.
public class Pioche
{
	private ArrayList<Carte> cartes;
	private int              nbFonceesRestantes;

	// nbStations cartes foncées (type 1..N) + 1 joker foncé + autant de cartes claires
	public Pioche(int nbStations)
	{
		this.cartes             = new ArrayList<Carte>();
		this.nbFonceesRestantes = nbStations + 1;

		for (int type = 1; type <= nbStations; type++)
			this.cartes.add(new Carte(type, true));

		this.cartes.add(new Carte(Carte.JOKER, true));

		for (int type = 1; type <= nbStations; type++)
			this.cartes.add(new Carte(type, false));

		this.cartes.add(new Carte(Carte.JOKER, false));
	}

	// Mélange aléatoire Fisher-Yates

	public void melanger()
	{
		Random rng = new Random();
		for (int i = this.cartes.size() - 1; i > 0; i--)
		{
			int j     = rng.nextInt(i + 1);
			Carte tmp = this.cartes.get(i);

			this.cartes.set(i, this.cartes.get(j));
			this.cartes.set(j, tmp);
		}
	}

	// Révèle la carte suivante (la retire du tas). Renvoie null si la pioche est vide.

	public Carte piocher()
	{
		if (this.cartes.isEmpty()) return null;

		Carte c = this.cartes.remove(0);

		if (c.estFoncee()) this.nbFonceesRestantes--;

		return c;
	}

	// La manche se termine quand toutes les cartes foncées ont été tirées
	
	public boolean estTerminee()           { return this.nbFonceesRestantes <= 0; }
	public int     getNbFonceesRestantes() { return this.nbFonceesRestantes; }
}
