package metier;

import java.io.*;
import java.util.*;

public class GestionnairePlateaux
{
    private List<Plateau> plateaux   = new ArrayList<>();
    private int           prochainId = 0;        // id auto-incremente

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

    // ── Persistance ──
    // on ne sauve pas les aretes (recalculees au chargement) ; zones = leurs cases

    public void sauvegarder(Plateau p, String chemin) throws IOException
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(chemin)))
        {
            out.println("PLATEAU");
            out.println(p.getNom() == null ? "" : p.getNom());
            out.println(p.getLignes() + " " + p.getColonnes());

            out.println("ZONES " + p.getZones().size());
            for (Zone z : p.getZones())
            {
                StringBuilder sb = new StringBuilder();
                sb.append(z.getCases().size());
                for (int[] c : z.getCases())
                    sb.append(" ").append(c[0]).append(" ").append(c[1]);
                out.println(sb.toString());
            }

            out.println("SOMMETS " + p.getSommets().size());
            for (Sommet s : p.getSommets())
                out.println(s.getLigne() + " " + s.getColonne() + " " + s.getSymbole());

            int nbBases = 0;
            for (Couleur c : Couleur.values()) nbBases += p.getBases(c).size();
            out.println("BASES " + nbBases);
            for (Couleur c : Couleur.values())
                for (Base b : p.getBases(c))
                    out.println(c + " " + b.getSommet().getLigne() + " " + b.getSommet().getColonne());
        }
    }

    // charge un plateau et l'enregistre
    public Plateau charger(String chemin) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new FileReader(chemin)))
        {
            in.readLine();                                   // "PLATEAU"
            String nom = in.readLine();                      // nom
            String[] dim = in.readLine().trim().split("\\s+");
            Plateau p = new Plateau(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));

            // zones (une ligne par zone : nbCases l c l c ...)
            int nbZones = Integer.parseInt(in.readLine().trim().split("\\s+")[1]);
            for (int i = 0; i < nbZones; i++)
            {
                Zone z = p.ajouterZone();
                String[] t = in.readLine().trim().split("\\s+");
                int nbCases = Integer.parseInt(t[0]);
                for (int k = 0; k < nbCases; k++)
                    z.ajouterCase(Integer.parseInt(t[1 + k * 2]), Integer.parseInt(t[2 + k * 2]));
            }

            // sommets
            int nbSommets = Integer.parseInt(in.readLine().trim().split("\\s+")[1]);
            for (int i = 0; i < nbSommets; i++)
            {
                String[] t = in.readLine().trim().split("\\s+");
                p.ajouterSommet(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Symbole.valueOf(t[2]));
            }

            // bases
            int nbBases = Integer.parseInt(in.readLine().trim().split("\\s+")[1]);
            for (int i = 0; i < nbBases; i++)
            {
                String[] t = in.readLine().trim().split("\\s+");
                Sommet s = p.getSommet(Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                if (s != null)
                    p.ajouterBase(Couleur.valueOf(t[0]), s);
            }

            p.genererAretes();
            p.setId(prochainId++);
            p.setNom(nom);
            plateaux.add(p);
            return p;
        }
    }
}
