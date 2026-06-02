package Metier;

import java.util.*;

public class Jeu
{
    public enum Etat { EN_ATTENTE, EN_COURS, TERMINE }

    private Plateau       plateau;
    private List<Joueur>  joueurs;
    private List<Couleur> ordreManche = new ArrayList<>();
    private int           indiceManche = 0;
    private Manche        mancheActuelle;
    private Etat          etat = Etat.EN_ATTENTE;

    public Jeu(Plateau plateau, List<Joueur> joueurs)
    {
        this.plateau = plateau;
        this.joueurs = new ArrayList<>(joueurs);

        for (Couleur c : Couleur.values())
            if (!plateau.getBases(c).isEmpty())
                ordreManche.add(c);

        for (Joueur j : this.joueurs)
            for (Couleur c : ordreManche)
            {
                Base base = plateau.getBases(c).get(0);
                j.initialiserChemin(c, base, plateau);
            }
    }

    public void demarrer()
    {
        etat = Etat.EN_COURS;
        indiceManche = 0;
        demarrerManche();
    }

    private void demarrerManche()
    {
        mancheActuelle = new Manche(ordreManche.get(indiceManche));
    }

    public Carte piocher() { return mancheActuelle.piocher(); }

    public boolean connecter(Joueur joueur, Sommet sommet)
    {
        if (!peutConnecter(joueur, sommet)) return false;
        CheminCouleur chemin = joueur.getChemin(getCouleurActuelle());
        return chemin.ajouter(sommet);
    }

    public boolean peutConnecter(Joueur joueur, Sommet sommet)
    {
        if (etat != Etat.EN_COURS) return false;
        if (sommet == null)        return false;

        CheminCouleur chemin = joueur.getChemin(getCouleurActuelle());
        if (chemin == null)            return false;
        if (chemin.contient(sommet))   return false;
        return chemin.estAdjacent(sommet);
    }

    public void supprimerSommet(Sommet sommet)
    {
        plateau.supprimerSommet(sommet);
        plateau.genererAretes();                       // les arêtes ont changé

        for (Joueur j : joueurs)
            for (CheminCouleur c : j.getChemins().values())
            {
                c.getRelies().remove(sommet);          // au cas où il était dans un réseau
                c.recalculer();
            }
    }

    public boolean tourActuelTermine() { return mancheActuelle.tourTermine(); }

    public boolean mancheSuivante()
    {
        indiceManche++;
        if (indiceManche >= ordreManche.size())
        {
            etat = Etat.TERMINE;
            return false;
        }
        demarrerManche();
        return true;
    }

    public List<Joueur> getClassement()
    {
        List<Joueur> classement = new ArrayList<>(joueurs);
        for (int i = 0; i < classement.size() - 1; i++)
            for (int j = 0; j < classement.size() - 1 - i; j++)
                if (classement.get(j).getScoreTotal() < classement.get(j + 1).getScoreTotal())
                {
                    Joueur tmp = classement.get(j);            // échange
                    classement.set(j, classement.get(j + 1));
                    classement.set(j + 1, tmp);
                }
        return classement;
    }

    public Plateau      getPlateau()         { return plateau; }
    public Manche       getMancheActuelle()  { return mancheActuelle; }
    public Etat         getEtat()            { return etat; }
    public List<Joueur> getJoueurs()         { return joueurs; }
    public Couleur      getCouleurActuelle() { return mancheActuelle.getCouleur(); }
}
