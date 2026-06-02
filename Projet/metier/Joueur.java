package metier;

import java.util.*;

public class Joueur
{
    private int                         id;
    private String                      nom;
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

    public CheminCouleur               getChemin(Couleur couleur) { return chemins.get(couleur); }
    public Map<Couleur, CheminCouleur> getChemins()               { return chemins; }

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
