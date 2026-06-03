package metier;

public class Sommet
{
    private static int id = 0;
    private int     ligne;
    private int     colonne;
    private Symbole symbole;

    Sommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet.id++;
        this.ligne   = ligne;
        this.colonne = colonne;
        this.symbole = symbole;
    }

    public int     getId()      { return id; }
    public int     getLigne()   { return ligne; }
    public int     getColonne() { return colonne; }
    public Symbole getSymbole() { return symbole; }

    public void setSymbole(Symbole symbole) { this.symbole = symbole; }
}
