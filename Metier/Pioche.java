package Metier;

import java.util.*;

class Pioche
{
    private List<Carte> toutesLesCartes = new ArrayList<>();
    private List<Carte> restantes       = new ArrayList<>();

    Pioche()
    {
        for (Teinte t : Teinte.values())          // FONCE, CLAIR
        {
            for (Symbole s : Symbole.values())     // les 4 symboles
                toutesLesCartes.add(Carte.de(s, t));
            toutesLesCartes.add(Carte.joker(t));   // + 1 joker par teinte
        }
        this.reinitialiser();
    }

    public void reinitialiser()
    {
        restantes.clear();
        restantes.addAll(toutesLesCartes);
        Collections.shuffle(restantes);
    }

    public Carte piocher()
    {
        return restantes.remove(restantes.size() - 1);
    }

    public boolean estVide()        { return restantes.isEmpty(); }
    public int     getNbRestantes() { return restantes.size(); }
    public int     getNbTotal()     { return toutesLesCartes.size(); }
}
