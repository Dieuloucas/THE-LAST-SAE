package jeu.metier;

import java.util.ArrayList;
import java.util.Random;

public class Pioche
{
	private ArrayList<Carte> cartes;
	private int              nbFonceesRestantes;

	// nbStations cartes foncées (type 1..N) + 1 joker foncé + même nombre de cartes claires
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

	// Constructeur de copie : reproduit exactement l'ordre d'une pioche modèle
	public Pioche(Pioche modele)
	{
		this.cartes             = new ArrayList<Carte>();
		this.nbFonceesRestantes = modele.nbFonceesRestantes;
		for (int i = 0; i < modele.cartes.size(); i++)
		{
			Carte c = modele.cartes.get(i);
			this.cartes.add(new Carte(c.getTypeStation(), c.estFoncee()));
		}
	}

	// Mélange aléatoire Fisher-Yates
	public void melanger()
	{
		Random rng = new Random();
		for (int i = this.cartes.size() - 1; i > 0; i--)
		{
			int j    = rng.nextInt(i + 1);
			Carte tmp = this.cartes.get(i);
			this.cartes.set(i, this.cartes.get(j));
			this.cartes.set(j, tmp);
		}
	}

	public Carte piocher()
	{
		if (this.cartes.isEmpty()) return null;
		Carte c = this.cartes.remove(0);
		if (c.estFoncee()) this.nbFonceesRestantes--;
		return c;
	}

	public boolean estTerminee()      { return this.nbFonceesRestantes <= 0; }
	public int getNbCartes()          { return this.cartes.size(); }
	public int getNbFonceesRestantes(){ return this.nbFonceesRestantes; }
}
