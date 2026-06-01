package metier;

import java.util.*;

// ── Énumérations ───────────────────────────────────────────────────────────────

enum Symbole { CERCLE, CROIX, TRIANGLE, CARRE }
enum Couleur { ROUGE, VERT, BLEU, MARRON }
enum Teinte  { FONCE, CLAIR }

// ── Sommet ──────────────────────────────────────────────────────────────────────

class Sommet
{
    private int     id;
    private int     ligne;
    private int     colonne;
    private Symbole symbole;
    private Zone    zone;

    Sommet(int id, int ligne, int colonne, Symbole symbole)
    {
        this.id      = id;
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
        if (s == s1) return s2;
        return s1;
    }

    // vrai si l'arête relie bien les deux sommets a et b
    public boolean relie(Sommet a, Sommet b)
    {
        return (s1 == a && s2 == b) || (s1 == b && s2 == a);
    }
}

// ── Zone ──────────────────────────────────────────────────────────────────────

class Zone
{
    private int          id;
    private List<Sommet> sommets = new ArrayList<>();

    Zone(int id) { this.id = id; }

    public int getId() { return id; }

    public void ajouterSommet(Sommet s)
    {
        if (!sommets.contains(s))
        {
            sommets.add(s);
            s.setZone(this);
        }
    }

    public void retirerSommet(Sommet s) { sommets.remove(s); }

    public List<Sommet> getSommets() { return sommets; }
    public int          getTaille()  { return sommets.size(); }
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
        Sommet s = new Sommet(prochainId, ligne, colonne, symbole);
        prochainId++;
        sommets.add(s);
        return s;
    }

    public void supprimerSommet(Sommet s)
    {
        sommets.remove(s);

        // on enlève les arêtes qui touchent ce sommet
        List<Arete> aSupprimer = new ArrayList<>();
        for (Arete a : aretes)
            if (a.getS1() == s || a.getS2() == s)
                aSupprimer.add(a);
        aretes.removeAll(aSupprimer);

        if (s.getZone() != null)
            s.getZone().retirerSommet(s);
    }

    // cherche le sommet à une position donnée (null si la case est vide)
    public Sommet getSommet(int ligne, int colonne)
    {
        for (Sommet s : sommets)
            if (s.getLigne() == ligne && s.getColonne() == colonne)
                return s;
        return null;
    }

    public List<Sommet> getSommets() { return sommets; }

    // ── Arêtes ─────────────────────────────────────────────────────────────────

    /**
     * Reconstruit toutes les arêtes du plateau. Un sommet est relié à ses voisins
     * dans les 8 directions. Pour ne pas créer deux fois la même arête, on ne
     * regarde que 4 directions (la droite et les trois du bas) : l'autre moitié
     * sera ajoutée quand on traitera le sommet voisin.
     */
    public void genererAretes()
    {
        aretes.clear();

        // {décalage ligne, décalage colonne} : E, SW, S, SE
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
        for (Arete a : aretes)
        {
            if (a.getS1() == s)      voisins.add(a.getS2());
            else if (a.getS2() == s) voisins.add(a.getS1());
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
        List<Base> resultat = new ArrayList<>();
        for (Base b : bases)
            if (b.getCouleur() == couleur)
                resultat.add(b);
        return resultat;
    }
}
