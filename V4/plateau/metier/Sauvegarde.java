package plateau.metier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// S'occupe UNIQUEMENT de lire/ecrire un plateau sur le disque (separation des responsabilites).
// Format : 1re ligne = largeur;hauteur;nbJoueurs;nbStations , puis 1 ligne par case = numCase;arr;station;depart.
// Les aretes ne sont pas sauvegardees : elles se recalculent a partir des stations.
public class Sauvegarde
{
	private static final String DOSSIER = "sauvegarde";

	public static boolean enregistrer(Plateau p, String nomFichier)
	{
		try
		{
			File dossier = new File(DOSSIER);
			if (!dossier.exists()) dossier.mkdirs();

			if (!nomFichier.endsWith(".txt")) nomFichier = nomFichier + ".txt";
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(dossier, nomFichier)));

			pw.println(p.getLargeur() + ";" + p.getHauteur() + ";" + p.getNbJoueurs() + ";" + p.getNbStations());
			for (int i = 0; i < p.getNbCases(); i++)
			{
				Case c = p.getCase(i);
				pw.println(i + ";" + c.getArrondissement() + ";" + c.getStation() + ";" + c.getDepart());
			}
			pw.close();

			System.out.println("Plateau enregistre : " + DOSSIER + "/" + nomFichier);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Erreur d'enregistrement : " + e.getMessage());
			return false;
		}
	}

	public static Plateau charger(File fichier)
	{
		try
		{
			Scanner sc = new Scanner(fichier);
			if (!sc.hasNextLine()) { sc.close(); return null; }

			String[] entete = sc.nextLine().split(";");
			int largeur = Integer.parseInt(entete[0]);
			int hauteur = Integer.parseInt(entete[1]);
			Plateau p = new Plateau(largeur, hauteur);
			if (entete.length >= 4) p.setConfig(Integer.parseInt(entete[2]), Integer.parseInt(entete[3]));

			while (sc.hasNextLine())
			{
				String ligne = sc.nextLine().trim();
				if (ligne.isEmpty()) continue;
				String[] parts = ligne.split(";");
				if (parts.length < 2) continue;                 // ignore les lignes inattendues
				int index = Integer.parseInt(parts[0]);
				if (index < 0 || index >= p.getNbCases()) continue;

				Case c = p.getCase(index);                      // on remplit la case directement (pas de recalcul a chaque fois)
				c.setArrondissement(Integer.parseInt(parts[1]));
				if (parts.length >= 3) c.setStation(Integer.parseInt(parts[2]));
				if (parts.length >= 4) c.setDepart(Integer.parseInt(parts[3]));
			}
			sc.close();

			p.recalculerConfig();   // si le fichier n'avait pas de config
			p.genererAretes();      // les aretes sont derivees des stations
			return p;
		}
		catch (Exception e)
		{
			System.out.println("Erreur de lecture : " + e.getMessage());
			return null;
		}
	}

	// Liste les noms des fichiers .txt du dossier sauvegarde/ (pour le menu de chargement).
	public static String[] listerNoms()
	{
		File dossier = new File(DOSSIER);
		if (!dossier.exists() || !dossier.isDirectory()) return new String[0];

		File[] fichiers = dossier.listFiles();
		if (fichiers == null) return new String[0];

		List<String> noms = new ArrayList<>();
		for (File f : fichiers)
			if (f.isFile() && f.getName().endsWith(".txt")) noms.add(f.getName());
		return noms.toArray(new String[0]);
	}
}
