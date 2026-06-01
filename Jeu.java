package metier;

import java.util.*;

// ── Carte ─────────────────────────────────────────────────────────────────────

class Carte
{
    private final Symbole symbole; // null si joker
    private final Teinte teinte;
    private final boolean joker;

    private Carte(Symbole symbole, Teinte teinte, boolean joker)
    {
        this.symbole = symbole;
        this.teinte  = teinte;
        this.joker   = joker;
    }

    public static Carte de(Symbole symbole, Teinte teinte) { return new Carte(symbole, teinte, false); }
    public static Carte joker(Teinte teinte)               { return new Carte(null, teinte, true); }

    public Symbole getSymbole() { return symbole; }
    public Teinte getTeinte()   { return teinte; }
    public boolean estJoker()   { return joker; }
    public boolean estFonce()   { return teinte == Teinte.FONCE; }

    @Override
    public String toString() { return teinte + (joker ? "_JOKER" : "_" + symbole); }
}

// ── Pioche ────────────────────────────────────────────────────────────────────

class Pioche
{
    private final List<Carte> toutesLesCartes;
    private List<Carte> restantes;

    Pioche()
    {
        toutesLesCartes = new ArrayList<>();
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

    public boolean estVide()         { return restantes.isEmpty(); }
    public int getNbRestantes()      { return restantes.size(); }
}

// ── CheminCouleur ─────────────────────────────────────────────────────────────

class CheminCouleur
{
    private final Couleur couleur;
    private final Base base;
    private final Plateau plateau;
    private final Set<Sommet> relies            = new HashSet<>(); // sommets du réseau
    private final Set<Arete>  liens             = new HashSet<>(); // arêtes internes au réseau
    private final Set<Arete>  aretesDisponibles = new HashSet<>(); // arêtes jouables (frontière)

    CheminCouleur(Couleur couleur, Base base, Plateau plateau)
    {
        this.couleur  = couleur;
        this.base     = base;
        this.plateau  = plateau;
        relies.add(base.getSommet());
        recalculer();
    }

    // ── Ajout d'un sommet ────────────────────────────────────────────────────

    /**
     * Relie un sommet au réseau (il doit être au bout d'une arête disponible).
     * Les arêtes qui le connectent aux sommets déjà reliés deviennent internes,
     * puis on recalcule la frontière.
     */
    public boolean ajouter(Sommet s)
    {
        if (relies.contains(s) || !estAdjacent(s)) return false;
        relies.add(s);

        for (Arete a : plateau.getAretes())
        {
            if (a.getS1() != s && a.getS2() != s) continue; // arête ne touchant pas s
            if (relies.contains(a.getAutre(s)))
                liens.add(a);
        }
        recalculer();
        return true;
    }

    // ── Recalcul de la frontière ──────────────────────────────────────────────

    /**
     * Recalcule les arêtes DISPONIBLES = la frontière du réseau :
     * les arêtes dont un seul des deux sommets est déjà relié.
     * Ce sont les coups jouables, à proposer ensuite au joueur.
     *
     * Nettoie au passage le réseau des sommets / arêtes disparus du plateau,
     * pour que la suppression d'un sommet se répercute directement.
     */
    public void recalculer()
    {
        // 1. retirer du réseau ce qui n'existe plus sur le plateau
        relies.removeIf(s -> !plateau.getSommets().contains(s));
        liens.removeIf(a -> !plateau.getAretes().contains(a));

        // 2. recalculer la frontière
        aretesDisponibles.clear();
        for (Arete a : plateau.getAretes())
        {
            boolean s1Relie = relies.contains(a.getS1());
            boolean s2Relie = relies.contains(a.getS2());
            if (s1Relie ^ s2Relie) // exactement un sommet relié → arête jouable
                aretesDisponibles.add(a);
        }
    }

    // ── Requêtes ─────────────────────────────────────────────────────────────

    /** Vérifie si un sommet est dans le chemin. */
    public boolean contient(Sommet s)
    {
        return relies.contains(s);
    }

    /** Vérifie si une arête directe existe entre deux sommets dans le chemin. */
    public boolean aLeLien(Sommet a, Sommet b)
    {
        for (Arete lien : liens)
            if (lien.relie(a, b)) return true;
        return false;
    }

