package metier;

import java.util.*;

// ── Carte ─────────────────────────────────────────────────────────────────────

class Carte
{
    private Symbole symbole; // null si c'est un joker
    private Teinte  teinte;
    private boolean joker;

    private Carte(Symbole symbole, Teinte teinte, boolean joker)
    {
        this.symbole = symbole;
        this.teinte  = teinte;
        this.joker   = joker;
    }

    public static Carte de(Symbole symbole, Teinte teinte) { return new Carte(symbole, teinte, false); }
    public static Carte joker(Teinte teinte)               { return new Carte(null, teinte, true); }

    public Symbole getSymbole() { return symbole; }
    public Teinte  getTeinte()  { return teinte; }
    public boolean estJoker()   { return joker; }
    public boolean estFonce()   { return teinte == Teinte.FONCE; }

    public String toString()
    {
        // TODO (12)
        return "";
    }
}

// ── Pioche ────────────────────────────────────────────────────────────────────

class Pioche
{
    private List<Carte> toutesLesCartes = new ArrayList<>();
    private List<Carte> restantes       = new ArrayList<>();

    Pioche()
    {
        // TODO (13)
    }

    public void reinitialiser()
    {
        // TODO (14)
    }

    public Carte piocher()
    {
        // TODO (15)
        return null;
    }

    public boolean estVide()        { return restantes.isEmpty(); }
    public int     getNbRestantes() { return restantes.size(); }
}

// ── CheminCouleur ─────────────────────────────────────────────────────────────

class CheminCouleur
{
    private Couleur      couleur;
    private Base         base;
    private Plateau      plateau;
    private List<Sommet> relies            = new ArrayList<>(); // sommets du réseau
    private List<Arete>  liens             = new ArrayList<>(); // arêtes internes au réseau
    private List<Arete>  aretesDisponibles = new ArrayList<>(); // arêtes jouables (frontière)

    CheminCouleur(Couleur couleur, Base base, Plateau plateau)
    {
        this.couleur = couleur;
        this.base    = base;
        this.plateau = plateau;
        // TODO (16)
    }

    public boolean ajouter(Sommet s)
    {
        // TODO (17)
        return false;
    }

    public void recalculer()
    {
        // TODO (18)
    }

    public boolean contient(Sommet s) { return relies.contains(s); }

    // vrai s'il existe déjà un lien interne entre a et b
    public boolean aLeLien(Sommet a, Sommet b)
    {
        // TODO (19)
        return false;
    }

    // vrai si s est voisin d'au moins un sommet déjà relié
    public boolean estAdjacent(Sommet s)
    {
        // TODO (20)
        return false;
    }

    public Couleur      getCouleur()           { return couleur; }
    public List<Sommet> getRelies()            { return relies; }
    public List<Arete>  getLiens()             { return liens; }
    public List<Arete>  getAretesDisponibles() { return aretesDisponibles; }

    public List<Zone> getZonesConquises()
    {
        // TODO (21)
        return null;
    }

    public int calculerScore()
    {
        // TODO (22)
        return 0;
    }
}

// ── Manche ────────────────────────────────────────────────────────────────────

class Manche
{
    private Couleur couleur;
    private Pioche  pioche;
    private int     nbFoncees = 0;
    private int     nbClaires = 0;

    private static final int SEUIL_ARRET = 3;

    Manche(Couleur couleur)
    {
        this.couleur = couleur;
        this.pioche  = new Pioche();
    }

    public Couleur getCouleur() { return couleur; }

    public Carte piocher()
    {
        // TODO (23)
        return null;
    }

    public boolean tourTermine()
    {
        // TODO (24)
        return false;
    }

    public boolean piocheVide()   { return pioche.estVide(); }
    public int     getNbFoncees() { return nbFoncees; }
    public int     getNbClaires() { return nbClaires; }
}

// ── Joueur ────────────────────────────────────────────────────────────────────

class Joueur
{
    private int                         id;
    private String                      nom;
    private Map<Couleur, CheminCouleur> chemins = new EnumMap<>(Couleur.class);

    Joueur(int id, String nom)
    {
        this.id  = id;
        this.nom = nom;
    }

    public int    getId()  { return id; }
    public String getNom() { return nom; }

    public void initialiserChemin(Couleur couleur, Base base, Plateau plateau)
    {
        chemins.put(couleur, new CheminCouleur(couleur, base, plateau));
    }

    public CheminCouleur               getChemin(Couleur couleur) { return chemins.get(couleur); }
    public Map<Couleur, CheminCouleur> getChemins()               { return chemins; }

    public int getScoreTotal()
    {
        // TODO (25)
        return 0;
    }

    public int getScorePourCouleur(Couleur couleur)
    {
        // TODO (26)
        return 0;
    }
}

// ── Jeu ───────────────────────────────────────────────────────────────────────

public class Jeu
{
    public enum Etat { EN_ATTENTE, EN_COURS, TERMINE }

    private Plateau       plateau;
    private List<Joueur>  joueurs;
    private List<Couleur> ordreManche = new ArrayList<>();
    private int           indiceManche = 0;
    private Manche        mancheActuelle;
    private Etat          etat = Etat.EN_ATTENTE;

    public Jeu(Plateau plateau, List<Joueur> joueurs)
    {
        this.plateau = plateau;
        this.joueurs = new ArrayList<>(joueurs);
        // TODO (27)
    }

    public void demarrer()
    {
        // TODO (28)
    }

    private void demarrerManche()
    {
        mancheActuelle = new Manche(ordreManche.get(indiceManche));
    }

    public Carte piocher() { return mancheActuelle.piocher(); }

    public boolean connecter(Joueur joueur, Sommet sommet)
    {
        // TODO (29)
        return false;
    }

    public boolean peutConnecter(Joueur joueur, Sommet sommet)
    {
        // TODO (30)
        return false;
    }

    public void supprimerSommet(Sommet sommet)
    {
        // TODO (31)
    }

    public boolean tourActuelTermine()
    {
        // TODO (32)
        return false;
    }

    public boolean mancheSuivante()
    {
        // TODO (33)
        return false;
    }

    public List<Joueur> getClassement()
    {
        // TODO (34)
        return null;
    }

    public Plateau      getPlateau()         { return plateau; }
    public Manche       getMancheActuelle()  { return mancheActuelle; }
    public Etat         getEtat()            { return etat; }
    public List<Joueur> getJoueurs()         { return joueurs; }
    public Couleur      getCouleurActuelle() { return mancheActuelle.getCouleur(); }
}
