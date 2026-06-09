package plateau.metier;

public class GraphePlateau
{
	private boolean[][] matriceAretes;

	public GraphePlateau(int taille)
	{
		this.matriceAretes = new boolean[taille][taille];
	}

	public boolean aArete(int i, int j)
	{
		if (i >= 0 && i < matriceAretes.length && j >= 0 && j < matriceAretes.length)
		{
			return this.matriceAretes[i][j];
		}
		return false;
	}

	public void ajouterArete(int i, int j)
	{
		if (i >= 0 && i < matriceAretes.length && j >= 0 && j < matriceAretes.length)
		{
			this.matriceAretes[i][j] = true;
			this.matriceAretes[j][i] = true;
		}
	}

	public void vider()
	{
		for (int i = 0; i < matriceAretes.length; i++)
		{
			for (int j = 0; j < matriceAretes.length; j++)
			{
				this.matriceAretes[i][j] = false;
			}
		}
	}

	public boolean aDesAretes()
	{
		for (int i = 0; i < matriceAretes.length; i++)
		{
			for (int j = 0; j < matriceAretes[i].length; j++)
			{
				if (matriceAretes[i][j]) return true;
			}
		}
		return false;
	}

	public int getTaille() { return this.matriceAretes.length; }

	public int getNbAretes()
	{
		int compteur = 0;
		for (int i = 0; i < matriceAretes.length; i++)
		{
			for (int j = i + 1; j < matriceAretes.length; j++)
			{
				if (matriceAretes[i][j])
				{
					compteur++;
				}
			}
		}
		return compteur;
	}

	public void genererAretesAuto(Plateau plateau)
	{
		int taille = plateau.getLargeur() * plateau.getHauteur();
		this.vider();

		// Les 4 directions vers le bas / la droite (Est, Sud-Est, Sud, Sud-Ouest)
		int[] deplacementsX = {1, 1, 0, -1};
		int[] deplacementsY = {0, 1, 1,  1};

		for (int i = 0; i < taille; i++)
		{
			if (plateau.getStation(i) == 0) continue;

			int x = i % plateau.getLargeur();
			int y = i / plateau.getLargeur();

			for (int direction = 0; direction < 4; direction++)
			{
				int cx = x + deplacementsX[direction];
				int cy = y + deplacementsY[direction];

				while (cx >= 0 && cx < plateau.getLargeur() && cy >= 0 && cy < plateau.getHauteur())
				{
					int indexCible = cy * plateau.getLargeur() + cx;
					if (plateau.getStation(indexCible) > 0)
					{
						this.ajouterArete(i, indexCible);
						break; // S'arrêter au sommet le plus proche
					}
					cx += deplacementsX[direction];
					cy += deplacementsY[direction];
				}
			}
		}
	}
}
