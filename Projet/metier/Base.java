package metier;

public class Base
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
