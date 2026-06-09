package jeu.metier;

import java.util.ArrayList;
import plateau.metier.Plateau;

// Gère une partie complète en jeu simultané.
// Chaque joueur a sa propre Pioche et joue indépendamment.
// La manche se termine quand TOUS les joueurs ont épuisé leurs cartes foncées.
public class PartieJeu
{
	private Plateau  plateau;
	private Joueur[] joueurs;
	private int      numeroManche;
	private int      nbManches;    // = nombre de joueurs = nombre de départs distincts
	private int      nbStations;
	private boolean  partieTerminee;

	public PartieJeu(Plateau plateau)
	{
		this.plateau        = plateau;
		this.numeroManche   = 0;
		this.partieTerminee = false;

		// Détecter nbJoueurs (max valeur depart) et nbStations (max valeur station)
		int taille     = plateau.getLargeur() * plateau.getHauteur();
		int maxDepart  = 0;
		int maxStation = 0;
		for (int i = 0; i < taille; i++)
		{
			if (plateau.getDepart(i)  > maxDepart)  maxDepart  = plateau.getDepart(i);
			if (plateau.getStation(i) > maxStation) maxStation = plateau.getStation(i);
		}
		this.nbManches  = maxDepart;
		this.nbStations = maxStation;

		// Créer chaque joueur en cherchant sa case de départ
		this.joueurs = new Joueur[this.nbManches];
		for (int n = 1; n <= this.nbManches; n++)
		{
			int caseDepart = 0;
			for (int i = 0; i < taille; i++)
			{
				if (plateau.getDepart(i) == n) { caseDepart = i; break; }
			}
			this.joueurs[n - 1] = new Joueur(n, caseDepart, this.nbManches);
		}
	}

	// Lance la partie : initialise la première manche pour tous les joueurs
	public void demarrerPartie()
	{
		this.numeroManche = 1;
		initialiserManche();
	}

	// Donne à chaque joueur sa propre pioche et tire sa première carte
	private void initialiserManche()
	{
		for (int i = 0; i < this.joueurs.length; i++)
		{
			this.joueurs[i].initialiserPioche(this.nbStations);
			this.joueurs[i].piocherCarte();
		}
	}

	// Un joueur place une station. Retourne true si le coup est valide.
	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length) return false;
		Joueur joueur = this.joueurs[numeroJoueur - 1];
		if (joueur.isMancheTerminee()) return false;
		Carte carte = joueur.getCarteCourante();
		if (carte == null) return false;
		if (!ValidateurMouvement.estValide(numCase, joueur, carte, this.plateau)) return false;
		joueur.getReseau().ajouterStation(numCase);
		joueur.piocherCarte();
		verifierFinManche();
		return true;
	}

	// Un joueur passe son tour : tire une nouvelle carte sans poser de station
	public void passerTour(int numeroJoueur)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length) return;
		Joueur joueur = this.joueurs[numeroJoueur - 1];
		if (joueur.isMancheTerminee()) return;
		joueur.piocherCarte();
		verifierFinManche();
	}

	// Vérifie si TOUS les joueurs ont épuisé leurs foncées → déclenche la fin de manche
	private void verifierFinManche()
	{
		for (int i = 0; i < this.joueurs.length; i++)
		{
			if (!this.joueurs[i].isMancheTerminee()) return;
		}
		finDeManche();
	}

	private void finDeManche()
	{
		// Calculer et enregistrer le score de chaque joueur
		for (int i = 0; i < this.joueurs.length; i++)
		{
			int score = CalculateurScore.calculer(this.joueurs[i].getReseau(), this.plateau);
			this.joueurs[i].ajouterScore(this.numeroManche - 1, score);
		}

		// Réinitialiser les réseaux pour la manche suivante
		for (int i = 0; i < this.joueurs.length; i++)
		{
			this.joueurs[i].getReseau().reinitialiser();
			this.joueurs[i].getReseau().ajouterStation(this.joueurs[i].getNumCaseDepart());
		}

		if (this.numeroManche < this.nbManches)
			prochaineManche();
		else
			this.partieTerminee = true;
	}

	private void prochaineManche()
	{
		this.numeroManche++;
		initialiserManche();
	}

	// Retourne les cases valides pour un joueur particulier selon sa carte courante
	public ArrayList<Integer> getCasesValides(int numeroJoueur)
	{
		if (numeroJoueur < 1 || numeroJoueur > this.joueurs.length)
			return new ArrayList<Integer>();
		Joueur joueur = this.joueurs[numeroJoueur - 1];
		Carte  carte  = joueur.getCarteCourante();
		if (carte == null || joueur.isMancheTerminee()) return new ArrayList<Integer>();
		return ValidateurMouvement.getCasesValides(joueur, carte, this.plateau);
	}

	// --- Getters ---
	public Plateau  getPlateau()       { return this.plateau; }
	public Joueur[] getJoueurs()       { return this.joueurs; }
	public int      getNumeroManche()  { return this.numeroManche; }
	public int      getNbManches()     { return this.nbManches; }
	public int      getNbStations()    { return this.nbStations; }
	public boolean  isPartieTerminee() { return this.partieTerminee; }
}
