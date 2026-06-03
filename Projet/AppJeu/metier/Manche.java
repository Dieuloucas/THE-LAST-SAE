package metier;

public class Manche
{
    private Couleur couleur;
    private Pioche  pioche;
    private int     nbFoncees = 0;
    private int     seuilArret;

    Manche(Couleur couleur)
    {
        this.couleur    = couleur;
        this.pioche     = new Pioche();
        this.seuilArret = (pioche.getNbTotal() - 2) / 2;
    }

    public Couleur getCouleur() { return couleur; }

    public Carte piocher()
    {
        Carte c = pioche.piocher();
        if (c == null) return null;
        if (c.estFonce()) nbFoncees++;
        return c;
    }

    public boolean tourTermine() { return nbFoncees >= seuilArret || pioche.estVide(); }

    public boolean piocheVide()   { return pioche.estVide(); }
    public int     getNbFoncees() { return nbFoncees; }
}
