package plateau.metier;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;

public class Plateau
{
	private int[] tabArrondissements;
	private int[] tabStations;
	private int[] tabDeparts;
	private int   largeur;
	private int   hauteur;
	private boolean[][] matriceAretes;

	public Plateau(int largeur, int hauteur)
	{
		this.largeur  = largeur;
		this.hauteur  = hauteur;
		int size = largeur * hauteur;
		this.tabArrondissements = new int[size];
		this.tabStations = new int[size];
		this.tabDeparts = new int[size];
		this.matriceAretes = new boolean[size][size];
	}

	public int getLargeur() { return this.largeur; }
	public int getHauteur() { return this.hauteur; }

	public int getArrondissement(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabArrondissements.length)
			return this.tabArrondissements[numCase];
		return 0;
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		if (numCase >= 0 && numCase < this.tabArrondissements.length)
			this.tabArrondissements[numCase] = arrondissement;
	}

	public void affecterStation(int numCase, int station)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			this.tabStations[numCase] = station;
	}

	public int getStation(int numCase)
	{
		if (numCase >= 0 && numCase < this.tabStations.length)
			return this.tabStations[numCase];
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

	public boolean aArete(int i, int j)
	{
		int size = this.largeur * this.hauteur;
		if (i >= 0 && i < size && j >= 0 && j < size)
		{
			return this.matriceAretes[i][j];
		}
		return false;
	}

	public void genererAretesAuto()
	{
		int size = this.largeur * this.hauteur;
		this.matriceAretes = new boolean[size][size];

		// Les 4 directions vers le bas / la droite (Est, Sud-Est, Sud, Sud-Ouest)
		int[] dx = {1, 1, 0, -1};
		int[] dy = {0, 1, 1,  1};

		for (int i = 0; i < size; i++)
		{
			if (this.tabStations[i] == 0) continue;

			int x = i % this.largeur;
			int y = i / this.largeur;

			for (int d = 0; d < 4; d++)
			{
				int cx = x + dx[d];
				int cy = y + dy[d];

				while (cx >= 0 && cx < this.largeur && cy >= 0 && cy < this.hauteur)
				{
					int targetIndex = cy * this.largeur + cx;
					if (this.tabStations[targetIndex] > 0)
					{
						this.matriceAretes[i][targetIndex] = true;
						this.matriceAretes[targetIndex][i] = true;
						break; // Trouvé le plus proche dans cette direction, arrêt de la recherche
					}
					cx += dx[d];
					cy += dy[d];
				}
			}
		}
	}

	private boolean aDesAretes()
	{
		for (int i = 0; i < this.matriceAretes.length; i++)
		{
			for (int j = 0; j < this.matriceAretes[i].length; j++)
			{
				if (this.matriceAretes[i][j]) return true;
			}
		}
		return false;
	}

	public boolean chargerPlateau(File file)
	{
		try (Scanner scanner = new Scanner(file))
		{
			if (!scanner.hasNextLine()) return false;
			
			String[] dim = scanner.nextLine().split(";");
			this.largeur = Integer.parseInt(dim[0]);
			this.hauteur = Integer.parseInt(dim[1]);
			
			int size = this.largeur * this.hauteur;
			this.tabArrondissements = new int[size];
			this.tabStations = new int[size];
			this.tabDeparts = new int[size];
			this.matriceAretes = new boolean[size][size];

			int cellCount = 0;
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) continue;
				String[] parts = line.split(";");
				
				if (cellCount < size)
				{
					int index = Integer.parseInt(parts[0]);
					this.tabArrondissements[index] = Integer.parseInt(parts[1]);
					if (parts.length >= 3) this.tabStations[index] = Integer.parseInt(parts[2]);
					if (parts.length >= 4) this.tabDeparts[index] = Integer.parseInt(parts[3]);
					cellCount++;
				}
				else
				{
					int src = Integer.parseInt(parts[0]);
					int dest = Integer.parseInt(parts[1]);
					this.matriceAretes[src][dest] = true;
					this.matriceAretes[dest][src] = true;
				}
			}

			// Si le fichier n'avait pas d'arêtes pré-enregistrées (ex: plateau_prof.txt)
			if (!aDesAretes())
			{
				genererAretesAuto();
			}

			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
			return false;
		}
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		try
		{
			genererAretesAuto();

			File dossier = new File("sauvegarde");
			if (!dossier.exists()) dossier.mkdirs();

			File file = new File(dossier, nomFichier.endsWith(".txt") ? nomFichier : nomFichier + ".txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));

			pw.println(this.largeur + ";" + this.hauteur);

			for (int i = 0; i < this.tabArrondissements.length; i++)
			{
				pw.println(i + ";" + this.tabArrondissements[i] + ";" + this.tabStations[i] + ";" + this.tabDeparts[i]);
			}

			for (int i = 0; i < this.tabArrondissements.length; i++)
			{
				for (int j = i + 1; j < this.tabArrondissements.length; j++)
				{
					if (this.matriceAretes[i][j])
					{
						pw.println(i + ";" + j);
					}
				}
			}

			pw.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}
	}
}