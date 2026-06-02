package Metier;

class Sommet
{
    private static int id = 0;
    private int     ligne;
    private int     colonne;
    private Symbole symbole;
    private Zone    zone;

    Sommet(int ligne, int colonne, Symbole symbole)
    {
        this.id++;
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
