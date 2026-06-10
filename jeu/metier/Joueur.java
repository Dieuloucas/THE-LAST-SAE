package jeu.metier;

// Représente un joueur : son numéro, sa station de départ, son réseau et ses scores.
// En jeu simultané, chaque joueur possède sa propre Pioche et sa propre carte courante.
public class Joueur
{
	private int          numero;        // 1..4
	private int          numCaseDepart; // index de sa case de départ sur le plateau
	private ReseauJoueur reseau;
	private int[]        scoresManches; // score obtenu à chaque manche (index 0 = manche 1)
	private int          scoreTotal;
	private Pioche       pioche;        // pioche personnelle du joueur pour la manche courante
	private Carte        carteCourante; // carte actuellement tirée

	public Joueur(int numero, int numCaseDepart, int nbManches)
	{
		this.numero        = numero;
		this.numCaseDepart = numCaseDepart;
		this.reseau        = new ReseauJoueur();
		this.scoresManches = new int[nbManches];
		this.scoreTotal    = 0;
		this.pioche        = null;
		this.carteCourante = null;

		// La station de départ fait partie du réseau dès le début
		this.reseau.ajouterStation(numCaseDepart);
	}

	// Reçoit une copie de la pioche modèle déjà mélangée (même ordre pour tous les joueurs)
	public void initialiserPioche(Pioche piocheModele)
	{
		this.pioche        = new Pioche(piocheModele);
		this.carteCourante = null;
	}

	// Tire la prochaine carte depuis la pioche personnelle
	public void piocherCarte()
	{
		if (this.pioche != null)
			this.carteCourante = this.pioche.piocher();
	}

	// La manche de ce joueur est terminée quand toutes ses foncées ont été tirées
	public boolean isMancheTerminee()
	{
		return this.pioche != null && this.pioche.estTerminee();
	}

	// Enregistre le score d'une manche et met à jour le total
	public void ajouterScore(int indiceManche, int score)
	{
		if (indiceManche >= 0 && indiceManche < this.scoresManches.length)
		{
			this.scoresManches[indiceManche] = score;
			this.scoreTotal += score;
		}
	}

	// Renvoie le score d'une manche précise (indiceManche commence à 0)
	public int getScoreManche(int indiceManche)
	{
		if (indiceManche >= 0 && indiceManche < this.scoresManches.length)
			return this.scoresManches[indiceManche];
		return 0;
	}

	public int          getNumero()        { return this.numero; }
	public int          getNumCaseDepart() { return this.numCaseDepart; }
	public ReseauJoueur getReseau()        { return this.reseau; }
	public int          getScoreTotal()    { return this.scoreTotal; }
	public Carte        getCarteCourante() { return this.carteCourante; }
	public Pioche       getPioche()        { return this.pioche; }
}
