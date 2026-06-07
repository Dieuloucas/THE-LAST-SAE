package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

// Le plateau = une grille de cases. Pour chaque case (numerotee de 0 a n-1) on retient :
//   - son arrondissement (tabCases)
//   - son type de metro   (tabMetros)
//   - son depart de joueur (tabDeparts)
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
		this.largeur    = largeur;
		this.hauteur    = hauteur;
		this.tabCases   = new int[largeur * hauteur];
		this.tabMetros  = new int[largeur * hauteur];
		this.tabDeparts = new int[largeur * hauteur];
	}

	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }
	public int getNbCases() { return this.tabCases.length; }

	// config de la partie (sauvegardee avec le plateau)
	public void setConfig(int nbJoueurs, int nbMetro) { this.nbJoueurs = nbJoueurs; this.nbMetro = nbMetro; }
	public int  getNbJoueurs() { return this.nbJoueurs; }
	public int  getNbMetro()   { return this.nbMetro; }

	// arrondissement
	public int getArrondissement(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabCases.length) return this.tabCases[numCase];
		return 0;
	}
	public void affecterArrondissement(int numCase, int arrondissement)
	{
		if (numCase >= 0 && numCase < this.tabCases.length) this.tabCases[numCase] = arrondissement;
	}

	// metro
	public int getMetro(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length) return this.tabMetros[numCase];
		return 0;
	}
	public void affecterMetro(int numCase, int metro)
	{
		if (numCase >= 0 && numCase < this.tabMetros.length) this.tabMetros[numCase] = metro;
	}

	// depart
	public int getDepart(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length) return this.tabDeparts[numCase];
		return 0;
	}
	public void affecterDepart(int numCase, int depart)
	{
		if (numCase >= 0 && numCase < this.tabDeparts.length) this.tabDeparts[numCase] = depart;
	}

	// enregistre le plateau dans un fichier texte (dossier sauvegarde/)
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
			pw.println(this.largeur + ";" + this.hauteur + ";" + this.nbJoueurs + ";" + this.nbMetro);
			for (int i = 0; i < this.tabCases.length; i++)
				pw.println(i + ";" + this.tabCases[i] + ";" + this.tabMetros[i] + ";" + this.tabDeparts[i]);
			pw.close();

			System.out.println("Plateau enregistre : " + file.getPath());
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}
	}

	// charge un plateau depuis un fichier texte
	public boolean chargerPlateau(File file)
	{
		try
		{
			Scanner scanner = new Scanner(file);

			String[] dim = scanner.nextLine().split(";");
			this.largeur = Integer.parseInt(dim[0]);
			this.hauteur = Integer.parseInt(dim[1]);
			if (dim.length >= 4)
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
				String ligne = scanner.nextLine();
				if (ligne.trim().isEmpty()) continue;
				String[] parts = ligne.split(";");
				int index = Integer.parseInt(parts[0]);
				if (index >= 0 && index < size)
				{
					this.tabCases[index] = Integer.parseInt(parts[1]);
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
}
