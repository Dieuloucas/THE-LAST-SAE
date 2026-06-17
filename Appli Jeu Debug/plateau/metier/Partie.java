package plateau.metier;

import java.util.ArrayList;

// plusieurs manches 
// Pioche COMMUNE : une seule carte est révélée à la fois, la même pour tous les joueurs.
// Ordre de tour : J1 puis J2... ; quand tous ont joué la carte commune, on révèle la suivante.
// Une manche se termine quand toutes les cartes FONCÉES ont été tirées : on calcule alors le score.
// À la manche suivante, le plateau est remis à vide (réseaux réinitialisés au départ).
public class Partie
{
	private Plateau  plateau;
	private Joueur[] joueurs;
	private int      nbJoueurs;
	private int      nbStations;
	private int[]    casesDepart;   // casesDepart[k] = case du départ numéro (k+1)

	private int      nbManches;     // nombre total de manches (choisi par l'utilisateur)
	private int      numeroManche;  // manche en cours (1..nbManches)

	private Pioche   pioche;        // pioche COMMUNE à tous les joueurs
	private Carte    carteCourante; // carte COMMUNE révélée ce tour
	private int      joueurCourant; // numéro du joueur dont c'est le tour (1..nbJoueurs)

	private boolean  entreManche;   // true quand une manche vient de finir (en attente de continuer)
	private boolean  partieTerminee;

	public Partie(Plateau plateau, int nbManches) 
	{
		this.plateau        = plateau;
		this.nbManches      = Math.max(1, nbManches);
		this.numeroManche   = 1;
		this.entreManche    = false;
		this.partieTerminee = false;

		// Détecter le nombre de joueurs (max départ) et de stations (max station)
		int taille     = plateau.getLargeur() * plateau.getHauteur();
		int maxDepart  = 0;
		int maxStation = 0;
		for (int i = 0; i < taille; i++)
		{
			if (plateau.getDepart(i)  > maxDepart)  maxDepart  = plateau.getDepart(i); // on défini le nombre de départ max et les stations max
			if (plateau.getStation(i) > maxStation) maxStation = plateau.getStation(i);
		}
		this.nbJoueurs  = maxDepart; // il ya autant de joueurs que de departs
		this.nbStations = maxStation;

		// Repérer la case de chaque départ (départ numéro n -> casesDepart[n-1])
		this.casesDepart = new int[this.nbJoueurs]; //tableau de n cases de depart 
		for (int n = 1; n <= this.nbJoueurs; n++)  // on parcours le nombre de joueurs 
		{
			for (int i = 0; i < taille; i++)
			{
				if (plateau.getDepart(i) == n) { this.casesDepart[n - 1] = i; break; } 
			}
		}

		// Créer les joueurs (leur réseau sera rempli au démarrage de la manche)
		this.joueurs = new Joueur[this.nbJoueurs];
		for (int n = 1; n <= this.nbJoueurs; n++)
			this.joueurs[n - 1] = new Joueur(this.nbManches);

		demarrerManche();
	}

	// Case de départ d'un joueur pour la manche en cours.
	// Les départs TOURNENT à chaque manche (comme les couleurs) : à la manche m,
	// le joueur p prend le départ "p + (m-1)" (modulo le nombre de joueurs).
	public int getCaseDepart(int numeroJoueur)
	{
		int k = ((numeroJoueur - 1) + (this.numeroManche - 1)) % this.nbJoueurs;
		return this.casesDepart[k];
	}

	// Prépare une manche : chaque joueur repart de son départ, pioche commune mélangée, joueur 1.
	private void demarrerManche()
	{
		for (int n = 1; n <= this.nbJoueurs; n++)
			this.joueurs[n - 1].commencerReseau(getCaseDepart(n));

		this.pioche        = new Pioche(this.nbStations);
		this.pioche.melanger();
		this.carteCourante = this.pioche.piocher();
		this.joueurCourant = 1;
	}

	// Un joueur pose une station (de la carte commune) sur son réseau.
	// Seul le joueur dont c'est le tour peut jouer. Retourne true si le coup est valide.
	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		if (this.partieTerminee || this.entreManche) return false;
		if (numeroJoueur != this.joueurCourant) return false;

		Joueur joueur = this.joueurs[numeroJoueur - 1];
		if (joueur.aJoue())             return false;
		if (this.carteCourante == null) return false;
		if (!ValidateurMouvement.estValide(numCase, joueur, this.carteCourante, this.plateau)) return false;

