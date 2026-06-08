package plateau.metier;

// Le plateau = une grille de cases (objets Case) + la configuration + le reseau d'aretes.
// Une case est reperee par un numero unique : numCase = ligne * largeur + colonne.
// Toute la logique du jeu vit ici (placements, validations, generation des aretes).
public class Plateau
{
	private Case[]        cases;
	private int           largeur;
	private int           hauteur;
	private Configuration config = new Configuration();
	private boolean[][]   aretes;     // matrice d'adjacence : aretes[i][j] = il y a un lien entre i et j

	public Plateau(int largeur, int hauteur)
	{
		this.largeur = largeur;
		this.hauteur = hauteur;
		int taille   = largeur * hauteur;
		this.cases   = new Case[taille];
		for (int i = 0; i < taille; i++) this.cases[i] = new Case();
		this.aretes  = new boolean[taille][taille];
	}

	// ── Dimensions ──
	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }
	public int getNbCases() { return this.cases.length; }

	// ── Configuration ──
	public void setConfig(int nbJoueurs, int nbStations) { this.config.setConfig(nbJoueurs, nbStations); }
	public int  getNbJoueurs()  { return this.config.getNbJoueurs(); }
	public int  getNbStations() { return this.config.getNbStations(); }

	// ── Numero de case <-> coordonnees ──
	public int getLigne(int numCase)              { return numCase / this.largeur; }
	public int getColonne(int numCase)            { return numCase % this.largeur; }
	public int getNumCase(int ligne, int colonne) { return ligne * this.largeur + colonne; }

	private boolean caseValide(int numCase) { return numCase >= 0 && numCase < this.cases.length; }
	public  Case    getCase(int numCase)    { return this.cases[numCase]; }

	// ── Acces a une case (delegue a l'objet Case) ──
	public int  getArrondissement(int numCase)               { return caseValide(numCase) ? this.cases[numCase].getArrondissement() : 0; }
	public void affecterArrondissement(int numCase, int arr) { if (caseValide(numCase)) this.cases[numCase].setArrondissement(arr); }

	public int  getStation(int numCase)               { return caseValide(numCase) ? this.cases[numCase].getStation() : 0; }
	public void affecterStation(int numCase, int station)
	{
		if (caseValide(numCase)) this.cases[numCase].setStation(station);
		genererAretes();                                  // les aretes dependent des stations
	}

	public int  getDepart(int numCase) { return caseValide(numCase) ? this.cases[numCase].getDepart() : 0; }
	public void affecterDepart(int numCase, int depart) { if (caseValide(numCase)) this.cases[numCase].setDepart(depart); }

	// Place le depart d'un joueur en respectant la regle "un seul depart par joueur".
	// joueur == 0 efface simplement le depart de cette case.
	public void placerDepart(int numCase, int joueur)
	{
		if (!caseValide(numCase)) return;
		if (joueur == 0) { this.cases[numCase].setDepart(0); return; }

		for (Case c : this.cases)                         // on enleve l'ancien depart de ce joueur
			if (c.getDepart() == joueur) c.setDepart(0);
		this.cases[numCase].setDepart(joueur);
	}

	// ── Validations (utilisees par l'IHM avant de sauvegarder) ──

	// toutes les cases ont-elles un arrondissement ?
	public boolean toutesCasesRemplies()
	{
		for (Case c : this.cases) if (c.estVide()) return false;
		return true;
	}

	public int compterDepart(int joueur)
	{
		int n = 0;
		for (Case c : this.cases) if (c.getDepart() == joueur) n++;
		return n;
	}

	// chaque joueur a-t-il exactement un depart ? (renvoie le numero du 1er joueur fautif, ou 0 si tout va bien)
	public int premierJoueurSansDepartUnique()
	{
		for (int joueur = 1; joueur <= getNbJoueurs(); joueur++)
			if (compterDepart(joueur) != 1) return joueur;
		return 0;
	}

	// recalcule la config a partir des cases (utile au chargement d'un vieux fichier sans config)
	public void recalculerConfig()
	{
		int maxStation = 0, maxJoueur = 0;
		for (Case c : this.cases)
		{
			if (c.getStation() > maxStation) maxStation = c.getStation();
			if (c.getDepart()  > maxJoueur)  maxJoueur  = c.getDepart();
		}
		if (maxStation > getNbStations() || maxJoueur > getNbJoueurs())
			this.config.setConfig(Math.max(maxJoueur, getNbJoueurs()), Math.max(maxStation, getNbStations()));
	}

	// ── Reseau d'aretes ──

	public boolean aArete(int i, int j)
	{
		return caseValide(i) && caseValide(j) && this.aretes[i][j];
	}

	// Relie chaque station a la station la plus proche dans 4 demi-directions
	// (Est, Sud-Est, Sud, Sud-Ouest) -> chaque arete n'est creee qu'une fois (pas de doublon).
	public void genererAretes()
	{
		int taille = getNbCases();
		this.aretes = new boolean[taille][taille];

		int[] dLigne = {0, 1, 1, 1};    // Est, Sud-Est, Sud, Sud-Ouest
		int[] dColonne = {1, 1, 0, -1};

		for (int i = 0; i < taille; i++)
		{
			if (this.cases[i].getStation() == 0) continue;   // pas de station ici -> rien a relier

			int ligne = getLigne(i), colonne = getColonne(i);
			for (int d = 0; d < 4; d++)
			{
				int l = ligne + dLigne[d], c = colonne + dColonne[d];
				while (l >= 0 && l < this.hauteur && c >= 0 && c < this.largeur)   // on avance jusqu'a sortir
				{
					int j = getNumCase(l, c);
					if (this.cases[j].getStation() > 0)        // 1re station trouvee dans cette direction
					{
						this.aretes[i][j] = true;
						this.aretes[j][i] = true;
						break;
					}
					l += dLigne[d];
					c += dColonne[d];
				}
			}
		}
	}
}
