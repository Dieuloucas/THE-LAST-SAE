package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

// Le plateau = une grille de cases (objets Case) + la configuration de la partie.
// Une case est reperee par un numero unique : numCase = ligne * largeur + colonne.
public class Plateau
{
	private Case[]        cases;
	private int           largeur;
	private int           hauteur;
	private Configuration config = new Configuration();

	public Plateau(int largeur, int hauteur)
	{
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.cases   = new Case[largeur * hauteur];
		for (int i = 0; i < this.cases.length; i++)   // chaque case est un objet Case (vide au depart)
			this.cases[i] = new Case();
	}

	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }
	public int getNbCases() { return this.cases.length; }

	// ── Config de la partie (sauvegardee avec le plateau) ──
	public void setConfig(int nbJoueurs, int nbMetro) { this.config.setConfig(nbJoueurs, nbMetro); }
	public int  getNbJoueurs() { return this.config.getNbJoueurs(); }
	public int  getNbMetro()   { return this.config.getNbMetro(); }

	// ── Numero de case <-> coordonnees ──
	public int getLigne(int numCase)              { return numCase / this.largeur; }
	public int getColonne(int numCase)            { return numCase % this.largeur; }
	public int getNumCase(int ligne, int colonne) { return ligne * this.largeur + colonne; }

	// garde-fou : la case existe-t-elle ?
	private boolean caseValide(int numCase) { return numCase >= 0 && numCase < this.cases.length; }

	// ── Acces a une case (delegue a l'objet Case) ──
	public int  getArrondissement(int numCase)               { return caseValide(numCase) ? this.cases[numCase].getArrondissement() : 0; }
	public void affecterArrondissement(int numCase, int arr) { if (caseValide(numCase)) this.cases[numCase].setArrondissement(arr); }

	public int  getMetro(int numCase)                 { return caseValide(numCase) ? this.cases[numCase].getMetro() : 0; }
	public void affecterMetro(int numCase, int metro) { if (caseValide(numCase)) this.cases[numCase].setMetro(metro); }

	public int  getDepart(int numCase)                  { return caseValide(numCase) ? this.cases[numCase].getDepart() : 0; }
	public void affecterDepart(int numCase, int depart) { if (caseValide(numCase)) this.cases[numCase].setDepart(depart); }

	// ── Requetes sur le plateau ──

	// nombre de cases d'un arrondissement
	public int compterArrondissement(int arrondissement)
	{
		int n = 0;
		for (Case c : this.cases) if (c.getArrondissement() == arrondissement) n++;
		return n;
	}

	// numeros des cases d'un arrondissement
	public int[] getCasesArrondissement(int arrondissement)
	{
		int[] res = new int[compterArrondissement(arrondissement)];
		int k = 0;
		for (int i = 0; i < this.cases.length; i++)
			if (this.cases[i].getArrondissement() == arrondissement) res[k++] = i;
		return res;
	}

	// cases voisines (8 directions) d'une case
	public int[] getVoisins(int numCase)
	{
		int ligne = getLigne(numCase), colonne = getColonne(numCase);
		int[] tmp = new int[8];
		int n = 0;
		for (int dl = -1; dl <= 1; dl++)
			for (int dc = -1; dc <= 1; dc++)
			{
				if (dl == 0 && dc == 0) continue;
				int l = ligne + dl, c = colonne + dc;
				if (l >= 0 && l < this.hauteur && c >= 0 && c < this.largeur)
					tmp[n++] = getNumCase(l, c);
			}
		int[] res = new int[n];
		for (int i = 0; i < n; i++) res[i] = tmp[i];
		return res;
	}

	// le plateau est-il jouable ? (au moins 1 metro pose et assez de departs pour les joueurs)
	public boolean estValide()
	{
		int nbMetros = 0, nbDeparts = 0;
		for (Case c : this.cases)
		{
			if (c.getMetro()  != 0) nbMetros++;
			if (c.getDepart() != 0) nbDeparts++;
		}
		return nbMetros > 0 && nbDeparts >= this.config.getNbJoueurs();
	}

	// ── Sauvegarde / chargement (fichier texte) ──
	// format : 1re ligne = largeur;hauteur;nbJoueurs;nbMetro , puis 1 ligne par case = numCase;arr;metro;depart

	public boolean enregistrerPlateau(String nomFichier)
	{
		try
		{
			File dossier = new File("sauvegarde");
			if (!dossier.exists()) dossier.mkdirs();

			if (!nomFichier.endsWith(".txt")) nomFichier = nomFichier + ".txt";
			File file = new File(dossier, nomFichier);

			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.println(this.largeur + ";" + this.hauteur + ";" + this.config.getNbJoueurs() + ";" + this.config.getNbMetro());
			for (int i = 0; i < this.cases.length; i++)
			{
				Case c = this.cases[i];
				pw.println(i + ";" + c.getArrondissement() + ";" + c.getMetro() + ";" + c.getDepart());
			}
			pw.close();

			System.out.println("Plateau enregistre.");
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}
	}

	public boolean chargerPlateau(File file)
	{
		try
		{
			Scanner scanner = new Scanner(file);
			if (!scanner.hasNextLine()) { scanner.close(); return false; }

			String[] dim = scanner.nextLine().split(";");
			this.largeur = Integer.parseInt(dim[0]);
			this.hauteur = Integer.parseInt(dim[1]);
			if (dim.length >= 4)   // config presente (nouveau format)
				this.config.setConfig(Integer.parseInt(dim[2]), Integer.parseInt(dim[3]));

			int size = this.largeur * this.hauteur;
			this.cases = new Case[size];
			for (int i = 0; i < size; i++) this.cases[i] = new Case();

			while (scanner.hasNextLine())
			{
				String ligne = scanner.nextLine();
				if (ligne.trim().isEmpty()) continue;
				String[] parts = ligne.split(";");
				int index = Integer.parseInt(parts[0]);
				if (index >= 0 && index < size)
				{
					this.cases[index].setArrondissement(Integer.parseInt(parts[1]));
					if (parts.length >= 3) this.cases[index].setMetro(Integer.parseInt(parts[2]));
					if (parts.length >= 4) this.cases[index].setDepart(Integer.parseInt(parts[3]));
				}
			}
			scanner.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur de lecture : " + e.getMessage());
			return false;
		}
	}
}
