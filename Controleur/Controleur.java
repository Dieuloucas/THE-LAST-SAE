package Controleur;

import java.io.*;
import java.util.*;

import Metier.Plateau;
import Metier.Sommet;
import Metier.Zone;

/**
 * Façade unique entre l'IHM et le métier.
 * Gère deux modes : ÉDITION (construction du plateau) et JEU (déroulé de la partie).
 */
public class Controleur
{
    public enum Mode { EDITION, JEU }

    private Plateau plateau;
    private Jeu     jeu;
    private Mode    mode = Mode.EDITION;

    // état du tour de jeu
    private int    indiceJoueur = 0;
    private Joueur joueurActuel;
    private Carte  carteActuelle;

    public Controleur() { }

    public Plateau getPlateau() { return plateau; }
    public Jeu     getJeu()     { return jeu; }
    public Mode    getMode()    { return mode; }

    // ════════════════════════════════════════════════════════════════════════
    //  ÉDITEUR
    // ════════════════════════════════════════════════════════════════════════

    public Plateau creerPlateau(int lignes, int colonnes)
    {
        plateau = new Plateau(lignes, colonnes);
        mode    = Mode.EDITION;
        return plateau;
    }

    public Sommet ajouterSommet(int ligne, int colonne, Symbole symbole)
    {
        Sommet s = plateau.ajouterSommet(ligne, colonne, symbole);
        plateau.genererAretes(); // les arêtes se régénèrent à chaque modification
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

    public void ajouterSommetAZone(Zone zone, Sommet s)
    {
        zone.ajouterSommet(s);
    }

    public Base poserBase(Couleur couleur, Sommet s)
    {
        return plateau.ajouterBase(couleur, s);
    }

    // ── Duplication ────────────────────────────────────────────────────────────

    /** Renvoie une copie indépendante du plateau courant. */
    public Plateau dupliquerPlateau()
    {
        return copier(plateau);
    }

    private Plateau copier(Plateau src)
    {
        Plateau copie = new Plateau(src.getLignes(), src.getColonnes());

        // zones (même ordre → mêmes ids)
        Map<Integer, Zone> zones = new HashMap<>();
        for (Zone z : src.getZones())
            zones.put(z.getId(), copie.ajouterZone());

        // sommets + rattachement à leur zone
        for (Sommet s : src.getSommets())
        {
            Sommet ns = copie.ajouterSommet(s.getLigne(), s.getColonne(), s.getSymbole());
            if (s.getZone() != null)
                zones.get(s.getZone().getId()).ajouterSommet(ns);
        }

        // bases (résolues par position)
        for (Couleur c : Couleur.values())
            for (Base b : src.getBases(c))
            {
                Sommet origine = b.getSommet();
                Sommet ns = copie.getSommet(origine.getLigne(), origine.getColonne());
                copie.ajouterBase(c, ns);
            }

        copie.genererAretes();
        return copie;
    }

    // ── Sauvegarde / chargement ─────────────────────────────────────────────────

    /**
     * Sauvegarde le plateau courant en texte.
     * On ne stocke pas les arêtes : elles sont régénérées au chargement.
     */
    public void sauvegarderPlateau(String chemin) throws IOException
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(chemin)))
        {
            out.println("PLATEAU");
            out.println(plateau.getLignes() + " " + plateau.getColonnes());
            out.println("ZONES " + plateau.getZones().size());

            out.println("SOMMETS");
            for (Sommet s : plateau.getSommets())
            {
                int z = (s.getZone() == null) ? -1 : s.getZone().getId();
                out.println(s.getLigne() + " " + s.getColonne() + " " + s.getSymbole() + " " + z);
            }

            out.println("BASES");
            for (Couleur c : Couleur.values())
                for (Base b : plateau.getBases(c))
                    out.println(c + " " + b.getSommet().getLigne() + " " + b.getSommet().getColonne());
        }
    }

    /** Charge un plateau depuis un fichier texte et le rend courant. */
    public Plateau chargerPlateau(String chemin) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new FileReader(chemin)))
        {
            in.readLine(); // "PLATEAU"
            String[] dim = in.readLine().trim().split("\\s+");
            Plateau p = new Plateau(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));

            // zones
            String[] zl = in.readLine().trim().split("\\s+"); // "ZONES n"
            int nbZones = Integer.parseInt(zl[1]);
            List<Zone> zones = new ArrayList<>();
            for (int i = 0; i < nbZones; i++)
                zones.add(p.ajouterZone());

            // sommets (jusqu'à la ligne "BASES")
            in.readLine(); // "SOMMETS"
            String ligne;
            while ((ligne = in.readLine()) != null && !ligne.equals("BASES"))
            {
                String[] t  = ligne.trim().split("\\s+");
                Sommet   s  = p.ajouterSommet(Integer.parseInt(t[0]),
                                              Integer.parseInt(t[1]),
                                              Symbole.valueOf(t[2]));
                int z = Integer.parseInt(t[3]);
                if (z >= 0)
                    zones.get(z).ajouterSommet(s);
            }

            // bases ("BASES" déjà consommée)
            while ((ligne = in.readLine()) != null)
            {
                String[] t = ligne.trim().split("\\s+");
                Sommet   s = p.getSommet(Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                if (s != null)
                    p.ajouterBase(Couleur.valueOf(t[0]), s);
            }

            p.genererAretes();
            plateau = p;
            mode    = Mode.EDITION;
            return p;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  JEU
    // ════════════════════════════════════════════════════════════════════════

    /** Crée les joueurs à partir de leurs noms et démarre la partie sur le plateau courant. */
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

    /** Tire une carte pour le tour courant. */
    public Carte piocher()
    {
        carteActuelle = jeu.piocher();
        return carteActuelle;
    }

    /**
     * Sommets jouables par le joueur courant : les bouts libres des arêtes
     * disponibles, filtrés par le symbole de la carte tirée (joker = tous).
     */
    public Set<Sommet> getSommetsJouables()
    {
        Set<Sommet> jouables = new HashSet<>();
        if (joueurActuel == null || carteActuelle == null)
            return jouables;

        CheminCouleur chemin = joueurActuel.getChemin(jeu.getCouleurActuelle());
        if (chemin == null)
            return jouables;

        for (Arete a : chemin.getAretesDisponibles())
        {
            Sommet libre = chemin.contient(a.getS1()) ? a.getS2() : a.getS1();
            if (carteActuelle.estJoker() || libre.getSymbole() == carteActuelle.getSymbole())
                jouables.add(libre);
        }
        return jouables;
    }

    /** Connecte un sommet pour le joueur courant (vérifie qu'il est bien jouable). */
    public boolean connecter(Sommet s)
    {
        if (!getSommetsJouables().contains(s))
            return false;
        return jeu.connecter(joueurActuel, s);
    }

    /** Passe au joueur suivant (réseau : même tirage, bases différentes). */
    public void joueurSuivant()
    {
        List<Joueur> joueurs = jeu.getJoueurs();
        indiceJoueur = (indiceJoueur + 1) % joueurs.size();
        joueurActuel = joueurs.get(indiceJoueur);
    }

    /** Vrai si le seuil de cartes est atteint ou la pioche vide. */
    public boolean tourTermine()
    {
        return jeu.tourActuelTermine();
    }

    /** Passe à la manche suivante. Renvoie false si la partie est terminée. */
    public boolean mancheSuivante()
    {
        boolean encore = jeu.mancheSuivante();
        indiceJoueur  = 0;
        joueurActuel  = jeu.getJoueurs().isEmpty() ? null : jeu.getJoueurs().get(0);
        carteActuelle = null;
        return encore;
    }

    public List<Joueur> getClassement() { return jeu.getClassement(); }

    // ── Accesseurs jeu ───────────────────────────────────────────────────────

    public Joueur  getJoueurActuel()    { return joueurActuel; }
    public Carte   getCarteActuelle()   { return carteActuelle; }
    public Couleur getCouleurActuelle() { return jeu.getCouleurActuelle(); }
}
