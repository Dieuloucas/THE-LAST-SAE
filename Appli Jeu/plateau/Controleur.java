package plateau;

import plateau.ihm.FrameAccueil;
import plateau.ihm.FrameInfos;
import plateau.ihm.FrameJoueur;
import plateau.ihm.FrameResultats;
import plateau.metier.*;

import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

// Contrôleur (façade) : seul point de passage entre l'IHM et le métier.
public class Controleur
{
	private FrameAccueil  ihm;
	private Plateau       metier;

	private int           nbJoueurs;
	private int           nbStations;
	private String        mode;        // "LOCAL" ou "MULTIJOUEUR"

	private Partie        partie;
	private FrameJoueur[] framesJoueurs;
	private FrameInfos    frameInfos;        // bande verticale centrale
	private boolean       resultatsAffiches;

	// Couleurs des 4 joueurs (partagées entre IHM et contrôleur)
	public static final Color[] COULEURS_JOUEURS =
	{
		new Color(220,  50,  50),  // Joueur 1 rouge
		new Color( 50, 100, 220),  // Joueur 2 bleu
		new Color( 50, 180,  50),  // Joueur 3 vert
		new Color(220, 140,   0)   // Joueur 4 orange
	};

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil(this);
	}

	public void   setMode(String mode) { this.mode = mode; }
	public String getMode()            { return this.mode; }

	/*-------------------------------------*/
	/* Chargement d'un plateau             */
	/*-------------------------------------*/

	public boolean chargerPlateau(File fichier)
	{
		Plateau p = ChargeurPlateau.charger(fichier);
		if (p != null) { this.metier = p; return true; }
		return false;
	}

	// Renvoie la liste des plateaux (.txt) trouvés dans plateau/sauvegarde.
	public File[] getSauvegardes()
	{
		File[] liste = new File("../plateau/sauvegarde").listFiles();
		if (liste == null) return new File[0];

		ArrayList<File> txt = new ArrayList<File>();
		for (int i = 0; i < liste.length; i++)
		{
			if (liste[i].isFile() && liste[i].getName().endsWith(".txt"))
				txt.add(liste[i]);
		}
		return txt.toArray(new File[0]);
	}

	// Vérifie que le plateau chargé est jouable (au moins un départ et une station).
	// nbJoueurs = plus grand numéro de départ, nbStations = plus grand numéro de station.
	public boolean plateauEstJouable()
	{
		int maxJoueurs  = 0;
		int maxStations = 0;
		int taille = this.metier.getLargeur() * this.metier.getHauteur();
		for (int i = 0; i < taille; i++)
		{
			if (this.metier.getDepart(i)  > maxJoueurs)  maxJoueurs  = this.metier.getDepart(i);
			if (this.metier.getStation(i) > maxStations) maxStations = this.metier.getStation(i);
		}
		this.nbJoueurs  = maxJoueurs;
		this.nbStations = maxStations;
		return this.nbJoueurs > 0 && this.nbStations > 0;
	}

	/*-------------------------------------*/
	/* Partie en LOCAL                     */
	/*-------------------------------------*/

	// Démarre la partie : crée la partie, la bande d'infos centrale et une fenêtre par joueur.
	public void lancerPartieLocale()
	{
		this.partie            = new Partie(this.metier);
		this.resultatsAffiches = false;
		this.frameInfos        = new FrameInfos(this);

		int nb = this.partie.getNbJoueurs();
		this.framesJoueurs = new FrameJoueur[nb];
		for (int i = 0; i < nb; i++)
			this.framesJoueurs[i] = new FrameJoueur(this, i + 1);

		placerFenetres();
	}

	// Place la bande d'infos au centre et les plateaux de part et d'autre.
	private void placerFenetres()
	{
		Dimension ecran = Toolkit.getDefaultToolkit().getScreenSize();

		int bandeW = this.frameInfos.getWidth();
		int bandeH = Math.min(this.frameInfos.getHeight(), ecran.height - 60);
		int bx     = (ecran.width - bandeW) / 2;
		this.frameInfos.setBounds(bx, 20, bandeW, bandeH);

		int nb     = this.framesJoueurs.length;
		int gap    = 15;
		int rangs  = (nb <= 2) ? 1 : 2;
		int boardW = Math.min(460, bx - gap - 10);
		int boardH = Math.min(520, (ecran.height - 60) / rangs - 10);

		for (int i = 0; i < nb; i++)
		{
			boolean gauche = (i % 2 == 0);
			int x = gauche ? (bx - gap - boardW) : (bx + bandeW + gap);
			int y = 20 + (i / 2) * (boardH + 15);
			if (x < 0) x = 0;
			this.framesJoueurs[i].setBounds(x, y, boardW, boardH);
		}
	}

	// Un joueur pose la carte commune sur une case. Retourne true si le coup est valide.
	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		return this.partie != null && this.partie.jouerCoup(numeroJoueur, numCase);
	}

	// Un joueur passe son tour pour la carte commune
	public void passerTour(int numeroJoueur)
	{
		if (this.partie != null) this.partie.passerTour(numeroJoueur);
	}

	// Rafraîchit toutes les fenêtres joueurs + la bande d'infos.
	public void rafraichirTout()
	{
		if (this.frameInfos != null) this.frameInfos.rafraichir();

		if (this.framesJoueurs != null)
		{
			for (int i = 0; i < this.framesJoueurs.length; i++)
				if (this.framesJoueurs[i] != null) this.framesJoueurs[i].rafraichir();
		}

		// Fin de partie : on cache les fenêtres de jeu et on affiche l'écran de résultats
		if (this.partie != null && this.partie.isPartieTerminee() && !this.resultatsAffiches)
		{
			this.resultatsAffiches = true;
			cacherFenetres();
			new FrameResultats(this);
		}
	}

	// Cache la bande d'infos et toutes les fenêtres joueurs (fin de partie)
	private void cacherFenetres()
	{
		if (this.frameInfos != null) this.frameInfos.setVisible(false);
		if (this.framesJoueurs != null)
		{
			for (int i = 0; i < this.framesJoueurs.length; i++)
				if (this.framesJoueurs[i] != null) this.framesJoueurs[i].setVisible(false);
		}
	}

	/*-------------------------------------*/
	/* Accès à la partie (pour l'IHM)      */
	/*-------------------------------------*/

	public int      getNbJoueurs()                     { return this.partie != null ? this.partie.getNbJoueurs() : this.nbJoueurs; }
	public Joueur[] getJoueurs()                       { return this.partie == null ? new Joueur[0] : this.partie.getJoueurs(); }
	public boolean  isPartieTerminee()                 { return this.partie != null && this.partie.isPartieTerminee(); }
	public boolean  aJoue(int numeroJoueur)            { return this.partie != null && this.partie.aJoue(numeroJoueur); }
	public boolean  estSonTour(int numeroJoueur)       { return this.partie != null && this.partie.estSonTour(numeroJoueur); }
	public Carte    getCarteCourante()                 { return this.partie == null ? null : this.partie.getCarteCourante(); }
	public int      getNbFonceesRestantes()            { return this.partie == null ? 0 : this.partie.getNbFonceesRestantes(); }

	public ArrayList<Integer> getCasesValides(int numeroJoueur) { return this.partie == null ? new ArrayList<Integer>() : this.partie.getCasesValides(numeroJoueur); }
	public ArrayList<Integer> getCheminJoueur(int numeroJoueur) { return this.partie == null ? new ArrayList<Integer>() : this.partie.getCheminJoueur(numeroJoueur); }

	public Color getCouleurJoueur(int numeroJoueur)
	{
		if (numeroJoueur >= 1 && numeroJoueur <= COULEURS_JOUEURS.length)
			return COULEURS_JOUEURS[numeroJoueur - 1];
		return Color.WHITE;
	}

	/*-------------------------------------*/
	/* Accès au plateau (métier)           */
	/*-------------------------------------*/

	public int getLargeur()              { return this.metier.getLargeur(); }
	public int getHauteur()              { return this.metier.getHauteur(); }
	public int getArrondissement(int n)  { return this.metier.getArrondissement(n); }
	public int getStation(int n)         { return this.metier.getStation(n); }
	public int getDepart(int n)          { return this.metier.getDepart(n); }

	/*-------------------------------------*/
	/* Images (stations + fonds)           */
	/*-------------------------------------*/

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

	public String getImageFond()  { return "plateau/images/fond.png"; }
	public String getImageFond2() { return "plateau/images/fond2.png"; }

	public static void main(String[] args)
	{
		new Controleur();
	}
}
