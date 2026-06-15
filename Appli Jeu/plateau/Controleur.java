package plateau;

import plateau.ihm.FrameAccueil;
import plateau.ihm.FrameInfos;
import plateau.ihm.FrameJoueur;
import plateau.ihm.FrameResultats;
import plateau.metier.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// Contrôleur du jeu : fait le lien entre l'IHM et la partie en cours.
public class Controleur
{
	private FrameAccueil  ihm;
	private Plateau       metier;

	// Partie en cours (mode local)
	private Partie        partie;
	private FrameJoueur[] framesJoueurs;
	private FrameInfos    frameInfos;
	private boolean       resultatsAffiches;

	// Couleurs des joueurs. Elles tournent à chaque manche (la couleur du J2 passe au J1, etc.).
	public static final Color[] COULEURS_JOUEURS = {
		new Color(220,  50,  50),  // rouge
		new Color( 50, 100, 220),  // bleu
		new Color( 50, 180,  50),  // vert
		new Color(220, 140,   0)   // orange
	};

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil(this);
	}

	/*-------------------------------------*/
	/* Chargement d'un plateau             */
	/*-------------------------------------*/
	public boolean chargerPlateau(File fichier)
	{
		Plateau p = ChargeurPlateau.charger(fichier);
		if (p != null) { this.metier = p; return true; }
		return false;
	}

	// Liste les plateaux (.txt) conçus dans l'Appli Conception.
	public File[] getSauvegardes()
	{
		String[] chemins = {
			"../Appli Conception/plateau/sauvegarde",
			"../../Appli Conception/plateau/sauvegarde",
			"plateau/sauvegarde",
			"sauvegarde"
		};
		for (int c = 0; c < chemins.length; c++)
		{
			File dir = new File(chemins[c]);
			if (dir.exists() && dir.isDirectory())
			{
				ArrayList<File> txt = new ArrayList<File>();
				File[] liste = dir.listFiles();
				if (liste != null)
					for (int i = 0; i < liste.length; i++)
						if (liste[i].isFile() && liste[i].getName().endsWith(".txt")) txt.add(liste[i]);

				if (!txt.isEmpty())
				{
					File[] resultat = new File[txt.size()];
					for (int i = 0; i < txt.size(); i++) resultat[i] = txt.get(i);
					return resultat;
				}
			}
		}
		return new File[0];
	}

	// Un plateau est jouable s'il a au moins un départ et une station.
	public boolean plateauEstJouable()
	{
		boolean aDepart  = false;
		boolean aStation = false;
		int taille = this.metier.getLargeur() * this.metier.getHauteur();
		for (int i = 0; i < taille; i++)
		{
			if (this.metier.getDepart(i)  > 0) aDepart  = true;
			if (this.metier.getStation(i) > 0) aStation = true;
		}
		return aDepart && aStation;
	}

	/*-------------------------------------*/
	/* Partie en LOCAL                     */
	/*-------------------------------------*/
	// Crée la partie, la bande d'infos centrale, une fenêtre par joueur, et place le tout.
	public void lancerPartieLocale(int nbManches)
	{
		this.partie            = new Partie(this.metier, nbManches);
		this.resultatsAffiches = false;

		this.frameInfos = new FrameInfos(this);

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

	public boolean jouerCoup(int numeroJoueur, int numCase)
	{
		return this.partie != null && this.partie.jouerCoup(numeroJoueur, numCase);
	}

	public void passerTour(int numeroJoueur)
	{
		if (this.partie != null) this.partie.passerTour(numeroJoueur);
	}

	// Rafraîchit toutes les fenêtres, puis gère la fin de manche / de partie.
	public void rafraichirTout()
	{
		rafraichirFrames();
		if (this.partie == null) return;

		if (this.partie.isEntreManche())
		{
			afficherScoresManche();
			this.partie.continuerManche();
			rafraichirFrames();
		}
		else if (this.partie.isPartieTerminee() && !this.resultatsAffiches)
		{
			this.resultatsAffiches = true;
			afficherScoresManche();
			new FrameResultats(this);
		}
	}

	private void rafraichirFrames()
	{
		if (this.frameInfos != null) this.frameInfos.rafraichir();
		if (this.framesJoueurs != null)
			for (int i = 0; i < this.framesJoueurs.length; i++)
				if (this.framesJoueurs[i] != null) this.framesJoueurs[i].rafraichir();
	}

	// Affiche le score de la manche qui vient de se terminer.
	private void afficherScoresManche()
	{
		int      m  = this.partie.getNumeroManche();
		Joueur[] js = this.partie.getJoueurs();

		String texte = "Manche " + m + " terminée\n\n";
		for (int i = 0; i < js.length; i++)
			texte += "Joueur " + (i + 1) + " : " + js[i].getScoreManche(m - 1) + " points\n";

		JOptionPane.showMessageDialog(null, texte);
	}

	/*-------------------------------------*/
	/* Accès à la partie (pour l'IHM)      */
	/*-------------------------------------*/
	public boolean  isPartieTerminee()              { return this.partie != null && this.partie.isPartieTerminee(); }
	public boolean  aJoue(int numeroJoueur)         { return this.partie != null && this.partie.aJoue(numeroJoueur); }
	public boolean  estSonTour(int numeroJoueur)    { return this.partie != null && this.partie.estSonTour(numeroJoueur); }
	public int      getJoueurCourant()              { return this.partie == null ? 0 : this.partie.getJoueurCourant(); }
	public Carte    getCarteCourante()              { return this.partie == null ? null : this.partie.getCarteCourante(); }
	public int      getNbFonceesRestantes()         { return this.partie == null ? 0 : this.partie.getNbFonceesRestantes(); }
	public int      getCaseDepart(int numeroJoueur) { return this.partie == null ? -1 : this.partie.getCaseDepart(numeroJoueur); }
	public int      getNbJoueurs()                  { return this.partie == null ? 0 : this.partie.getNbJoueurs(); }
	public int      getNbManches()                  { return this.partie == null ? 0 : this.partie.getNbManches(); }
	public int      getNumeroManche()               { return this.partie == null ? 0 : this.partie.getNumeroManche(); }
	public Joueur[] getJoueurs()                    { return this.partie == null ? new Joueur[0] : this.partie.getJoueurs(); }

	public ArrayList<Integer> getCasesValides(int numeroJoueur)
	{
		if (this.partie == null) return new ArrayList<Integer>();
		return this.partie.getCasesValides(numeroJoueur);
	}
	public ArrayList<Integer> getCheminJoueur(int numeroJoueur) { return this.partie == null ? new ArrayList<Integer>() : this.partie.getCheminJoueur(numeroJoueur); }

	// Couleur d'un joueur pour une manche (les couleurs tournent à chaque manche).
	public Color getCouleurJoueur(int numeroJoueur, int numeroManche)
	{
		if (numeroJoueur < 1) numeroJoueur = 1;
		if (numeroManche < 1) numeroManche = 1;
		int idx = ((numeroJoueur - 1) + (numeroManche - 1)) % COULEURS_JOUEURS.length;
		return COULEURS_JOUEURS[idx];
	}

	public Color getCouleurJoueur(int numeroJoueur)
	{
		return getCouleurJoueur(numeroJoueur, getNumeroManche());
	}

	// Gagnant(s) : meilleur total ; égalité -> meilleure manche ; sinon victoire partagée.
	public ArrayList<Integer> getGagnants()
	{
		ArrayList<Integer> gagnants = new ArrayList<Integer>();
		if (this.partie == null) return gagnants;
		Joueur[] js = this.partie.getJoueurs();

		// Meilleur score total
		int maxTotal = 0;
		for (int i = 0; i < js.length; i++)
			if (js[i].getScoreTotal() > maxTotal) maxTotal = js[i].getScoreTotal();

		ArrayList<Integer> candidats = new ArrayList<Integer>();
		for (int i = 0; i < js.length; i++)
			if (js[i].getScoreTotal() == maxTotal) candidats.add(i + 1);

		if (candidats.size() == 1) return candidats;

		// Égalité -> meilleur score sur une seule manche
		int maxManche = 0;
		for (int i = 0; i < candidats.size(); i++)
		{
			int n = candidats.get(i);
			if (js[n - 1].getMeilleurScoreManche() > maxManche) maxManche = js[n - 1].getMeilleurScoreManche();
		}
		for (int i = 0; i < candidats.size(); i++)
		{
			int n = candidats.get(i);
			if (js[n - 1].getMeilleurScoreManche() == maxManche) gagnants.add(n);
		}
		return gagnants;
	}

	/*-------------------------------------*/
	/* Accès au plateau                    */
	/*-------------------------------------*/
	public int getLargeur()             { return this.metier.getLargeur(); }
	public int getHauteur()             { return this.metier.getHauteur(); }
	public int getArrondissement(int i) { return this.metier.getArrondissement(i); }
	public int getStation(int i)        { return this.metier.getStation(i); }

	/*-------------------------------------*/
	/* Images                              */
	/*-------------------------------------*/
	private Image[] imagesStations = new Image[11];

	public Image getImageStation(int numero)
	{
		if (numero < 1 || numero >= this.imagesStations.length) return null;
		if (this.imagesStations[numero] == null)
		{
			String chemin = UtilitaireJeu.getCheminImageStation(numero);
			if (chemin != null) this.imagesStations[numero] = new ImageIcon(chemin).getImage();
		}
		return this.imagesStations[numero];
	}

	// Cache des images de cartes : imagesCartes[numéro][0 = claire, 1 = foncée]. Numéro 1..7 (7 = joker).
	private Image[][] imagesCartes = new Image[8][2];

	// Image d'une carte (joker = numéro 7). Renvoie null si l'image manque.
	public Image getImageCarte(Carte carte)
	{
		if (carte == null) return null;
		int numero = carte.estJoker() ? 7 : carte.getTypeStation();
		int indice = carte.estFoncee() ? 1 : 0;
		if (numero < 1 || numero >= this.imagesCartes.length) return null;

		if (this.imagesCartes[numero][indice] == null)
		{
			String chemin = UtilitaireJeu.getCheminImageCarte(numero, carte.estFoncee());
			if (chemin != null) this.imagesCartes[numero][indice] = new ImageIcon(chemin).getImage();
		}
		return this.imagesCartes[numero][indice];
	}

	public String getImageFond()  { return chercherImage("fond.png"); }
	public String getImageFond2() { return chercherImage("fond2.png"); }

	// Cherche une image de fond dans les emplacements possibles.
	private String chercherImage(String nom)
	{
		String[] chemins = {
			"plateau/images/"       + nom,
			"images/"               + nom,
			"../plateau/images/"    + nom,
			"../../plateau/images/" + nom
		};
		for (int i = 0; i < chemins.length; i++)
		{
			File f = new File(chemins[i]);
			if (f.exists()) return f.getAbsolutePath();
		}
		return "plateau/images/" + nom;
	}

	public static void main(String[] a)
	{
		new Controleur();
	}
}
