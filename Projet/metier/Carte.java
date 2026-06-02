package metier;

public class Carte
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
