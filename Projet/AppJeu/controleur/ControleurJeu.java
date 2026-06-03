package controleur;

import java.io.*;
import java.util.*;
import metier.*;

// Controleur de l'APPLI 2 : on charge un plateau (fichier txt produit par l'appli d'edition)
// et on joue dessus (mode 2 joueurs ou plus). Il ne fait qu'appeler le metier.
public class ControleurJeu
{
    private final GestionnairePlateaux gestionnaire = new GestionnairePlateaux();
    private Plateau plateau;
    private Jeu     jeu;

    // etat du tour
    private int    indiceJoueur = 0;
    private Joueur joueurActuel;
    private Carte  carteActuelle;

    // ── Chargement du plateau ──

    public Plateau chargerPlateau(String chemin) throws IOException
    {
        plateau = gestionnaire.charger(chemin);
        return plateau;
    }

    public int[]   getCouleurs() { return gestionnaire.getDernieresCouleurs(); } // pour redessiner la grille
    public Plateau getPlateau()  { return plateau; }

    // ── Partie ──

    // cree les joueurs (2 noms pour le mode 2 joueurs) et lance la partie
    public void demarrerPartie(List<String> noms)
    {
        List<Joueur> joueurs = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++)
            joueurs.add(new Joueur(i, noms.get(i)));

        jeu = new Jeu(plateau, joueurs);
        jeu.demarrer();

        indiceJoueur  = 0;
        joueurActuel  = joueurs.isEmpty() ? null : joueurs.get(0);
        carteActuelle = null;
    }

    public Carte piocher() { carteActuelle = jeu.piocher(); return carteActuelle; }

    // coups possibles du joueur courant (regle calculee par le metier)
    public Set<Sommet> getSommetsJouables()
    {
        return jeu.getSommetsJouables(joueurActuel, carteActuelle);
    }

    // joue un sommet pour le joueur courant si le coup est valide
    public boolean connecter(Sommet s)
    {
        if (!jeu.getSommetsJouables(joueurActuel, carteActuelle).contains(s))
            return false;
        return jeu.connecter(joueurActuel, s);
    }

    public void joueurSuivant()
    {
        List<Joueur> joueurs = jeu.getJoueurs();
        indiceJoueur = (indiceJoueur + 1) % joueurs.size();
        joueurActuel = joueurs.get(indiceJoueur);
    }

    public boolean tourTermine() { return jeu.tourActuelTermine(); }

    public boolean mancheSuivante()
    {
        boolean encore = jeu.mancheSuivante();
        indiceJoueur  = 0;
        joueurActuel  = jeu.getJoueurs().isEmpty() ? null : jeu.getJoueurs().get(0);
        carteActuelle = null;
        return encore;
    }

    public List<Joueur> getClassement() { return jeu.getClassement(); }

    // ── Accesseurs jeu ──

    public Jeu     getJeu()             { return jeu; }
    public Joueur  getJoueurActuel()    { return joueurActuel; }
    public Carte   getCarteActuelle()   { return carteActuelle; }
    public Couleur getCouleurActuelle() { return jeu.getCouleurActuelle(); }
}
