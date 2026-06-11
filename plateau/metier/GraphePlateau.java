package plateau.metier;

/*
 * =====================================================================
 *  GraphePlateau
 * =====================================================================
 *  Représente les LIAISONS entre les cases du plateau sous forme de
 *  GRAPHE, à l'aide d'une MATRICE D'ADJACENCE.
 *
 *  matriceAretes est un tableau de booléens à 2 dimensions :
 *      matriceAretes[i][j] == true   ⇔   les cases i et j sont reliées.
 *
 *  Le graphe est NON ORIENTÉ : si i est relié à j, alors j est relié à i.
 *  C'est pour ça qu'on remplit toujours les deux cases en même temps
 *  ([i][j] ET [j][i]) — voir ajouterArete().
 * =====================================================================
 */
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

	/*
	 * Génère AUTOMATIQUEMENT toutes les arêtes du plateau.
	 *
	 * IDÉE : chaque station est reliée à la station la plus PROCHE dans
	 * chacune des 8 directions (comme une dame aux échecs : horizontales,
	 * verticales et diagonales), à condition qu'aucune autre station ne soit
	 * sur le chemin.
	 *
	 *   Grille des 8 directions autour d'une station S :
	 *
	 *        NO   N   NE
	 *          \  |  /
	 *        O -- S -- E
	 *          /  |  \
	 *        SO   S   SE
	 *
	 * ASTUCE IMPORTANTE — pourquoi seulement 4 directions et pas 8 ?
	 * Le graphe est non orienté : ajouterArete(a, b) relie a→b ET b→a en une
	 * seule fois. Donc si chaque station regarde uniquement vers la DROITE et
	 * vers le BAS (E, SE, S, SO), les liaisons vers la gauche et le haut sont
	 * créées "gratuitement" par les stations situées de ce côté.
	 *   Exemple : ma voisine de gauche regarde vers l'Est, me trouve, et la
	 *   liaison gauche↔moi est posée. Inutile que je regarde à gauche.
	 * On évite ainsi de traiter deux fois chaque paire.
	 */
	public void genererAretesAuto(Plateau plateau)
	{
		int largeur = plateau.getLargeur();
		int hauteur = plateau.getHauteur();
		int taille  = largeur * hauteur;

		this.vider(); // on repart d'un graphe sans aucune arête

		// Les 4 directions "vers la droite / vers le bas" :
		//   index 0 = Est        (dx=+1, dy= 0)
		//   index 1 = Sud-Est    (dx=+1, dy=+1)
		//   index 2 = Sud        (dx= 0, dy=+1)
		//   index 3 = Sud-Ouest  (dx=-1, dy=+1)
		
		int[] deplacementsX = {1, 1, 0, -1};
		int[] deplacementsY = {0, 1, 1,  1};

		// On parcourt toutes les cases du plateau.
		for (int i = 0; i < taille; i++)
		{
			// On ne s'intéresse qu'aux cases qui portent une station.
			if (plateau.getStation(i) == 0) continue;

			// Conversion de l'indice "à plat" i en coordonnées (colonne x, ligne y).
			int x = i % largeur;
			int y = i / largeur;

			// Pour chacune des 4 directions...
			for (int direction = 0; direction < 4; direction++)
			{
				// On part de la case juste à côté, dans cette direction.
				int cx = x + deplacementsX[direction];
				int cy = y + deplacementsY[direction];

				// ...puis on AVANCE case par case (comme un rayon) tant qu'on
				// reste à l'intérieur de la grille.
				while (cx >= 0 && cx < largeur && cy >= 0 && cy < hauteur)
				{
					int indexCible = cy * largeur + cx; // (x,y) -> indice à plat

					// Dès qu'on rencontre la PREMIÈRE station sur ce rayon :
					if (plateau.getStation(indexCible) > 0)
					{
						this.ajouterArete(i, indexCible); // on crée la liaison
						break;                              // et on arrête ce rayon
					}

					// Case vide : on continue d'avancer dans la même direction.
					cx += deplacementsX[direction];
					cy += deplacementsY[direction];
				}
			}
		}
	}
}
