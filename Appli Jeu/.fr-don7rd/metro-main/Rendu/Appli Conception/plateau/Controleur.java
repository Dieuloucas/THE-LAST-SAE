package plateau;

import plateau.ihm.FrameAccueil;
import plateau.metier.*;

public class Controleur
{
	private FrameAccueil ihm;
	private Plateau      metier;

	public Controleur()
	{
		this.metier = new Plateau(10, 10);
		this.ihm    = new FrameAccueil ( this );
	}

	private int nbJoueurs;
	private int nbStations;

	public void initialiserPlateau(int largeur, int hauteur)
	{
		this.metier = new Plateau(largeur, hauteur);
	}

	public void setConfigJeu(int nbJoueurs, int nbStations)
	{
		this.nbJoueurs = nbJoueurs;
		this.nbStations = nbStations;
	}

	public int getNbJoueurs()
	{
		return this.nbJoueurs;
	}

	public int getNbStations()
	{
		return this.nbStations;
	}

	public boolean chargerPlateau(java.io.File fichier)
	{
		Plateau p = ChargeurPlateau.charger(fichier);
		if (p != null)
		{
			this.metier = p;
			return true;
		}
		return false;
	}

	public int getLargeur()
	{
		return this.metier.getLargeur();
	}

	public int getHauteur()
	{
		return this.metier.getHauteur();
	}

	public int getArrondissement(int numCase)
	{
		return this.metier.getArrondissement(numCase);
	}

	public void affecterStation(int numCase, int station)
	{
		this.metier.affecterStation(numCase, station);
	}

	public int getStation(int numCase)
	{
		return this.metier.getStation(numCase);
	}

	public void affecterDepart(int numCase, int depart)
	{
		this.metier.affecterDepart(numCase, depart);
	}

	public int getDepart(int numCase)
	{
		return this.metier.getDepart(numCase);
	}

	public boolean aArete(int i, int j)
	{
		return this.metier.getGraphe().aArete(i, j);
	}

	public int getNbAretes()
	{
		return this.metier.getGraphe().getNbAretes();
	}

	public void genererAretesAuto()
	{
		this.metier.getGraphe().genererAretesAuto(this.metier);
	}

	public boolean enregistrerPlateau(String nomFichier)
	{
		return EnregistreurPlateau.enregistrer(this.metier, nomFichier);
	}

	public void affecterArrondissement(int numCase, int arrondissement)
	{
		this.metier.affecterArrondissement( numCase, arrondissement );
	}

	public String getImageFond()
	{
		String[] chemins = {
			"plateau/images/fond.png",
			"images/fond.png",
			"../plateau/images/fond.png",
			"../../plateau/images/fond.png"
		};

		for (String chemin : chemins)
		{
			java.io.File fichier = new java.io.File(chemin);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}

		return "plateau/images/fond.png";
	}

	public String getImageFond2()
	{
		String[] chemins = {
			"plateau/images/fond2.png",
			"images/fond2.png",
			"../plateau/images/fond2.png",
			"../../plateau/images/fond2.png"
		};

		for (String chemin : chemins)
		{
			java.io.File fichier = new java.io.File(chemin);
			if (fichier.exists())
			{
				return fichier.getAbsolutePath();
			}
		}

		return "plateau/images/fond2.png";
	}

	public void mettreAJourConfigurationDepuisPlateau()
	{
		int[] maxConfig = ConfigurationPlateau.detecterMaxJoueursEtStations(this.metier, this.nbJoueurs, this.nbStations);
		this.setConfigJeu(maxConfig[0], maxConfig[1]);
	}

	public boolean validerDepartsPlateau()
	{
		return ValidateurPlateau.validerDeparts(this.metier, this.nbJoueurs);
	}

	public static void main(String[] a)
	{
		new Controleur();
	}
}
