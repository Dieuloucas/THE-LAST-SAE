package metier;

import java.util.*;

public class CheminCouleur
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

        this.relies.add(base.getSommet());
        this.recalculer();
    }

    public boolean ajouter(Sommet s)
    {
        if (relies.contains(s)) return false; // contient déjà le sommet
        if (!estAdjacent(s))    return false; // n'est pas accessible par un simple lien

        relies.add(s);
        this.recalculer();
        return true;
    }

    public void recalculer()
    {
        this.liens.clear();
        this.aretesDisponibles.clear();
        for (Arete a : this.plateau.getAretes())
        {
            boolean s1Relie = this.relies.contains(a.getS1());
            boolean s2Relie = this.relies.contains(a.getS2());

            if (s1Relie && s2Relie)
                this.liens.add(a);
            else if (s1Relie || s2Relie)
                this.aretesDisponibles.add(a);
        }
    }

    public boolean contient(Sommet s) { return relies.contains(s); }

    public boolean aLeLien(Sommet a, Sommet b)
    {
        for (Arete ar : liens)
            if (ar.relie(a, b))
                return true;
        return false;
    }

    // vrai si s est voisin d'au moins un sommet déjà relié
    public boolean estAdjacent(Sommet s)
    {
        for (Sommet voisin : plateau.getVoisins(s))
            if (relies.contains(voisin))
                return true;
        return false;
    }

    public Couleur      getCouleur()           { return couleur; }
    public List<Sommet> getRelies()            { return relies; }
    public List<Arete>  getLiens()             { return liens; }
    public List<Arete>  getAretesDisponibles() { return aretesDisponibles; }

    public List<Zone> getZonesConquises()
    {
        List<Zone> resultat = new ArrayList<>();
        for (Sommet s : relies)
            for (Zone z : plateau.getZones())
                if (z.contientCase(s.getLigne(), s.getColonne())
                    && !resultat.contains(z))
                    resultat.add(z);
        return resultat;
    }

    public int calculerScore()
    {
        List<Zone> zones = getZonesConquises();
        if (zones.isEmpty()) return 0;

        int max = 0;
        for (Zone z : zones)
        {
            int compte = 0;
            for (Sommet s : relies)
                if (z.contientCase(s.getLigne(), s.getColonne()))
                    compte++;

            if (compte > max)
                max = compte;
        }

        return zones.size() * max;
    }
}
