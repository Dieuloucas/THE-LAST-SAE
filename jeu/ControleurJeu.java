package jeu;

import jeu.ihm.FrameAccueilJeu;
import jeu.ihm.FrameJoueur;
import jeu.ihm.FrameResultats;
import jeu.metier.*;
import plateau.metier.*;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class ControleurJeu
{
	private FrameAccueilJeu ihm;
	private PartieJeu       partie;
	private FrameJoueur[]   framesJoueurs;
	private boolean         resultatsAffichees;

	// Couleurs des 4 joueurs (partagées entre IHM et contrôleur)
	public static final Color[] COULEURS_JOUEURS = {
		new Color(220,  50,  50),  // Joueur 1 rouge
		new Color( 50, 100, 220),  // Joueur 2 bleu
		new Color( 50, 180,  50),  // Joueur 3 vert
		new Color(220, 140,   0)   // Joueur 4 orange
	};

	public ControleurJeu()
	{
		this.partie             = null;
		this.framesJoueurs      = null;
		this.resultatsAffichees = false;
		this.ihm                = new FrameAccueilJeu(this);
	}

	public boolean chargerPlateau(File fichier)
	{
		Plateau p = ChargeurPlateau.charger(fichier);
		if (p != null) { this.partie = new PartieJeu(p); return true; }
		return false;
	}

	// Retourne true si le plateau chargé est jouable (a des joueurs et des stations)
	public boolean plateauEstJouable()
	{
		return this.partie != null
			&& this.partie.getNbManches()  > 0
			&& this.partie.getNbStations() > 0;
	}

	public void demarrerPartie()
	{
		if (this.partie != null)
		{
			this.resultatsAffichees = false;
			this.partie.demarrerPartie();
		}
	}

	// Joue un coup pour le joueur numéroté. Retourne true si le coup est valide.
	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		if (this.partie == null) return false;
		return this.partie.jouerCoup(numeroJoueur, numCase);
	}

	// Le joueur passe son tour (tire une nouvelle carte sans poser de station)
	public void passerTour(int numeroJoueur)
	{
		if (this.partie != null) this.partie.passerTour(numeroJoueur);
	}

	// --- Plateau ---
	public int getLargeur()             { return this.partie == null ? 0 : this.partie.getPlateau().getLargeur(); }
	public int getHauteur()             { return this.partie == null ? 0 : this.partie.getPlateau().getHauteur(); }
	public int getArrondissement(int i) { return this.partie == null ? 0 : this.partie.getPlateau().getArrondissement(i); }
	public int getStation(int i)        { return this.partie == null ? 0 : this.partie.getPlateau().getStation(i); }
	public int getDepart(int i)         { return this.partie == null ? 0 : this.partie.getPlateau().getDepart(i); }
	public boolean aArete(int i, int j) { return this.partie != null && this.partie.getPlateau().getGraphe().aArete(i, j); }

	// --- Partie ---
	public int      getNumeroManche()  { return this.partie == null ? 0 : this.partie.getNumeroManche(); }
	public int      getNbManches()     { return this.partie == null ? 0 : this.partie.getNbManches(); }
	public int      getNbJoueurs()     { return this.partie == null ? 0 : this.partie.getNbManches(); }
	public int      getNbStations()    { return this.partie == null ? 0 : this.partie.getNbStations(); }
	public Joueur[] getJoueurs()       { return this.partie == null ? new Joueur[0] : this.partie.getJoueurs(); }
	public boolean  isPartieTerminee() { return this.partie != null && this.partie.isPartieTerminee(); }

	// Carte courante d'un joueur particulier
	public Carte getCarteCourante(int numeroJoueur)
	{
		if (this.partie == null) return null;
		Joueur[] joueurs = this.partie.getJoueurs();
		if (numeroJoueur < 1 || numeroJoueur > joueurs.length) return null;
		return joueurs[numeroJoueur - 1].getCarteCourante();
	}

	// La manche de ce joueur est-elle terminée (toutes ses foncées tirées) ?
	public boolean isMancheJoueurTerminee(int numeroJoueur)
	{
		if (this.partie == null) return false;
		Joueur[] joueurs = this.partie.getJoueurs();
		if (numeroJoueur < 1 || numeroJoueur > joueurs.length) return false;
		return joueurs[numeroJoueur - 1].isMancheTerminee();
	}

	// Cases valides pour un joueur particulier (selon sa carte courante)
	public ArrayList<Integer> getCasesValides(int numeroJoueur)
	{
		if (this.partie == null) return new ArrayList<Integer>();
		return this.partie.getCasesValides(numeroJoueur);
	}

	public boolean estDansReseau(int numeroJoueur, int numCase)
	{
		if (this.partie == null) return false;
		Joueur[] joueurs = this.partie.getJoueurs();
		if (joueurs == null || numeroJoueur < 1 || numeroJoueur > joueurs.length) return false;
		return joueurs[numeroJoueur - 1].getReseau().contient(numCase);
	}

	// Pioche d'un joueur particulier
	public int getNbFonceesRestantes(int numeroJoueur)
	{
		if (this.partie == null) return 0;
		Joueur[] joueurs = this.partie.getJoueurs();
		if (numeroJoueur < 1 || numeroJoueur > joueurs.length) return 0;
		Pioche p = joueurs[numeroJoueur - 1].getPioche();
		return p == null ? 0 : p.getNbFonceesRestantes();
	}

	public int getNbCartesRestantes(int numeroJoueur)
	{
		if (this.partie == null) return 0;
		Joueur[] joueurs = this.partie.getJoueurs();
		if (numeroJoueur < 1 || numeroJoueur > joueurs.length) return 0;
		Pioche p = joueurs[numeroJoueur - 1].getPioche();
		return p == null ? 0 : p.getNbCartes();
	}

	public Color getCouleurJoueur(int numeroJoueur)
	{
		if (numeroJoueur >= 1 && numeroJoueur <= COULEURS_JOUEURS.length)
			return COULEURS_JOUEURS[numeroJoueur - 1];
		return Color.WHITE;
	}

	// --- Gestion des fenêtres joueurs (jeu simultané) ---

	// Enregistre toutes les FrameJoueur pour pouvoir les rafraîchir en bloc
	public void enregistrerFrames(FrameJoueur[] frames)
	{
		this.framesJoueurs      = frames;
		this.resultatsAffichees = false;
	}

	// Rafraîchit toutes les fenêtres et ouvre FrameResultats si la partie est finie
	public void rafraichirTout()
	{
		if (this.framesJoueurs == null) return;
		for (int i = 0; i < this.framesJoueurs.length; i++)
		{
			if (this.framesJoueurs[i] != null)
				this.framesJoueurs[i].rafraichir();
		}
		// Ouvrir les résultats une seule fois quand la partie est terminée
		if (isPartieTerminee() && !this.resultatsAffichees)
		{
			this.resultatsAffichees = true;
			for (int i = 0; i < this.framesJoueurs.length; i++)
			{
				if (this.framesJoueurs[i] != null)
					this.framesJoueurs[i].setVisible(false);
			}
			new FrameResultats(this);
		}
	}

	// --- Images des stations (cache partagé) ---
	private Image[] stationImages = new Image[11];

	public Image getImageStation(int stationNum)
	{
		if (stationNum < 1 || stationNum >= this.stationImages.length) return null;
		if (this.stationImages[stationNum] == null)
		{
			String chemin = UtilitaireJeu.getCheminImageStation(stationNum);
			if (chemin != null)
				this.stationImages[stationNum] = new ImageIcon(chemin).getImage();
		}
		return this.stationImages[stationNum];
	}

	// --- Fichiers sauvegardes ---
	public File[] getSauvegardes()
	{
		String[] chemins = {
			"sauvegarde",
			"../CONCEPTION/RENDU FINAL/plateau/sauvegarde",
			"../sauvegarde"
		};
		for (int c = 0; c < chemins.length; c++)
		{
			File dir = new File(chemins[c]);
			if (dir.exists() && dir.isDirectory())
			{
				File[] liste = dir.listFiles();
				if (liste != null)
				{
					ArrayList<File> txt = new ArrayList<File>();
					for (int i = 0; i < liste.length; i++)
					{
						if (liste[i].isFile() && liste[i].getName().endsWith(".txt"))
							txt.add(liste[i]);
					}
					if (!txt.isEmpty()) return txt.toArray(new File[0]);
				}
			}
		}
		return new File[0];
	}

	// --- Images de fond ---
	public String getImageFond()
	{
		String[] chemins = {
			"jeu/images/fond.png", "images/fond.png",
			"../CONCEPTION/RENDU FINAL/plateau/images/fond.png"
		};
		for (int i = 0; i < chemins.length; i++)
		{
			if (new File(chemins[i]).exists()) return new File(chemins[i]).getAbsolutePath();
		}
		return "jeu/images/fond.png";
	}

	public String getImageFond2()
	{
		String[] chemins = {
			"jeu/images/fond2.png", "images/fond2.png",
			"../CONCEPTION/RENDU FINAL/plateau/images/fond2.png"
		};
		for (int i = 0; i < chemins.length; i++)
		{
			if (new File(chemins[i]).exists()) return new File(chemins[i]).getAbsolutePath();
		}
		return "jeu/images/fond2.png";
	}

	public static void main(String[] args)
	{
		new ControleurJeu();
	}
}
