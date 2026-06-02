package Metier;

import java.util.ArrayList;
import java.util.List;

public class Zone
{
	private int id;
	private List<Sommet> sommets;

	public Zone(int id)
	{
		this.id = id;
		this.sommets = new ArrayList<>();
	}

	public int getId() { return id; }

	public void ajouterSommet(Sommet s)
	{
		if (!sommets.contains(s))
		{
			sommets.add(s);
			s.setZone(this);
		}
	}

	public void retirerSommet(Sommet s)
	{
		sommets.remove(s);
		if (s.getZone() == this)
		{
			s.setZone(null);
		}
	}

	public List<Sommet> getSommets()
	{
		return sommets;
	}

	public int getTaille()
	{
		return sommets.size();
	}
}
