package metier;

import java.util.*;

// ── Enums ────────────────────────────────────────────────────────────────────

enum Symbole  { CERCLE, CROIX, TRIANGLE, CARRE }
enum Couleur  { ROUGE, VERT, BLEU, MARRON }
enum Teinte   { FONCE, CLAIR }

enum Direction
{
    N(-1, 0), NE(-1, 1), E(0, 1), SE(1, 1),
    S(1, 0),  SW(1, -1), W(0, -1), NW(-1, -1);

    public final int dl, dc; // delta ligne, delta colonne
    Direction(int dl, int dc) { this.dl = dl; this.dc = dc; }
}

// ── Sommet ───────────────────────────────────────────────────────────────────

class Sommet
{
    private final int id;
    private final int ligne, colonne;
    private Symbole symbole;
    private Zone zone;

    Sommet(int id, int ligne, int colonne, Symbole symbole)
    {
        this.id      = id;
        this.ligne   = ligne;
        this.colonne = colonne;
        this.symbole = symbole;
    }

    public int getId()          { return id; }
    public int getLigne()       { return ligne; }
    public int getColonne()     { return colonne; }
    public Symbole getSymbole() { return symbole; }
    public Zone getZone()       { return zone; }

    public void setSymbole(Symbole symbole) { this.symbole = symbole; }
    public void setZone(Zone zone)          { this.zone = zone; }
}

// ── Arete ────────────────────────────────────────────────────────────────────

class Arete
{
    private final Sommet s1, s2;

    Arete(Sommet s1, Sommet s2) { this.s1 = s1; this.s2 = s2; }

    public Sommet getS1() { return s1; }
    public Sommet getS2() { return s2; }

    public Sommet getAutre(Sommet s)
    {
        if (s == s1) return s2;
        if (s == s2) return s1;
        throw new IllegalArgumentException("Sommet absent de cette arête");
    }

    public boolean relie(Sommet a, Sommet b)
    {
        return (s1 == a && s2 == b) || (s1 == b && s2 == a);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Arete a)) return false;
        return relie(a.s1, a.s2);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            Math.min(s1.getId(), s2.getId()),
            Math.max(s1.getId(), s2.getId())
        );
    }
}

// ── Zone ─────────────────────────────────────────────────────────────────────

class Zone
{
    private final int id;
    private final Set<Sommet> sommets = new HashSet<>();

    Zone(int id) { this.id = id; }

    public int getId() { return id; }

    public void ajouterSommet(Sommet s) { sommets.add(s); s.setZone(this); }
    public void retirerSommet(Sommet s) { sommets.remove(s); }

    public Set<Sommet> getSommets() { return Collections.unmodifiableSet(sommets); }
    public int getTaille()          { return sommets.size(); }
}

// ── Base ─────────────────────────────────────────────────────────────────────

class Base
{
    private final Couleur couleur;
    private final Sommet sommet;

    Base(Couleur couleur, Sommet sommet) { this.couleur = couleur; this.sommet = sommet; }

    public Couleur getCouleur() { return couleur; }
    public Sommet getSommet()   { return sommet; }
}

// ── Plateau ───────────────────────────────────────────────────────────────────

public class Plateau
{
    private final int lignes, colonnes;
    private final Map<Integer, Sommet> sommets = new HashMap<>();
    private final Map<String, Sommet> grille   = new HashMap<>();
    private final Set<Arete> aretes            = new HashSet<>();
    private final List<Zone> zones             = new ArrayList<>();
    private final Map<Couleur, List<Base>> bases = new EnumMap<>(Couleur.class);
    private int prochainId = 0;

    public Plateau(int lignes, int colonnes)
    {
        this.lignes   = lignes;
        this.colonnes = colonnes;
        for (Couleur c : Couleur.values())
            bases.put(c, new ArrayList<>());
    }

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = new Sommet(prochainId++, ligne, colonne, symbole);
        sommets.put(s.getId(), s);
        grille.put(cle(ligne, colonne), s);
        return s;
    }

    public void supprimerSommet(Sommet s)
    {
        sommets.remove(s.getId());
        grille.remove(cle(s.getLigne(), s.getColonne()));
        aretes.removeIf(a -> a.getS1() == s || a.getS2() == s);
        if (s.getZone() != null)
            s.getZone().retirerSommet(s);
    }

    public Sommet getSommet(int ligne, int colonne) { return grille.get(cle(ligne, colonne)); }
    public Collection<Sommet> getSommets()          { return Collections.unmodifiableCollection(sommets.values()); }

    public void genererAretes()
    {
        aretes.clear();
        for (Sommet s : sommets.values())
        {
            for (Direction d : Direction.values())
            {
                Sommet voisin = getSommet(s.getLigne() + d.dl, s.getColonne() + d.dc);
                if (voisin != null)
                    aretes.add(new Arete(s, voisin));
            }
        }
    }

    public Set<Arete> getAretes() { return Collections.unmodifiableSet(aretes); }

    public List<Sommet> getVoisins(Sommet s)
    {
        List<Sommet> voisins = new ArrayList<>();
        for (Arete a : aretes)
        {
            if (a.getS1() == s) voisins.add(a.getS2());
            else if (a.getS2() == s) voisins.add(a.getS1());
        }
        return voisins;
    }

    public Zone ajouterZone()
    {
        Zone z = new Zone(zones.size());
        zones.add(z);
        return z;
    }

    public List<Zone> getZones() { return Collections.unmodifiableList(zones); }

    public Base ajouterBase(Couleur couleur, Sommet sommet)
    {
        Base b = new Base(couleur, sommet);
        bases.get(couleur).add(b);
        return b;
    }

    public List<Base> getBases(Couleur couleur) { return Collections.unmodifiableList(bases.get(couleur)); }

    public int getLignes()   { return lignes; }
    public int getColonnes() { return colonnes; }

    private String cle(int ligne, int colonne) { return ligne + "," + colonne; }
}