    /** Vérifie si un sommet est directement adjacent à un sommet déjà relié. */
    public boolean estAdjacent(Sommet s)
    {
        for (Sommet relie : relies)
            for (Arete a : plateau.getAretes())
                if (a.relie(s, relie)) return true;
        return false;
    }

    public Couleur getCouleur()              { return couleur; }
    public Set<Sommet> getRelies()           { return Collections.unmodifiableSet(relies); }
    public Set<Arete> getLiens()             { return Collections.unmodifiableSet(liens); }
    public Set<Arete> getAretesDisponibles() { return Collections.unmodifiableSet(aretesDisponibles); }

    // ── Score ────────────────────────────────────────────────────────────────

    public Set<Zone> getZonesConquises()
    {
        Set<Zone> zones = new HashSet<>();
        for (Sommet s : relies)
            if (s.getZone() != null) zones.add(s.getZone());
        return zones;
    }

    /** Score = nb zones conquises × taille de la plus grosse zone conquise. */
    public int calculerScore()
    {
        Set<Zone> zones = getZonesConquises();
        if (zones.isEmpty()) return 0;
        int tailleMax = zones.stream().mapToInt(Zone::getTaille).max().orElse(0);
        return zones.size() * tailleMax;
    }
}

// ── Manche ────────────────────────────────────────────────────────────────────

class Manche
{
    private final Couleur couleur;
    private final Pioche pioche;
    private int nbFoncees = 0, nbClaires = 0;

    // À ajuster selon les règles exactes
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

    public boolean piocheVide()   { return pioche.estVide(); }
    public int getNbFoncees()     { return nbFoncees; }
    public int getNbClaires()     { return nbClaires; }
}

// ── Joueur ────────────────────────────────────────────────────────────────────

class Joueur
{
    private final int id;
    private final String nom;
    private final Map<Couleur, CheminCouleur> chemins = new EnumMap<>(Couleur.class);

    Joueur(int id, String nom) { this.id = id; this.nom = nom; }

    public int getId()    { return id; }
    public String getNom() { return nom; }

    public void initialiserChemin(Couleur couleur, Base base, Plateau plateau)
    {
        chemins.put(couleur, new CheminCouleur(couleur, base, plateau));
    }

    public CheminCouleur getChemin(Couleur couleur) { return chemins.get(couleur); }

    public Map<Couleur, CheminCouleur> getChemins() { return Collections.unmodifiableMap(chemins); }

    public int getScoreTotal()
    {
        return chemins.values().stream().mapToInt(CheminCouleur::calculerScore).sum();
    }

    public int getScorePourCouleur(Couleur couleur)
    {
        CheminCouleur c = chemins.get(couleur);
        return c == null ? 0 : c.calculerScore();
    }
}

// ── Jeu ───────────────────────────────────────────────────────────────────────

public class Jeu
{
    public enum Etat { EN_ATTENTE, EN_COURS, TERMINE }

    private final Plateau plateau;
    private final List<Joueur> joueurs;
    private final List<Couleur> ordreManche = List.of(Couleur.values());
    private int indiceManche = 0;
    private Manche mancheActuelle;
    private Etat etat = Etat.EN_ATTENTE;

    public Jeu(Plateau plateau, List<Joueur> joueurs)
    {
        this.plateau = plateau;
        this.joueurs = new ArrayList<>(joueurs);
    }

    public void demarrer()
    {
        for (Joueur j : joueurs)
        {
            for (Couleur c : Couleur.values())
            {
                List<Base> bases = plateau.getBases(c);
                if (!bases.isEmpty())
                {
                    Base base = bases.get(Math.min(j.getId(), bases.size() - 1));
                    j.initialiserChemin(c, base, plateau);
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
     * Supprime un sommet du plateau et recalcule tous les chemins de tous
     * les joueurs pour retirer ce sommet et les liens qui en dépendaient.
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
        classement.sort((a, b) -> Integer.compare(b.getScoreTotal(), a.getScoreTotal()));
        return classement;
    }

    public Plateau getPlateau()          { return plateau; }
    public Manche getMancheActuelle()    { return mancheActuelle; }
    public Etat getEtat()                { return etat; }
    public List<Joueur> getJoueurs()     { return Collections.unmodifiableList(joueurs); }
    public Couleur getCouleurActuelle()  { return mancheActuelle.getCouleur(); }
}
