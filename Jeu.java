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
        if (joker) return teinte + "_JOKER";
        return teinte + "_" + symbole;
    }
}

// ── Pioche ────────────────────────────────────────────────────────────────────

class Pioche
{
    private List<Carte> toutesLesCartes = new ArrayList<>();
    private List<Carte> restantes       = new ArrayList<>();

    Pioche()
    {
        // une carte par symbole dans chaque teinte, plus un joker par teinte
        for (Symbole s : Symbole.values())
        {
            toutesLesCartes.add(Carte.de(s, Teinte.FONCE));
            toutesLesCartes.add(Carte.de(s, Teinte.CLAIR));
        }
        toutesLesCartes.add(Carte.joker(Teinte.FONCE));
        toutesLesCartes.add(Carte.joker(Teinte.CLAIR));
        reinitialiser();
    }

    public void reinitialiser()
    {
        restantes = new ArrayList<>(toutesLesCartes);
        Collections.shuffle(restantes);
    }

    public Carte piocher()
    {
        if (restantes.isEmpty()) return null;
        return restantes.remove(restantes.size() - 1);
    }

    public boolean estVide()    { return restantes.isEmpty(); }
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
        relies.add(base.getSommet());
        recalculer();
    }

    /**
     * Relie un nouveau sommet au réseau. Il doit être voisin d'un sommet déjà
     * relié. Les arêtes qui le connectent au réseau deviennent des liens internes,
     * puis on recalcule la frontière.
     */
    public boolean ajouter(Sommet s)
    {
        if (relies.contains(s) || !estAdjacent(s)) return false;
        relies.add(s);

        for (Arete a : plateau.getAretes())
        {
            // on ne s'intéresse qu'aux arêtes touchant s
            if (a.getS1() != s && a.getS2() != s) continue;
            if (relies.contains(a.getAutre(s)) && !liens.contains(a))
                liens.add(a);
        }
        recalculer();
        return true;
    }

    /**
     * Recalcule les arêtes DISPONIBLES = la frontière du réseau : les arêtes dont
     * un seul des deux sommets est relié. Ce sont les coups proposés au joueur.
     * On en profite pour retirer du réseau les sommets/arêtes qui auraient été
     * supprimés du plateau.
     */
    public void recalculer()
    {
        // 1. on enlève ce qui n'existe plus sur le plateau
        List<Sommet> sommetsMorts = new ArrayList<>();
        for (Sommet s : relies)
            if (!plateau.getSommets().contains(s))
                sommetsMorts.add(s);
        relies.removeAll(sommetsMorts);

        List<Arete> liensMorts = new ArrayList<>();
        for (Arete a : liens)
            if (!plateau.getAretes().contains(a))
                liensMorts.add(a);
        liens.removeAll(liensMorts);

        // 2. on recalcule la frontière
        aretesDisponibles.clear();
        for (Arete a : plateau.getAretes())
        {
            boolean s1Relie = relies.contains(a.getS1());
            boolean s2Relie = relies.contains(a.getS2());
            if (s1Relie != s2Relie) // un seul des deux sommets est relié
                aretesDisponibles.add(a);
        }
    }

    public boolean contient(Sommet s) { return relies.contains(s); }

    // vrai s'il existe déjà un lien interne entre a et b
    public boolean aLeLien(Sommet a, Sommet b)
    {
        for (Arete lien : liens)
            if (lien.relie(a, b)) return true;
        return false;
    }

    // vrai si s est voisin d'au moins un sommet déjà relié
    public boolean estAdjacent(Sommet s)
    {
        for (Sommet relie : relies)
            for (Arete a : plateau.getAretes())
                if (a.relie(s, relie)) return true;
        return false;
    }

    public Couleur      getCouleur()           { return couleur; }
    public List<Sommet> getRelies()            { return relies; }
    public List<Arete>  getLiens()             { return liens; }
    public List<Arete>  getAretesDisponibles() { return aretesDisponibles; }

    // ── Score ──────────────────────────────────────────────────────────────────

    public List<Zone> getZonesConquises()
    {
        List<Zone> zones = new ArrayList<>();
        for (Sommet s : relies)
        {
            Zone z = s.getZone();
            if (z != null && !zones.contains(z))
                zones.add(z);
        }
        return zones;
    }

    /** Score = nombre de zones conquises × taille de la plus grosse. */
    public int calculerScore()
    {
        List<Zone> zones = getZonesConquises();
        if (zones.isEmpty()) return 0;

        int tailleMax = 0;
        for (Zone z : zones)
            if (z.getTaille() > tailleMax)
                tailleMax = z.getTaille();

        return zones.size() * tailleMax;
    }
}

// ── Manche ────────────────────────────────────────────────────────────────────

