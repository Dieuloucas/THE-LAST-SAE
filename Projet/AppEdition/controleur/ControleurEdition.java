package controleur;

import java.io.*;
import metier.*;

// Controleur de l'APPLI 1 : creation / edition d'un plateau, puis sauvegarde en fichier txt.
// Il ne fait qu'appeler le metier.
public class ControleurEdition
{
    private final GestionnairePlateaux gestionnaire = new GestionnairePlateaux();
    private Plateau plateau;

    // ── Creation / edition ──

    public Plateau creerPlateau(int lignes, int colonnes, String nom)
    {
        plateau = gestionnaire.creer(lignes, colonnes, nom);
        return plateau;
    }

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = plateau.ajouterSommet(ligne, colonne, symbole);
        plateau.genererAretes();
        return s;
    }

    public void supprimerSommet(Sommet s)
    {
        plateau.supprimerSommet(s);
        plateau.genererAretes();
    }

    public Zone creerZone()                            { return plateau.ajouterZone(); }
    public void ajouterCaseAZone(Zone z, int l, int c) { z.ajouterCase(l, c); }
    public Base poserBase(Couleur couleur, Sommet s)   { return plateau.ajouterBase(couleur, s); }

    public Plateau dupliquerPlateau(String nouveauNom) { return gestionnaire.dupliquer(plateau, nouveauNom); }

    // ── Sauvegarde ──

    // l'IHM envoie le tableau des couleurs de toutes les cases ; le metier ecrit le fichier txt
    public void sauvegarder(int[] couleurs, String chemin) throws IOException
    {
        gestionnaire.sauvegarder(couleurs, plateau, chemin);
    }

    // ── Accesseurs ──

    public Plateau getPlateau()  { return plateau; }
    public int[]   getCouleurs() { return gestionnaire.getDernieresCouleurs(); }
}
