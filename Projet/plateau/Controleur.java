package plateau;

import plateau.ihm.FrameAccueil;
import plateau.metier.Plateau;

public class Controleur
{
	private FrameAccueil ihm;
	private Plateau      metier;

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil ( this );
	}

	public void initialiserPlateau(int largeur, int hauteur)
	{
		int j = this.metier.getNbJoueurs();    // on garde la config en recreant le plateau
		int m = this.metier.getNbMetro();
		this.metier = new Plateau(largeur, hauteur);
		this.metier.setConfig(j, m);
	}

	// la config vit maintenant dans le metier (donc sauvegardee avec le plateau)
	public void setConfigJeu(int nbJoueurs, int nbMetro) { this.metier.setConfig(nbJoueurs, nbMetro); }
	public int  getNbJoueurs() { return this.metier.getNbJoueurs(); }
	public int  getNbMetro()   { return this.metier.getNbMetro(); }

	public boolean chargerPlateau(java.io.File file)
	{
		return this.metier.chargerPlateau(file);
	}

	public int getLargeur()
	{
		return this.metier.getLargeur();
	}

	public int getHauteur()
	{
		return this.metier.getHauteur();
	}

	// ── Coordonnees / requetes (delegues au metier) ──
	public int getNbCases()                       { return this.metier.getNbCases(); }
	public int getLigne(int numCase)              { return this.metier.getLigne(numCase); }
	public int getColonne(int numCase)            { return this.metier.getColonne(numCase); }
	public int getNumCase(int ligne, int colonne) { return this.metier.getNumCase(ligne, colonne); }
	public int   compterArrondissement(int arr)   { return this.metier.compterArrondissement(arr); }
	public int[] getCasesArrondissement(int arr)  { return this.metier.getCasesArrondissement(arr); }
	public int[] getVoisins(int numCase)          { return this.metier.getVoisins(numCase); }
	public boolean estValide()                    { return this.metier.estValide(); }

	public int getArrondissement(int numCase)
	{
		return this.metier.getArrondissement(numCase);
	}

	public void affecterMetro(int numCase, int metro)
	{
		this.metier.affecterMetro(numCase, metro);
	}

	public int getMetro(int numCase)
	{
		return this.metier.getMetro(numCase);
	}

	public void affecterDepart(int numCase, int depart)
	{
		this.metier.affecterDepart(numCase, depart);
	}

	public int getDepart(int numCase)
	{
		return this.metier.getDepart(numCase);
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		return this.metier.enregistrerPlateau(nomFichier);
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		this.metier.affecterArrondissement( numCase, arrondissement );
	}

	// cherche une image dans les emplacements possibles (factorise : etait duplique fond/fond2)
	private String getImage(String nom)
	{
		String[] bases = { "plateau/images/", "images/", "../plateau/images/", "../../plateau/images/" };
		for (String b : bases)
		{
			java.io.File file = new java.io.File(b + nom);
			if (file.exists()) return file.getAbsolutePath();
		}
		return "plateau/images/" + nom;
	}

	public String getImageFond()  { return getImage("fond.png");  }
	public String getImageFond2() { return getImage("fond2.png"); }

	public static void main(String[] a)
	{
		new Controleur();
	}
}
