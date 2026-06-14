package plateau.metier;

// Un joueur : son numéro, son réseau (sa ligne), et ses scores par manche.
// La pioche et la carte sont communes, donc le joueur a juste un indicateur "a joué".
public class Joueur
{
	private ReseauJoueur reseau;
	private boolean      aJoue;

	private int[]        scoresManches; // score de chaque manche (case 0 = manche 1)
	private int          scoreTotal;

	public Joueur(int nbManches)
	{
		this.reseau        = new ReseauJoueur();
		this.aJoue         = false;
		this.scoresManches = new int[nbManches];
		this.scoreTotal    = 0;
	}

	// Démarre un nouveau réseau pour une manche, à partir de la case de départ donnée.
	public void commencerReseau(int caseDepart)
	{
		this.reseau.reinitialiser();
		this.reseau.ajouterStation(caseDepart);
		this.aJoue = false;
	}

	// Enregistre le score d'une manche et met à jour le total.
	public void ajouterScore(int indiceManche, int score)
	{
		if (indiceManche >= 0 && indiceManche < this.scoresManches.length)
		{
			this.scoresManches[indiceManche] = score;
			this.scoreTotal += score;
		}
	}

	public int getScoreManche(int indiceManche)
	{
		if (indiceManche >= 0 && indiceManche < this.scoresManches.length)
			return this.scoresManches[indiceManche];
		return 0;
	}

	// Meilleur score réalisé sur une seule manche (sert au départage).
	public int getMeilleurScoreManche()
	{
		int m = 0;
		for (int i = 0; i < this.scoresManches.length; i++)
			if (this.scoresManches[i] > m) m = this.scoresManches[i];
		return m;
	}

	public ReseauJoueur getReseau()         { return this.reseau; }
	public boolean      aJoue()             { return this.aJoue; }
	public void         setAJoue(boolean b) { this.aJoue = b; }
	public int          getScoreTotal()     { return this.scoreTotal; }
}
