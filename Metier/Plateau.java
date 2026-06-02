package Metier;

import java.util.*;

public class Plateau
{
    private int          id;
    private String       nom;
    private int          lignes;
    private int          colonnes;
    private List<Sommet> sommets = new ArrayList<>();
    private List<Arete>  aretes  = new ArrayList<>();
    private List<Zone>   zones   = new ArrayList<>();
    private List<Base>   bases   = new ArrayList<>();

    public Plateau(int lignes, int colonnes)
    {
        this.lignes   = lignes;
        this.colonnes = colonnes;
    }

    public int    getLignes()   { return lignes; }
    public int    getColonnes() { return colonnes; }
    public int    getId()       { return id; }
    public String getNom()      { return nom; }
    public void   setId(int id)      { this.id = id; }
    public void   setNom(String nom) { this.nom = nom; }

    // ── Sommets ────────────────────────────────────────────────────────────────

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = new Sommet(ligne, colonne, symbole);
        sommets.add(s);
        return s;
    }

    public void supprimerSommet(Sommet s) { sommets.remove(s); }

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
        List<Base> resultat = new ArrayList<>();
        for (Base b : bases)
            if (b.getCouleur() == couleur)
                resultat.add(b);
        return resultat;
    }

    // ── Duplication ──────────────────────────────────────────────────────────────

    // renvoie une copie profonde et indépendante de ce plateau
    public Plateau dupliquer()
    {
        Plateau copie = new Plateau(this.lignes, this.colonnes);

        for (Sommet s : sommets)                              // recopie des sommets
            copie.ajouterSommet(s.getLigne(), s.getColonne(), s.getSymbole());

        for (Zone z : zones)                                  // recopie des zones (case par case)
        {
            Zone nouvelle = copie.ajouterZone();
            for (int[] c : z.getCases())
                nouvelle.ajouterCase(c[0], c[1]);
        }

        for (Base b : bases)                                  // recopie des bases
        {
            Sommet ancien  = b.getSommet();
            Sommet nouveau = copie.getSommet(ancien.getLigne(), ancien.getColonne());
            copie.ajouterBase(b.getCouleur(), nouveau);       // vers le NOUVEAU sommet
        }

        copie.genererAretes();
        return copie;
    }
}