class Manche
{
    private Couleur couleur;
    private Pioche  pioche;
    private int     nbFoncees = 0;
    private int     nbClaires = 0;

    // seuil de cartes d'une teinte qui arrête le tour (à ajuster selon les règles)
    private static final int SEUIL_ARRET = 3;

    Manche(Couleur couleur)
    {
        this.couleur = couleur;
        this.pioche  = new Pioche();
    }

    public Couleur getCouleur() { return couleur; }

    public Carte piocher()
    {
        Carte c = pioche.piocher();
        if (c == null) return null;
        if (c.estFonce()) nbFoncees++;
        else              nbClaires++;
        return c;
    }

    public boolean tourTermine()
    {
        return nbFoncees >= SEUIL_ARRET || nbClaires >= SEUIL_ARRET;
    }

    public boolean piocheVide()  { return pioche.estVide(); }
    public int     getNbFoncees() { return nbFoncees; }
    public int     getNbClaires() { return nbClaires; }
}

// ── Joueur ────────────────────────────────────────────────────────────────────

class Joueur
{
    private int                       id;
    private String                    nom;
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

    public CheminCouleur getChemin(Couleur couleur) { return chemins.get(couleur); }

    public Map<Couleur, CheminCouleur> getChemins() { return chemins; }

    public int getScoreTotal()
    {
        int total = 0;
        for (CheminCouleur c : chemins.values())
            total += c.calculerScore();
        return total;
    }

    public int getScorePourCouleur(Couleur couleur)
    {
        CheminCouleur c = chemins.get(couleur);
        if (c == null) return 0;
        return c.calculerScore();
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

        // une manche par couleur, dans l'ordre de l'énumération
        for (Couleur c : Couleur.values())
            ordreManche.add(c);
    }

    public void demarrer()
    {
        // chaque joueur reçoit un chemin par couleur, à partir d'une base
        for (Joueur j : joueurs)
        {
            for (Couleur c : Couleur.values())
            {
                List<Base> basesCouleur = plateau.getBases(c);
                if (!basesCouleur.isEmpty())
                {
                    int indice = j.getId();
                    if (indice >= basesCouleur.size())
                        indice = basesCouleur.size() - 1;
                    j.initialiserChemin(c, basesCouleur.get(indice), plateau);
                }
            }
        }
        etat = Etat.EN_COURS;
        demarrerManche();
    }

    private void demarrerManche()
    {
        mancheActuelle = new Manche(ordreManche.get(indiceManche));
    }

    public Carte piocher() { return mancheActuelle.piocher(); }

    public boolean connecter(Joueur joueur, Sommet sommet)
    {
        CheminCouleur chemin = joueur.getChemin(mancheActuelle.getCouleur());
        if (chemin == null || !peutConnecter(joueur, sommet)) return false;
        chemin.ajouter(sommet);
        return true;
    }

    public boolean peutConnecter(Joueur joueur, Sommet sommet)
    {
        CheminCouleur chemin = joueur.getChemin(mancheActuelle.getCouleur());
        if (chemin == null || chemin.contient(sommet)) return false;
        return chemin.estAdjacent(sommet);
    }

    /**
     * Supprime un sommet du plateau puis recalcule les chemins de tous les
     * joueurs pour répercuter la disparition du sommet et de ses liens.
     */
    public void supprimerSommet(Sommet sommet)
    {
        plateau.supprimerSommet(sommet);
        for (Joueur j : joueurs)
            for (CheminCouleur chemin : j.getChemins().values())
                chemin.recalculer();
    }

    public boolean tourActuelTermine()
    {
        return mancheActuelle.tourTermine() || mancheActuelle.piocheVide();
    }

    public boolean mancheSuivante()
    {
        indiceManche++;
        if (indiceManche >= ordreManche.size())
        {
            etat = Etat.TERMINE;
            return false;
        }
        demarrerManche();
        return true;
    }

    public List<Joueur> getClassement()
    {
        List<Joueur> classement = new ArrayList<>(joueurs);
        // tri du meilleur score au moins bon (petit tri à bulles, simple à lire)
        for (int i = 0; i < classement.size() - 1; i++)
            for (int k = 0; k < classement.size() - 1 - i; k++)
                if (classement.get(k).getScoreTotal() < classement.get(k + 1).getScoreTotal())
                {
                    Joueur tmp = classement.get(k);
                    classement.set(k, classement.get(k + 1));
                    classement.set(k + 1, tmp);
                }
        return classement;
    }

    public Plateau      getPlateau()         { return plateau; }
    public Manche       getMancheActuelle()  { return mancheActuelle; }
    public Etat         getEtat()            { return etat; }
    public List<Joueur> getJoueurs()         { return joueurs; }
    public Couleur      getCouleurActuelle() { return mancheActuelle.getCouleur(); }
}
