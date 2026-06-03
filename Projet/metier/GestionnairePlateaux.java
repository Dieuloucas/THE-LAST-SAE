package metier;

import java.io.*;
import java.util.*;

public class GestionnairePlateaux
{
    private List<Plateau> plateaux   = new ArrayList<>();
    private int           prochainId = 0;        // id auto-incremente
    private int[]         dernieresCouleurs;     // couleurs des cases gardees en memoire (pour l'IHM)

    public static final int FOND_VIDE = -4144960; // gris clair (LIGHT_GRAY) = case sans zone

    public Plateau creer(int lignes, int colonnes, String nom)
    {
        Plateau p = new Plateau(lignes, colonnes);
        p.setId(prochainId++);                   // id puis +1
        p.setNom(nom);
        plateaux.add(p);
        return p;
    }

    public Plateau dupliquer(Plateau original, String nouveauNom)
    {
        Plateau copie = original.dupliquer();    // la copie profonde
        copie.setId(prochainId++);
        copie.setNom(nouveauNom);
        plateaux.add(copie);
        return copie;
    }

    public void supprimer(Plateau p) { plateaux.remove(p); }

    public Plateau getParId(int id)              // pour le menu
    {
        for (Plateau p : plateaux)
            if (p.getId() == id)
                return p;
        return null;
    }

    public List<Plateau> getPlateaux() { return plateaux; }   // pour le menu

    // ── Persistance (echange entre l'appli d'edition et l'appli de jeu) ──
    // tout est ecrit a la suite, separe par des espaces, pour pouvoir le relire avec un Scanner.
    // les zones sont stockees sous forme de couleurs (1 couleur = 1 zone) ; on ne sauve pas les aretes.

    // l'IHM d'edition envoie les couleurs de TOUTES les cases ; on ajoute les infos du plateau
    public void sauvegarder(int[] couleurs, Plateau p, String chemin) throws IOException
    {
        this.dernieresCouleurs = couleurs;

        try (PrintWriter out = new PrintWriter(new FileWriter(chemin)))
        {
            out.print(p.getLignes() + " " + p.getColonnes() + " ");          // dimensions

            out.print(couleurs.length + " ");                                // couleurs des cases
            for (int c : couleurs) out.print(c + " ");

            out.print(p.getSommets().size() + " ");                          // stations : ligne colonne symbole
            for (Sommet s : p.getSommets())
                out.print(s.getLigne() + " " + s.getColonne() + " " + s.getSymbole() + " ");

            int nbBases = 0;
            for (Couleur c : Couleur.values()) nbBases += p.getBases(c).size();
            out.print(nbBases + " ");                                        // bases : couleur ligne colonne
            for (Couleur c : Couleur.values())
                for (Base b : p.getBases(c))
                    out.print(c + " " + b.getSommet().getLigne() + " " + b.getSommet().getColonne() + " ");
        }
    }

    public int[] getDernieresCouleurs() { return dernieresCouleurs; }

    // charge un plateau produit par l'appli d'edition (et reconstruit les zones a partir des couleurs)
    public Plateau charger(String chemin) throws IOException
    {
        try (Scanner sc = new Scanner(new File(chemin)))
        {
            int lignes = sc.nextInt(), colonnes = sc.nextInt();
            Plateau p = new Plateau(lignes, colonnes);

            int n = sc.nextInt();                          // couleurs des cases
            int[] couleurs = new int[n];
            for (int i = 0; i < n; i++) couleurs[i] = sc.nextInt();
            this.dernieresCouleurs = couleurs;

            int nbS = sc.nextInt();                        // stations
            for (int i = 0; i < nbS; i++)
                p.ajouterSommet(sc.nextInt(), sc.nextInt(), Symbole.valueOf(sc.next()));

            int nbB = sc.nextInt();                        // bases
            for (int i = 0; i < nbB; i++)
            {
                Couleur col = Couleur.valueOf(sc.next());
                Sommet s = p.getSommet(sc.nextInt(), sc.nextInt());
                if (s != null) p.ajouterBase(col, s);
            }

            reconstruireZones(p, couleurs, colonnes);      // couleurs -> objets Zone (pour le score)
            p.genererAretes();
            p.setId(prochainId++);
            plateaux.add(p);
            return p;
        }
    }

    // une couleur (sauf le fond vide) = une zone ; les cases de meme couleur vont dans la meme zone
    private void reconstruireZones(Plateau p, int[] couleurs, int colonnes)
    {
        List<Integer> couleursVues = new ArrayList<>();
        List<Zone>    zones        = new ArrayList<>();

        for (int i = 0; i < couleurs.length; i++)
        {
            int couleur = couleurs[i];
            if (couleur == FOND_VIDE) continue;            // case vide -> pas de zone

            int index = couleursVues.indexOf(couleur);
            Zone zone;
            if (index == -1)                               // nouvelle couleur -> nouvelle zone
            {
                zone = p.ajouterZone();
                couleursVues.add(couleur);
                zones.add(zone);
            }
            else
                zone = zones.get(index);

            zone.ajouterCase(i / colonnes, i % colonnes);  // index -> (ligne, colonne)
        }
    }
}
