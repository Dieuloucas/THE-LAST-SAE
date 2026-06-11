package plateau.metier;

import java.util.ArrayList;

// Partie en mode LOCAL.
// Pioche COMMUNE : une seule carte est révélée à la fois, la même pour tous les joueurs.
// Jeu SIMULTANÉ : chaque joueur pose (ou passe) la carte commune sur son propre réseau.
// Quand TOUS les joueurs ont joué la carte courante, on révèle la carte suivante.
// La partie s'arrête (version simple) quand la pioche commune est épuisée.
public class Partie
{
	private Plateau  plateau;
	private Joueur[] joueurs;

	private int      nbJoueurs;
	private int      nbStations;
	private int      joueurCourant; // numéro du joueur dont c'est le tour (1..nbJoueurs)

	private Pioche   pioche;        // pioche COMMUNE à tous les joueurs

	private Carte    carteCourante; // carte COMMUNE révélée ce tour
	
	private boolean  partieTerminee;

	public Partie(Plateau plateau)
	{
		this.plateau        = plateau;
		this.partieTerminee = false;

		// Détecter le nombre de joueurs (max départ) et de stations (max station)

		
		int maxDepart  = 0;
		int maxStation = 0;

		int taille     = plateau.getLargeur() * plateau.getHauteur();
		for (int i = 0; i < taille; i++)
		{
			if (plateau.getDepart(i)  > maxDepart)  maxDepart  = plateau.getDepart(i);
			if (plateau.getStation(i) > maxStation) maxStation = plateau.getStation(i);
		}
		this.nbJoueurs  = maxDepart;
		this.nbStations = maxStation;

		// Créer chaque joueur à partir de sa case de départ

		this.joueurs = new Joueur[this.nbJoueurs];

		for (int n = 1; n <= this.nbJoueurs; n++)
		{
			int caseDepart = 0;
			for (int i = 0; i < taille; i++)
			{
				if (plateau.getDepart(i) == n) { caseDepart = i; break; }
			}
			this.joueurs[n - 1] = new Joueur(n, caseDepart);
		}

		// Pioche COMMUNE mélangée, puis on révèle la première carte commune

		this.pioche = new Pioche(this.nbStations);

		this.pioche.melanger();

		this.carteCourante = this.pioche.piocher();

		// On commence par le joueur 1

		this.joueurCourant = 1;
	}

	// Un joueur pose une station (de la carte commune) sur son réseau.
	// Seul le joueur dont c'est le tour peut jouer. Retourne true si le coup est valide.

	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		if (this.partieTerminee) return false;

		if (numeroJoueur != this.joueurCourant) return false; // ce n'est pas son tour

		Joueur joueur = this.joueurs[numeroJoueur - 1];

		if (joueur.aJoue())             return false; // a déjà joué cette carte

		if (this.carteCourante == null) return false;

		if (!ValidateurMouvement.estValide(numCase, joueur, this.carteCourante, this.plateau)) return false;

		// Ajouter la station à la bonne extrémité pour garder l'ordre du chemin :
		// adjacente à la dernière station → on prolonge en fin ; sinon → en début.

		int derniere = joueur.getReseau().getDerniereStation();

		if (this.plateau.getGraphe().aArete(derniere, numCase))
			joueur.getReseau().ajouterStation(numCase);
		else
			joueur.getReseau().ajouterStationEnDebut(numCase);

		joueur.setAJoue(true);

		avancerJoueur();

		return true;
	}

	// Le joueur dont c'est le tour passe (sans poser de station).

	public void passerTour(int numeroJoueur)
	{
		if (this.partieTerminee) return;

		if (numeroJoueur != this.joueurCourant) return; // ce n'est pas son tour

		Joueur joueur = this.joueurs[numeroJoueur - 1];

		if (joueur.aJoue()) return;

		joueur.setAJoue(true);

		avancerJoueur();
	}

	// Passe au joueur suivant qui n'a pas encore joué.
	// Quand TOUS ont joué la carte commune, on révèle la carte suivante et on repart au joueur 1.

	private void avancerJoueur()
	{
		for (int n = 1; n <= this.joueurs.length; n++)
		{
			if (!this.joueurs[n - 1].aJoue()) { this.joueurCourant = n; return; }
		}

		// Tout le monde a joué -> nouvelle carte commune pour tout le monde

		for (int i = 0; i < this.joueurs.length; i++)
			this.joueurs[i].setAJoue(false);

		this.joueurCourant = 1;

		this.carteCourante = this.pioche.piocher();

		// La manche se termine quand toutes les cartes foncées ont été tirées
		// (ou par sécurité si la pioche est totalement vide)

		if (this.carteCourante == null || this.pioche.estTerminee())
		{
			this.partieTerminee = true;
			calculerScores();
		}
	}

	// Calcule et stocke le score final de chaque joueur à la fin de la partie.

	private void calculerScores()
	{
		for (int i = 0; i < this.joueurs.length; i++)
		{
			int score = CalculateurScore.calculer(this.joueurs[i].getReseau(), this.plateau);
			this.joueurs[i].setScore(score);
		}
	}

	// Est-ce au tour de ce joueur de jouer ?

	public boolean estSonTour(int numeroJoueur)
	{
		return !this.partieTerminee
			&& numeroJoueur == this.joueurCourant
			&& numeroJoueur >= 1 && numeroJoueur <= this.joueurs.length
			&& !this.joueurs[numeroJoueur - 1].aJoue();
	}

	// Cases jouables pour un joueur selon la carte commune courante.
	// On ne propose les coups que lorsque c'est son tour (surbrillance sur sa frame uniquement).
	public ArrayList<Integer> getCasesValides(int numeroJoueur)
	{
		if (!estSonTour(numeroJoueur) || this.carteCourante == null) return new ArrayList<Integer>();

		Joueur joueur = this.joueurs[numeroJoueur - 1];

		return ValidateurMouvement.getCasesValides(joueur, this.carteCourante, this.plateau);
	}

	public ArrayList<Integer> getCheminJoueur(int numeroJoueur)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length) return new ArrayList<Integer>();

		return this.joueurs[numeroJoueur - 1].getReseau().getStations();
	}

	public boolean aJoue(int numeroJoueur)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length) return false;

		return this.joueurs[numeroJoueur - 1].aJoue();
	}

	// --- Getters ---

	public Joueur[] getJoueurs()           { return this.joueurs;                        }
	public int      getNbJoueurs()         { return this.nbJoueurs;                      }
	public Carte    getCarteCourante()     { return this.carteCourante;                  }
	public int      getNbFonceesRestantes(){ return this.pioche.getNbFonceesRestantes(); }
	public boolean  isPartieTerminee()     { return this.partieTerminee; }
}
