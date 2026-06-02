package Metier;

import java.util.ArrayList;
import java.util.List;

public class Plateau
{
	private int lignes;
	private int colonnes;
	private List<Sommet> sommets;
	private List<Arete> aretes;
	private List<Zone> zones;
	private List<Base> bases;
	private int prochainId = 0;

	public Plateau(int lignes, int colonnes)
	{
		this.lignes   = lignes;
		this.colonnes = colonnes;
		this.sommets  = new ArrayList<>();
		this.aretes   = new ArrayList<>();
		this.zones    = new ArrayList<>();
		this.bases    = new ArrayList<>();
	}

	public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
	{
		Sommet s = new Sommet(prochainId++, ligne, colonne, symbole);
		sommets.add(s);
		return s;
	}

	public void supprimerSommet(Sommet s)
	{
		sommets.remove(s);
		aretes.removeIf(a -> a.getS1() == s || a.getS2() == s);
		if (s.getZone() != null)
		{
			s.getZone().retirerSommet(s);
		}
		bases.removeIf(b -> b.getSommet() == s);
	}

	public Sommet getSommet(int ligne, int colonne)
	{
		for (Sommet s : sommets)
		{
			if (s.getLigne() == ligne && s.getColonne() == colonne)
			{
				return s;
			}
		}
		return null;
	}

	public List<Sommet> getSommets()
	{
		return sommets;
	}

	public void genererAretes()
	{
		aretes.clear();
		for (Sommet s : sommets)
		{
			for (Direction d : Direction.values())
			{
				Sommet voisin = getSommet(s.getLigne() + d.dl, s.getColonne() + d.dc);
				if (voisin != null)
				{
					Arete nouvelleArete = new Arete(s, voisin);
					if (!aretes.contains(nouvelleArete))
					{
						aretes.add(nouvelleArete);
					}
				}
			}
		}
	}

	public List<Arete> getAretes()
	{
		return aretes;
	}

	public List<Sommet> getVoisins(Sommet s)
	{
		List<Sommet> voisins = new ArrayList<>();
		for (Arete a : aretes)
		{
			if (a.getS1() == s) voisins.add(a.getS2());
			else if (a.getS2() == s) voisins.add(a.getS1());
		}
		return voisins;
	}

	public Zone ajouterZone()
	{
		Zone z = new Zone(zones.size());
		zones.add(z);
		return z;
	}

	public List<Zone> getZones()
	{
		return zones;
	}

	public Base ajouterBase(Couleur couleur, Sommet sommet)
	{
		Base b = new Base(couleur, sommet);
		bases.add(b);
		return b;
	}

	public List<Base> getBases(Couleur couleur)
	{
		List<Base> result = new ArrayList<>();
		for (Base b : bases)
		{
			if (b.getCouleur() == couleur)
			{
				result.add(b);
			}
		}
		return result;
	}

	public int getLignes() { return lignes; }
	public int getColonnes() { return colonnes; }

	private String cle(int ligne, int colonne)
	{
		return ligne + "," + colonne;
	}
}
