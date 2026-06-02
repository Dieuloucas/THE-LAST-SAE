package metier;

import java.util.*;

// ── Énumérations ───────────────────────────────────────────────────────────────

enum Symbole { CERCLE, CROIX, TRIANGLE, CARRE }
enum Couleur { ROUGE, VERT, BLEU, MARRON }
enum Teinte  { FONCE, CLAIR }

// ── Sommet ──────────────────────────────────────────────────────────────────────

class Sommet
{
    private static int     id = 0;
    private int     ligne;
    private int     colonne;
    private Symbole symbole;
    private Zone    zone;

    Sommet(int ligne, int colonne, Symbole symbole)
    {
        this.id++;
        this.ligne   = ligne;
        this.colonne = colonne;
        this.symbole = symbole;
    }

    public int     getId()      { return id; }
    public int     getLigne()   { return ligne; }
    public int     getColonne() { return colonne; }
    public Symbole getSymbole() { return symbole; }
    public Zone    getZone()    { return zone; }

    public void setSymbole(Symbole symbole) { this.symbole = symbole; }
    public void setZone(Zone zone)          { this.zone = zone; }
}

// ── Arête ─────────────────────────────────────────────────────────────────────

class Arete
{
    private Sommet s1;
    private Sommet s2;

    Arete(Sommet s1, Sommet s2)
    {
        this.s1 = s1;
        this.s2 = s2;
    }

    public Sommet getS1() { return s1; }
    public Sommet getS2() { return s2; }

    // renvoie l'autre bout de l'arête
    public Sommet getAutre(Sommet s)
    {
        if (s == this.s1)
			return this.s2;
        else if (s == this.s2)
			return this.s1;
		else 
			return null;
    }

    // vrai si l'arête relie bien les deux sommets a et b
    public boolean relie(Sommet a, Sommet b)
    {
		if (a == s1 && b == s2 || a == s2 && b == s1 )
        	return true ;
		else 
			return false;
    }
}

// ── Zone ──────────────────────────────────────────────────────────────────────

class Zone
{
    private int         id;
    private List<int[]> cases = new ArrayList<>();

    Zone(int id) { this.id = id; }

    public int getId() { return id; }

    public void ajouterCase(int ligne, int colonne)
    {
        if (!contientCase(ligne, colonne))  
            cases.add(new int[]{ligne, colonne});
    }

    public boolean contientCase(int ligne, int colonne)
    {
        for (int[] c : cases)
            if (c[0] == ligne && c[1] == colonne)
                return true;
        return false;
    }

    public List<int[]> getCases()  { return cases; }
    public int         getTaille() { return cases.size(); } // taille = nombre de cases
}
// ── Base ──────────────────────────────────────────────────────────────────────

class Base
{
    private Couleur couleur;
    private Sommet  sommet;

    Base(Couleur couleur, Sommet sommet)
    {
        this.couleur = couleur;
        this.sommet  = sommet;
    }

    public Couleur getCouleur() { return couleur; }
    public Sommet  getSommet()  { return sommet; }
}

// ── Plateau ─────────────────────────────────────────────────────────────────────

public class Plateau
{
    private int          lignes;
    private int          colonnes;
    private List<Sommet> sommets = new ArrayList<>();
    private List<Arete>  aretes  = new ArrayList<>();
    private List<Zone>   zones   = new ArrayList<>();
    private List<Base>   bases   = new ArrayList<>();
    private int          prochainId = 0;

    public Plateau(int lignes, int colonnes)
    {
        this.lignes   = lignes;
        this.colonnes = colonnes;
    }

    public int getLignes()   { return lignes; }
    public int getColonnes() { return colonnes; }

    // ── Sommets ────────────────────────────────────────────────────────────────

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = new Sommet(ligne, colonne, symbole);
		sommets.add(s);
		return s;
    }

    public void supprimerSommet(Sommet s){sommets.remove(s);}

    // cherche le sommet à une position donnée (null si la case est vide)
    public Sommet getSommet(int ligne, int colonne)
    {
		for (int i = 0; i < sommets.size(); i++)
		{
			if (sommets.get(i).getLigne() == ligne && sommets.get(i).getColonne() == colonne)
				return sommets.get(i);
		}
        return null;
    }

    public List<Sommet> getSommets() { return sommets; }

    // ── Arêtes ─────────────────────────────────────────────────────────────────

   public void genererAretes()
	{
    aretes.clear();

    // {décalage ligne, décalage colonne} : Est, Sud-Ouest, Sud, Sud-Est
    int[][] directions = { {0, 1}, {1, -1}, {1, 0}, {1, 1} };

    for (Sommet s : sommets)
    	{
        	for (int[] d : directions)
        	{
            	Sommet voisin = getSommet(s.getLigne() + d[0], s.getColonne() + d[1]);
            	if (voisin != null)
             	   aretes.add(new Arete(s, voisin));
   	    	}
   		}
	}

    public List<Arete> getAretes() { return aretes; }

    public List<Sommet> getVoisins(Sommet s)
	{
    	List<Sommet> voisins = new ArrayList<>();
    	for (Arete a : aretes)                      // on regarde TOUTES les arêtes
    	{
        	if (a.getS1() == s || a.getS2() == s)   // celles qui touchent s
            	voisins.add(a.getAutre(s));         // on prend l'autre bout
    	}
    	return voisins;
}

    // ── Zones ──────────────────────────────────────────────────────────────────

    public Zone ajouterZone()
	{
   		Zone z = new Zone(zones.size());
    	zones.add(z);
    	return z;
	}

    public List<Zone> getZones() { return zones; }

    // ── Bases ──────────────────────────────────────────────────────────────────

    public Base ajouterBase(Couleur couleur, Sommet sommet)
    {
		Base b = new Base(couleur, sommet);
		bases.add(b);
        return b;
    }

    public List<Base> getBases(Couleur couleur)
    {
        return bases;
    }
}