		// On rattache la nouvelle station à la bonne extrémité du tracé
		int derniere = joueur.getReseau().getDerniereStation();
		if (this.plateau.getGraphe().aArete(derniere, numCase))
			joueur.getReseau().ajouterStation(numCase); // prolonge par la fin
		else
			joueur.getReseau().ajouterDebut(numCase);    // prolonge par le début

		joueur.setAJoue(true);
		avancerJoueur();
		return true;
	}

	// Le joueur dont c'est le tour passe (sans poser de station).
	public void passerTour(int numeroJoueur)
	{
		if (this.partieTerminee || this.entreManche) return;
		if (numeroJoueur != this.joueurCourant) return;

		Joueur joueur = this.joueurs[numeroJoueur - 1];
		if (joueur.aJoue()) return;
		joueur.setAJoue(true);
		avancerJoueur();
	}

	// Passe au joueur suivant qui n'a pas encore joué.
	// Quand TOUS ont joué la carte commune, on révèle la suivante (et fin de manche si plus de foncées).
	private void avancerJoueur()
	{
		for (int n = 1; n <= this.joueurs.length; n++)
		{
			if (!this.joueurs[n - 1].aJoue()) { this.joueurCourant = n; return; }
		}

		// Tout le monde a joué la carte commune courante.
		// Si toutes les foncées ont déjà été tirées, c'est que la DERNIÈRE foncée vient d'être
		// JOUÉE : la manche se termine maintenant (on a donc bien pu jouer la dernière foncée).
		if (this.carteCourante == null || this.pioche.estTerminee())
		{
			finManche();
			return;
		}

		// Sinon, on révèle la carte commune suivante (elle sera jouée, même si c'est la dernière foncée).
		for (int i = 0; i < this.joueurs.length; i++)
			this.joueurs[i].setAJoue(false);
		this.joueurCourant = 1;
		this.carteCourante = this.pioche.piocher();
	}

	// Calcule le score de la manche pour chaque joueur, puis enchaîne ou termine la partie.
	private void finManche()
	{
		for (int i = 0; i < this.joueurs.length; i++)
		{
			int score = CalculateurScore.calculer(this.joueurs[i].getReseau(), this.plateau);
			this.joueurs[i].ajouterScore(this.numeroManche - 1, score);
		}

		if (this.numeroManche < this.nbManches)
			this.entreManche = true;   // pause : en attente du passage à la manche suivante
		else
			this.partieTerminee = true;
	}

	// Démarre la manche suivante (plateau remis à vide, départs qui tournent).
	public void continuerManche()
	{
		if (!this.entreManche) return;
		this.entreManche = false;
		this.numeroManche++;
		demarrerManche();
	}

	// Est-ce au tour de ce joueur de jouer ?
	public boolean estSonTour(int numeroJoueur)
	{
		return !this.partieTerminee
			&& !this.entreManche
			&& numeroJoueur == this.joueurCourant
			&& numeroJoueur >= 1 && numeroJoueur <= this.joueurs.length
			&& !this.joueurs[numeroJoueur - 1].aJoue();
	}

	// Cases jouables pour un joueur selon la carte commune courante (seulement à son tour).
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

	// ===== MODE DEBUG (démonstration à l'oral) =====

	// Remplace la carte commune courante par une carte choisie (au lieu de la pioche aléatoire).
	public void forcerCarteDebug(int typeStation, boolean foncee)
	{
		this.carteCourante = new Carte(typeStation, foncee);
	}

	// Termine immédiatement la manche en cours (calcule les scores, comme si la pioche était épuisée).
	public void sauterMancheDebug()
	{
		if (this.partieTerminee || this.entreManche) return;
		finManche();
	}

	// Nombre de types de stations (sert au sélecteur de carte du mode debug).
	public int getNbStations() { return this.nbStations; }

	public boolean estDansReseau(int numeroJoueur, int numCase)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length) return false;
		return this.joueurs[numeroJoueur - 1].getReseau().contient(numCase);
	}

	// Getters
	public Plateau  getPlateau()            { return this.plateau; }
	public Joueur[] getJoueurs()            { return this.joueurs; }
	public int      getNbJoueurs()          { return this.nbJoueurs; }
	public int      getNbManches()          { return this.nbManches; }
	public int      getNumeroManche()       { return this.numeroManche; }
	public Carte    getCarteCourante()      { return this.carteCourante; }
	public boolean  isEntreManche()         { return this.entreManche; }
	public boolean  isPartieTerminee()      { return this.partieTerminee; }
	public int      getJoueurCourant()      { return this.joueurCourant; }
	public int      getNbCartesRestantes()  { return this.pioche.getNbFonceesRestantes(); }
	public int      getNbFonceesRestantes() { return this.pioche.getNbFonceesRestantes(); }
}
