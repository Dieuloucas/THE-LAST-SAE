package controleur;

import java.io.*;
import java.util.*;
import metier.*;

import metier.Plateau;
import ihm.FrameTransport;

// Facade entre l'IHM et le metier : ici on ne fait qu'appeler le metier.
// Deux modes : EDITION (construire le plateau) et JEU (jouer).
public class Controleur
{
    public enum Mode { EDITION, JEU }

    private GestionnairePlateaux gestionnaire = new GestionnairePlateaux();
    private Plateau plateau;
    private Jeu     jeu;
    private Mode    mode = Mode.EDITION;

    // etat du tour de jeu
    private int    indiceJoueur = 0;
    private Joueur joueurActuel;
    private Carte  carteActuelle;

    public Controleur() { new FrameTransport(this);}

    public Plateau              getPlateau()      { return plateau; }
    public Jeu                  getJeu()          { return jeu; }
    public Mode                 getMode()         { return mode; }
    public GestionnairePlateaux getGestionnaire() { return gestionnaire; }

    // ── Editeur ──

    public Plateau creerPlateau(int lignes, int colonnes, String nom)
    {
        plateau = gestionnaire.creer(lignes, colonnes, nom);
        mode    = Mode.EDITION;
        return plateau;
    }

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = plateau.ajouterSommet(ligne, colonne, symbole);
        plateau.genererAretes(); // aretes a refaire apres chaque modif
        return s;
    }

    public void supprimerSommet(Sommet s)
    {
        plateau.supprimerSommet(s);
        plateau.genererAretes();
    }

    public Zone creerZone()
    {
        return plateau.ajouterZone();
    }

    public void ajouterCaseAZone(Zone zone, int ligne, int colonne)
    {
        zone.ajouterCase(ligne, colonne);
    }

    public Base poserBase(Couleur couleur, Sommet s)
    {
        return plateau.ajouterBase(couleur, s);
    }

    // ── Duplication ──

    // copie du plateau courant (le metier fait le travail)
    public Plateau dupliquerPlateau(String nouveauNom)
    {
        return gestionnaire.dupliquer(plateau, nouveauNom);
    }

    // ── Sauvegarde / chargement ──

    public void sauvegarderPlateau(String chemin) throws IOException
    {
        gestionnaire.sauvegarder(plateau, chemin);
    }

    public Plateau chargerPlateau(String chemin) throws IOException
    {
        plateau = gestionnaire.charger(chemin);
        mode    = Mode.EDITION;
        return plateau;
    }

    // ── Jeu ──

    // cree les joueurs et lance la partie sur le plateau courant
    public void demarrerPartie(List<String> noms)
    {
        List<Joueur> joueurs = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++)
            joueurs.add(new Joueur(i, noms.get(i)));

        jeu = new Jeu(plateau, joueurs);
        jeu.demarrer();

        mode          = Mode.JEU;
        indiceJoueur  = 0;
        joueurActuel  = joueurs.isEmpty() ? null : joueurs.get(0);
        carteActuelle = null;
    }

    // pioche la carte du tour
    public Carte piocher()
    {
        carteActuelle = jeu.piocher();
        return carteActuelle;
    }

    // coups possibles du joueur courant (calcule par le metier)
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

    // joueur suivant (meme tirage, base differente)
    public void joueurSuivant()
    {
        List<Joueur> joueurs = jeu.getJoueurs();
        indiceJoueur = (indiceJoueur + 1) % joueurs.size();
        joueurActuel = joueurs.get(indiceJoueur);
    }

    // fin du tour : seuil de cartes atteint ou pioche vide
    public boolean tourTermine()
    {
        return jeu.tourActuelTermine();
    }

    // manche suivante (false si la partie est finie)
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

    public Joueur  getJoueurActuel()    { return joueurActuel; }
    public Carte   getCarteActuelle()   { return carteActuelle; }
    public Couleur getCouleurActuelle() { return jeu.getCouleurActuelle(); }
}
