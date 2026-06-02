package Metier;

class Arete
{
    private Sommet s1;
    private Sommet s2;

    Arete(Sommet s1, Sommet s2)
    {
        this.s1 = s1;
        this.s2 = s2;
    }

    public Sommet getS1() { return s1; }
    public Sommet getS2() { return s2; }

    // renvoie l'autre bout de l'arête
    public Sommet getAutre(Sommet s)
    {
        if (s == this.s1)
            return this.s2;
        else if (s == this.s2)
            return this.s1;
        else
            return null;
    }

    // vrai si l'arête relie bien les deux sommets a et b
    public boolean relie(Sommet a, Sommet b)
    {
        if (a == s1 && b == s2 || a == s2 && b == s1)
            return true;
        else
            return false;
    }
}
