package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

public class Plateau
{
	private int[] tabCases;
	private int[] tabMetros;
	private int[] tabDeparts;
	private int   largeur;
	private int   hauteur;
	private int   nbJoueurs;
	private int   nbMetro;

	public Plateau(int largeur, int hauteur)
	{
		this.largeur  = largeur;
		this.hauteur  = hauteur;
		this.tabCases = new int[largeur * hauteur];
		this.tabMetros = new int[largeur * hauteur];
		this.tabDeparts = new int[largeur * hauteur];
	}

	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }
	public int getNbCases() { return this.tabCases.length; }

	// ── Config de la partie (sauvegardee avec le plateau) ──
	public void setConfig(int nbJoueurs, int nbMetro) { this.nbJoueurs = nbJoueurs; this.nbMetro = nbMetro; }
	public int  getNbJoueurs() { return this.nbJoueurs; }
	public int  getNbMetro()   { return this.nbMetro; }

	// ── Numero de case <-> coordonnees ──
	public int getLigne(int numCase)              { return numCase / this.largeur; }
	public int getColonne(int numCase)            { return numCase % this.largeur; }
	public int getNumCase(int ligne, int colonne) { return ligne * this.largeur + colonne; }

	// ── Requetes sur le plateau ──

	// nombre de cases d'un arrondissement
	public int compterArrondissement(int arrondissement)
	{
		int n = 0;
		for (int c : this.tabCases) if (c == arrondissement) n++;
		return n;
	}

	// numeros des cases d'un arrondissement
	public int[] getCasesArrondissement(int arrondissement)
	{
		int[] res = new int[compterArrondissement(arrondissement)];
		int k = 0;
		for (int i = 0; i < this.tabCases.length; i++)
			if (this.tabCases[i] == arrondissement) res[k++] = i;
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
		for (int m : this.tabMetros)  if (m != 0) nbMetros++;
		for (int d : this.tabDeparts) if (d != 0) nbDeparts++;
		return nbMetros > 0 && nbDeparts >= this.nbJoueurs;
	}

	public int getArrondissement(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabCases.length)
			return this.tabCases[numCase];
		return 0;
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		if (numCase >= 0 && numCase < this.tabCases.length)
			this.tabCases[numCase] = arrondissement;
	}

	public void affecterMetro(int numCase, int metro)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length)
			this.tabMetros[numCase] = metro;
	}

	public int getMetro(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length)
			return this.tabMetros[numCase];
		return 0;
	}

	public void affecterDepart(int numCase, int depart)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			this.tabDeparts[numCase] = depart;
	}

	public int getDepart(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length)
			return this.tabDeparts[numCase];
		return 0;
	}

	public boolean chargerPlateau(File file)
	{
		try
		{
			Scanner scanner = new Scanner(file);
			if (!scanner.hasNextLine()) { scanner.close(); return false; }

			String firstLine = scanner.nextLine();
			String[] dim = firstLine.split(";");
			this.largeur = Integer.parseInt(dim[0]);
			this.hauteur = Integer.parseInt(dim[1]);
			if (dim.length >= 4)   // config presente (nouveau format)
			{
				this.nbJoueurs = Integer.parseInt(dim[2]);
				this.nbMetro   = Integer.parseInt(dim[3]);
			}

			int size = this.largeur * this.hauteur;
			this.tabCases   = new int[size];
			this.tabMetros  = new int[size];
			this.tabDeparts = new int[size];

			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(";");
				int index = Integer.parseInt(parts[0]);
				if (index >= 0 && index < size)
				{
					this.tabCases[index] = Integer.parseInt(parts[1]);

					// compatibilite ancien format (2 colonnes) / nouveau format (4 colonnes)
					if (parts.length >= 3) this.tabMetros[index]  = Integer.parseInt(parts[2]);
					if (parts.length >= 4) this.tabDeparts[index] = Integer.parseInt(parts[3]);
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

	public boolean enregistrerPlateau(String nomFichier)
	{
		try
		{
			java.io.File dossier = new java.io.File("sauvegarde");
			if (!dossier.exists())
			{
				dossier.mkdirs();
			}

			File file = new java.io.File(dossier, nomFichier.endsWith(".txt") ? nomFichier : nomFichier + ".txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));

			// largeur ; hauteur ; nbJoueurs ; nbMetro (la config est sauvegardee avec le plateau)
			pw.println(this.largeur + ";" + this.hauteur + ";" + this.nbJoueurs + ";" + this.nbMetro);

			for (int i = 0; i < this.tabCases.length; i++)
			{
				pw.println(i + ";" + this.tabCases[i] + ";" + this.tabMetros[i] + ";" + this.tabDeparts[i]);
			}

			pw.close();
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}

		System.out.println("Plateau enregistre.");
		return true;
	}
}